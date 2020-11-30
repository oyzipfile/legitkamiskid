// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.combat;

import net.minecraft.entity.Entity;
import me.xiam.creativehack.module.modules.client.InfoOverlay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemEnderEye;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemBow;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.Comparator;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import me.xiam.creativehack.KamiMod;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.event.events.GuiScreenEvent;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.item.Item;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "OffhandGap", category = Category.COMBAT, description = "Holds a God apple when right clicking your sword!")
public class OffhandGap extends Module
{
    private Setting<Double> disableHealth;
    private Setting<Boolean> eatWhileAttacking;
    private Setting<Boolean> swordOrAxeOnly;
    private Setting<Boolean> preferBlocks;
    private Setting<Boolean> crystalCheck;
    int gaps;
    boolean autoTotemWasEnabled;
    boolean cancelled;
    boolean isGuiOpened;
    Item usedItem;
    CrystalAura crystalAura;
    @EventHandler
    private Listener<PacketEvent.Send> sendListener;
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> listener;
    
    public OffhandGap() {
        this.disableHealth = this.register((Setting<Double>)Settings.doubleBuilder("Disable Health").withMinimum(0.0).withValue(4.0).withMaximum(20.0).build());
        this.eatWhileAttacking = this.register(Settings.b("Eat While Attacking", false));
        this.swordOrAxeOnly = this.register(Settings.b("Sword or Axe Only", true));
        this.preferBlocks = this.register(Settings.booleanBuilder("Prefer Placing Blocks").withValue(false).withVisibility(v -> !this.swordOrAxeOnly.getValue()).build());
        this.crystalCheck = this.register(Settings.b("Crystal Check", false));
        this.gaps = -1;
        this.autoTotemWasEnabled = false;
        this.cancelled = false;
        this.isGuiOpened = false;
        EntityEnderCrystal crystal;
        this.sendListener = new Listener<PacketEvent.Send>(e -> {
            if (e.getPacket() instanceof CPacketPlayerTryUseItem) {
                if (this.cancelled) {
                    this.disableGaps();
                    return;
                }
                else if (OffhandGap.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || OffhandGap.mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe || this.passItemCheck()) {
                    if (KamiMod.MODULE_MANAGER.isModuleEnabled(AutoTotem.class)) {
                        this.autoTotemWasEnabled = true;
                        KamiMod.MODULE_MANAGER.getModule(AutoTotem.class).disable();
                    }
                    if (!this.eatWhileAttacking.getValue()) {
                        this.usedItem = OffhandGap.mc.player.getHeldItemMainhand().getItem();
                    }
                    this.enableGaps(this.gaps);
                }
            }
            try {
                if (!OffhandGap.mc.gameSettings.keyBindUseItem.isKeyDown() && OffhandGap.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
                    this.disableGaps();
                }
                else if (this.usedItem != OffhandGap.mc.player.getHeldItemMainhand().getItem() && OffhandGap.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
                    if (!this.eatWhileAttacking.getValue()) {
                        this.usedItem = OffhandGap.mc.player.getHeldItemMainhand().getItem();
                        this.disableGaps();
                    }
                }
                else if (OffhandGap.mc.player.getHealth() + OffhandGap.mc.player.getAbsorptionAmount() <= this.disableHealth.getValue()) {
                    this.disableGaps();
                }
                this.crystalAura = KamiMod.MODULE_MANAGER.getModuleT(CrystalAura.class);
                if (this.crystalCheck.getValue() && this.crystalAura.isEnabled()) {
                    crystal = (EntityEnderCrystal)OffhandGap.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> (EntityEnderCrystal)entity).min(Comparator.comparing(c -> OffhandGap.mc.player.getDistance(c))).orElse(null);
                    if (Objects.requireNonNull(crystal).getPosition().distanceSq((double)OffhandGap.mc.player.getPosition().x, (double)OffhandGap.mc.player.getPosition().y, (double)OffhandGap.mc.player.getPosition().z) <= this.crystalAura.range.getValue()) {
                        this.disableGaps();
                    }
                }
            }
            catch (NullPointerException ex) {}
            return;
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
        this.listener = new Listener<GuiScreenEvent.Displayed>(event -> this.isGuiOpened = (event.getScreen() != null), (Predicate<GuiScreenEvent.Displayed>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (OffhandGap.mc.player == null) {
            return;
        }
        this.cancelled = (OffhandGap.mc.player.getHealth() + OffhandGap.mc.player.getAbsorptionAmount() <= this.disableHealth.getValue());
        if (this.cancelled) {
            this.disableGaps();
            return;
        }
        if (OffhandGap.mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE) {
            for (int i = 0; i < 45; ++i) {
                if (OffhandGap.mc.player.inventory.getStackInSlot(i).getItem() == Items.GOLDEN_APPLE) {
                    this.gaps = i;
                    break;
                }
            }
        }
    }
    
    private boolean passItemCheck() {
        if (this.swordOrAxeOnly.getValue()) {
            return false;
        }
        final Item item = OffhandGap.mc.player.getHeldItemMainhand().getItem();
        return !(item instanceof ItemBow) && !(item instanceof ItemSnowball) && !(item instanceof ItemEgg) && !(item instanceof ItemPotion) && !(item instanceof ItemEnderEye) && !(item instanceof ItemEnderPearl) && !(item instanceof ItemFood) && !(item instanceof ItemShield) && !(item instanceof ItemFlintAndSteel) && !(item instanceof ItemFishingRod) && !(item instanceof ItemArmor) && !(item instanceof ItemExpBottle) && (!this.preferBlocks.getValue() || !(item instanceof ItemBlock));
    }
    
    private void disableGaps() {
        if (this.autoTotemWasEnabled != KamiMod.MODULE_MANAGER.isModuleEnabled(AutoTotem.class)) {
            this.moveGapsWaitForNoGui();
            KamiMod.MODULE_MANAGER.getModule(AutoTotem.class).enable();
            this.autoTotemWasEnabled = false;
        }
    }
    
    private void enableGaps(final int slot) {
        if (OffhandGap.mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE) {
            OffhandGap.mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.player);
            OffhandGap.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.player);
        }
    }
    
    private void moveGapsToInventory(final int slot) {
        if (OffhandGap.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
            OffhandGap.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.player);
            OffhandGap.mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)OffhandGap.mc.player);
        }
    }
    
    private void moveGapsWaitForNoGui() {
        if (this.isGuiOpened) {
            return;
        }
        this.moveGapsToInventory(this.gaps);
    }
    
    @Override
    public String getHudInfo() {
        return String.valueOf(InfoOverlay.getItems(Items.GOLDEN_APPLE));
    }
}

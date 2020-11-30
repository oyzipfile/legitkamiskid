// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.combat;

import me.xiam.creativehack.module.modules.client.InfoOverlay;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.inventory.GuiContainer;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AutoTotem", category = Category.COMBAT, description = "Refills your offhand with totems or other items")
public class AutoTotem extends Module
{
    private Setting<Mode> modeSetting;
    private Setting<Boolean> smartOffhand;
    private Setting<Double> healthSetting;
    private Setting<CustomItem> smartItemSetting;
    int totems;
    boolean moving;
    boolean returnI;
    
    public AutoTotem() {
        this.modeSetting = this.register(Settings.e("Mode", Mode.REPLACE_OFFHAND));
        this.smartOffhand = this.register(Settings.booleanBuilder("Custom Item").withValue(false).withVisibility(v -> this.modeSetting.getValue().equals(Mode.REPLACE_OFFHAND)).build());
        this.healthSetting = this.register(Settings.doubleBuilder("Custom Item Health").withValue(14.0).withVisibility(v -> this.smartOffhand.getValue() && this.modeSetting.getValue().equals(Mode.REPLACE_OFFHAND)).build());
        this.smartItemSetting = this.register((Setting<CustomItem>)Settings.enumBuilder(CustomItem.class).withName("Item").withValue(CustomItem.GAPPLE).withVisibility(v -> this.smartOffhand.getValue()).build());
        this.moving = false;
        this.returnI = false;
    }
    
    @Override
    public void onUpdate() {
        if (!this.modeSetting.getValue().equals(Mode.INVENTORY) && AutoTotem.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.returnI) {
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (AutoTotem.mc.player.inventory.getStackInSlot(i).isEmpty) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            AutoTotem.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
            this.returnI = false;
        }
        this.totems = AutoTotem.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == this.settingToItem()).mapToInt(ItemStack::func_190916_E).sum();
        if (AutoTotem.mc.player.getHeldItemOffhand().getItem() == this.settingToItem()) {
            ++this.totems;
        }
        else {
            if (!this.modeSetting.getValue().equals(Mode.REPLACE_OFFHAND) && !AutoTotem.mc.player.getHeldItemOffhand().isEmpty) {
                return;
            }
            if (this.moving) {
                AutoTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                this.moving = false;
                if (!AutoTotem.mc.player.inventory.itemStack.isEmpty()) {
                    this.returnI = true;
                }
                return;
            }
            if (AutoTotem.mc.player.inventory.itemStack.isEmpty()) {
                if (this.totems == 0) {
                    return;
                }
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (AutoTotem.mc.player.inventory.getStackInSlot(i).getItem() == this.settingToItem()) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                AutoTotem.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                this.moving = true;
            }
            else if (this.modeSetting.getValue().equals(Mode.REPLACE_OFFHAND)) {
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (AutoTotem.mc.player.inventory.getStackInSlot(i).isEmpty) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                AutoTotem.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
            }
        }
    }
    
    private Item settingToItem() {
        if (!this.smartOffhand.getValue() || this.passHealthCheck()) {
            return Items.TOTEM_OF_UNDYING;
        }
        switch (this.smartItemSetting.getValue()) {
            case GAPPLE: {
                return Items.GOLDEN_APPLE;
            }
            case CRYSTAL: {
                return Items.END_CRYSTAL;
            }
            default: {
                return null;
            }
        }
    }
    
    private boolean passHealthCheck() {
        return !this.modeSetting.getValue().equals(Mode.REPLACE_OFFHAND) || AutoTotem.mc.player.getHealth() + AutoTotem.mc.player.getAbsorptionAmount() <= this.healthSetting.getValue();
    }
    
    @Override
    public String getHudInfo() {
        return "" + InfoOverlay.getItems(this.settingToItem());
    }
    
    private enum Mode
    {
        NEITHER, 
        REPLACE_OFFHAND, 
        INVENTORY;
    }
    
    private enum CustomItem
    {
        CRYSTAL, 
        GAPPLE;
    }
}

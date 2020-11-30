// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.combat;

import me.xiam.creativehack.util.InfoCalculator;
import net.minecraft.item.ItemStack;
import me.xiam.creativehack.util.MessageSendHelper;
import java.util.function.Predicate;
import net.minecraft.init.Items;
import me.xiam.creativehack.setting.builder.SettingBuilder;
import me.xiam.creativehack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AutoMend", category = Category.COMBAT, description = "Automatically mends armour")
public class AutoMend extends Module
{
    private Setting<Boolean> autoThrow;
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> autoDisable;
    private Setting<Integer> threshold;
    private int initHotbarSlot;
    @EventHandler
    private Listener<PacketEvent.Receive> receiveListener;
    
    public AutoMend() {
        this.autoThrow = this.register(Settings.b("Auto Throw", true));
        this.autoSwitch = this.register(Settings.b("Auto Switch", true));
        this.autoDisable = this.register(Settings.booleanBuilder("Auto Disable").withValue(false).withVisibility(o -> this.autoSwitch.getValue()).build());
        this.threshold = this.register(Settings.integerBuilder("Repair %").withMinimum(1).withMaximum(100).withValue(75));
        this.initHotbarSlot = -1;
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (AutoMend.mc.player != null && AutoMend.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE) {
                AutoMend.mc.rightClickDelayTimer = 0;
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    @Override
    protected void onEnable() {
        if (AutoMend.mc.player == null) {
            return;
        }
        if (this.autoSwitch.getValue()) {
            this.initHotbarSlot = AutoMend.mc.player.inventory.currentItem;
        }
    }
    
    @Override
    protected void onDisable() {
        if (AutoMend.mc.player == null) {
            return;
        }
        if (this.autoSwitch.getValue() && this.initHotbarSlot != -1 && this.initHotbarSlot != AutoMend.mc.player.inventory.currentItem) {
            AutoMend.mc.player.inventory.currentItem = this.initHotbarSlot;
        }
    }
    
    @Override
    public void onUpdate() {
        if (AutoMend.mc.player == null) {
            return;
        }
        if (this.shouldMend(0) || this.shouldMend(1) || this.shouldMend(2) || this.shouldMend(3)) {
            if (this.autoSwitch.getValue() && AutoMend.mc.player.getHeldItemMainhand().getItem() != Items.EXPERIENCE_BOTTLE) {
                final int xpSlot = this.findXpPots();
                if (xpSlot == -1) {
                    if (this.autoDisable.getValue()) {
                        MessageSendHelper.sendWarningMessage(this.getChatName() + " No XP in hotbar, disabling");
                        this.disable();
                    }
                    return;
                }
                AutoMend.mc.player.inventory.currentItem = xpSlot;
            }
            if (this.autoThrow.getValue() && AutoMend.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE) {
                AutoMend.mc.rightClickMouse();
            }
        }
    }
    
    private int findXpPots() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (AutoMend.mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    
    private boolean shouldMend(final int i) {
        return ((ItemStack)AutoMend.mc.player.inventory.armorInventory.get(i)).getMaxDamage() != 0 && 100 * ((ItemStack)AutoMend.mc.player.inventory.armorInventory.get(i)).getItemDamage() / ((ItemStack)AutoMend.mc.player.inventory.armorInventory.get(i)).getMaxDamage() > InfoCalculator.reverseNumber(this.threshold.getValue(), 1, 100);
    }
}

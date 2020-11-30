// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.movement;

import me.xiam.creativehack.KamiMod;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import me.xiam.creativehack.module.modules.client.InfoOverlay;
import net.minecraft.init.Items;
import net.minecraft.client.gui.inventory.GuiContainer;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "ElytraReplace", description = "Automatically swap and replace your chestplate and elytra. Not an AFK tool, be warned.", category = Category.MOVEMENT)
public class ElytraReplace extends Module
{
    private Setting<InventoryMode> inventoryMode;
    private Setting<Boolean> elytraFlightCheck;
    private boolean currentlyMovingElytra;
    private boolean currentlyMovingChestplate;
    private int elytraCount;
    
    public ElytraReplace() {
        this.inventoryMode = this.register(Settings.e("Inventoryable", InventoryMode.ON));
        this.elytraFlightCheck = this.register(Settings.b("ElytraFlight Check", true));
        this.currentlyMovingElytra = false;
        this.currentlyMovingChestplate = false;
    }
    
    @Override
    public void onUpdate() {
        if (this.inventoryMode.getValue().equals(InventoryMode.OFF) && ElytraReplace.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        this.elytraCount = InfoOverlay.getItems(Items.ELYTRA) + InfoOverlay.getArmor(Items.ELYTRA);
        final int chestplateCount = InfoOverlay.getItems((Item)Items.DIAMOND_CHESTPLATE) + InfoOverlay.getArmor((Item)Items.DIAMOND_CHESTPLATE);
        if (this.currentlyMovingElytra) {
            ElytraReplace.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, (EntityPlayer)ElytraReplace.mc.player);
            this.currentlyMovingElytra = false;
            return;
        }
        if (this.currentlyMovingChestplate) {
            ElytraReplace.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, (EntityPlayer)ElytraReplace.mc.player);
            this.currentlyMovingChestplate = false;
            return;
        }
        if (this.onGround()) {
            int slot = -420;
            if (chestplateCount == 0) {
                return;
            }
            if (((ItemStack)ElytraReplace.mc.player.inventory.armorInventory.get(2)).isEmpty()) {
                for (int i = 0; i < 45; ++i) {
                    if (ElytraReplace.mc.player.inventory.getStackInSlot(i).getItem() == Items.DIAMOND_CHESTPLATE) {
                        slot = i;
                        break;
                    }
                }
                ElytraReplace.mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)ElytraReplace.mc.player);
                this.currentlyMovingElytra = true;
                return;
            }
            if (((ItemStack)ElytraReplace.mc.player.inventory.armorInventory.get(2)).getItem() != Items.DIAMOND_CHESTPLATE) {
                for (int i = 0; i < 45; ++i) {
                    if (ElytraReplace.mc.player.inventory.getStackInSlot(i).getItem() == Items.DIAMOND_CHESTPLATE) {
                        slot = i;
                        break;
                    }
                }
                ElytraReplace.mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)ElytraReplace.mc.player);
                ElytraReplace.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, (EntityPlayer)ElytraReplace.mc.player);
                ElytraReplace.mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)ElytraReplace.mc.player);
                return;
            }
        }
        if (!this.onGround() && this.passElytraFlightCheck()) {
            int slot = -420;
            if (this.elytraCount == 0) {
                return;
            }
            if (((ItemStack)ElytraReplace.mc.player.inventory.armorInventory.get(2)).isEmpty()) {
                for (int i = 0; i < 45; ++i) {
                    if (ElytraReplace.mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA) {
                        slot = i;
                        break;
                    }
                }
                ElytraReplace.mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)ElytraReplace.mc.player);
                this.currentlyMovingElytra = true;
                return;
            }
            if (((ItemStack)ElytraReplace.mc.player.inventory.armorInventory.get(2)).getItem() != Items.ELYTRA) {
                for (int i = 0; i < 45; ++i) {
                    if (ElytraReplace.mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA) {
                        slot = i;
                        break;
                    }
                }
                ElytraReplace.mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)ElytraReplace.mc.player);
                ElytraReplace.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, (EntityPlayer)ElytraReplace.mc.player);
                ElytraReplace.mc.playerController.windowClick(0, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)ElytraReplace.mc.player);
            }
        }
    }
    
    @Override
    public String getHudInfo() {
        return Integer.toString(this.elytraCount);
    }
    
    private boolean onGround() {
        return ElytraReplace.mc.player.onGround;
    }
    
    private boolean passElytraFlightCheck() {
        return (this.elytraFlightCheck.getValue() && KamiMod.MODULE_MANAGER.isModuleEnabled(ElytraFlight.class)) || !this.elytraFlightCheck.getValue();
    }
    
    private enum InventoryMode
    {
        ON, 
        OFF;
    }
}

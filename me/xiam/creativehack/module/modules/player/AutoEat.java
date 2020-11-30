// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.player;

import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AutoEat", description = "Automatically eat when hungry", category = Category.PLAYER)
public class AutoEat extends Module
{
    private Setting<Integer> foodLevel;
    private Setting<Integer> healthLevel;
    private int lastSlot;
    private boolean eating;
    
    public AutoEat() {
        this.foodLevel = this.register((Setting<Integer>)Settings.integerBuilder("Below Hunger").withValue(15).withMinimum(1).withMaximum(20).build());
        this.healthLevel = this.register((Setting<Integer>)Settings.integerBuilder("Below Health").withValue(8).withMinimum(1).withMaximum(20).build());
        this.lastSlot = -1;
        this.eating = false;
    }
    
    private boolean isValid(final ItemStack stack, final int food) {
        return (this.passItemCheck(stack.getItem()) && stack.getItem() instanceof ItemFood && this.foodLevel.getValue() - food >= ((ItemFood)stack.getItem()).getHealAmount(stack)) || (this.passItemCheck(stack.getItem()) && stack.getItem() instanceof ItemFood && this.healthLevel.getValue() - (AutoEat.mc.player.getHealth() + AutoEat.mc.player.getAbsorptionAmount()) > 0.0f);
    }
    
    private boolean passItemCheck(final Item item) {
        return item != Items.ROTTEN_FLESH && item != Items.SPIDER_EYE && item != Items.POISONOUS_POTATO && (item != Items.FISH || new ItemStack(Items.FISH).getItemDamage() != 3);
    }
    
    @Override
    public void onUpdate() {
        if (AutoEat.mc.player == null) {
            return;
        }
        if (this.eating && !AutoEat.mc.player.isHandActive()) {
            if (this.lastSlot != -1) {
                AutoEat.mc.player.inventory.currentItem = this.lastSlot;
                this.lastSlot = -1;
            }
            this.eating = false;
            KeyBinding.setKeyBindState(AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            return;
        }
        if (this.eating) {
            return;
        }
        final FoodStats stats = AutoEat.mc.player.getFoodStats();
        if (this.isValid(AutoEat.mc.player.getHeldItemOffhand(), stats.getFoodLevel())) {
            AutoEat.mc.player.setActiveHand(EnumHand.OFF_HAND);
            this.eating = true;
            KeyBinding.setKeyBindState(AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            AutoEat.mc.playerController.processRightClick((EntityPlayer)AutoEat.mc.player, (World)AutoEat.mc.world, EnumHand.OFF_HAND);
        }
        else {
            for (int i = 0; i < 9; ++i) {
                if (this.isValid(AutoEat.mc.player.inventory.getStackInSlot(i), stats.getFoodLevel())) {
                    this.lastSlot = AutoEat.mc.player.inventory.currentItem;
                    AutoEat.mc.player.inventory.currentItem = i;
                    this.eating = true;
                    KeyBinding.setKeyBindState(AutoEat.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                    AutoEat.mc.playerController.processRightClick((EntityPlayer)AutoEat.mc.player, (World)AutoEat.mc.world, EnumHand.MAIN_HAND);
                    return;
                }
            }
        }
    }
}

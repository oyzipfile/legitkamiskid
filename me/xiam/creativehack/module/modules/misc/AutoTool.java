// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.misc;

import net.minecraft.item.ItemTool;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.block.state.IBlockState;
import java.util.function.Predicate;
import me.xiam.creativehack.setting.Settings;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.module.modules.combat.Aura;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AutoTool", description = "Automatically switch to the best tools when mining or attacking", category = Category.MISC)
public class AutoTool extends Module
{
    private Setting<Aura.HitMode> preferTool;
    @EventHandler
    private Listener<PlayerInteractEvent.LeftClickBlock> leftClickListener;
    @EventHandler
    private Listener<AttackEntityEvent> attackListener;
    
    public AutoTool() {
        this.preferTool = this.register(Settings.e("Prefer", Aura.HitMode.NONE));
        this.leftClickListener = new Listener<PlayerInteractEvent.LeftClickBlock>(event -> this.equipBestTool(AutoTool.mc.world.getBlockState(event.getPos())), (Predicate<PlayerInteractEvent.LeftClickBlock>[])new Predicate[0]);
        this.attackListener = new Listener<AttackEntityEvent>(event -> equipBestWeapon(this.preferTool.getValue()), (Predicate<AttackEntityEvent>[])new Predicate[0]);
    }
    
    private void equipBestTool(final IBlockState blockState) {
        int bestSlot = -1;
        double max = 0.0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoTool.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty) {
                float speed = stack.getDestroySpeed(blockState);
                if (speed > 1.0f) {
                    final int eff;
                    speed += (float)(((eff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)) > 0) ? (Math.pow(eff, 2.0) + 1.0) : 0.0);
                    if (speed > max) {
                        max = speed;
                        bestSlot = i;
                    }
                }
            }
        }
        if (bestSlot != -1) {
            equip(bestSlot);
        }
    }
    
    public static void equipBestWeapon(final Aura.HitMode hitMode) {
        int bestSlot = -1;
        double maxDamage = 0.0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoTool.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty) {
                if (stack.getItem() instanceof ItemAxe || !hitMode.equals(Aura.HitMode.AXE)) {
                    if (stack.getItem() instanceof ItemSword || !hitMode.equals(Aura.HitMode.SWORD)) {
                        if (stack.getItem() instanceof ItemSword && (hitMode.equals(Aura.HitMode.SWORD) || hitMode.equals(Aura.HitMode.NONE))) {
                            final double damage = ((ItemSword)stack.getItem()).getAttackDamage() + (double)EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);
                            if (damage > maxDamage) {
                                maxDamage = damage;
                                bestSlot = i;
                            }
                        }
                        else if (stack.getItem() instanceof ItemAxe && (hitMode.equals(Aura.HitMode.AXE) || hitMode.equals(Aura.HitMode.NONE))) {
                            final double damage = ((ItemTool)stack.getItem()).attackDamage + (double)EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);
                            if (damage > maxDamage) {
                                maxDamage = damage;
                                bestSlot = i;
                            }
                        }
                        else if (stack.getItem() instanceof ItemTool) {
                            final double damage = ((ItemTool)stack.getItem()).attackDamage + (double)EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);
                            if (damage > maxDamage) {
                                maxDamage = damage;
                                bestSlot = i;
                            }
                        }
                    }
                }
            }
        }
        if (bestSlot != -1) {
            equip(bestSlot);
        }
    }
    
    private static void equip(final int slot) {
        AutoTool.mc.player.inventory.currentItem = slot;
        AutoTool.mc.playerController.syncCurrentPlayItem();
    }
}

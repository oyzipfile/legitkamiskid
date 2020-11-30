// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.movement;

import net.minecraft.potion.Potion;
import me.xiam.creativehack.module.Module;

@Info(name = "AntiLevitation", description = "Removes levitation potion effect", category = Category.MOVEMENT)
public class AntiLevitation extends Module
{
    @Override
    public void onUpdate() {
        if (AntiLevitation.mc.player.isPotionActive(Potion.getPotionFromResourceLocation("levitation"))) {
            AntiLevitation.mc.player.removeActivePotionEffect(Potion.getPotionFromResourceLocation("levitation"));
        }
    }
}

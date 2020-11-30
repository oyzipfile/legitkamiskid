// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.misc;

import me.xiam.creativehack.module.Module;

@Info(name = "AntiWeather", description = "Removes rain from your world", category = Category.MISC)
public class AntiWeather extends Module
{
    @Override
    public void onUpdate() {
        if (this.isDisabled()) {
            return;
        }
        if (AntiWeather.mc.world.isRaining()) {
            AntiWeather.mc.world.setRainStrength(0.0f);
        }
    }
}

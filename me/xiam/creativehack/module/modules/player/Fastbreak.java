// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.player;

import me.xiam.creativehack.module.Module;

@Info(name = "Fastbreak", category = Category.PLAYER, description = "Nullifies block hit delay")
public class Fastbreak extends Module
{
    @Override
    public void onUpdate() {
        Fastbreak.mc.playerController.blockHitDelay = 0;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.movement;

import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AutoJump", category = Category.MOVEMENT, description = "Automatically jumps if possible")
public class AutoJump extends Module
{
    private Setting<Integer> delay;
    private static long startTime;
    
    public AutoJump() {
        this.delay = this.register((Setting<Integer>)Settings.integerBuilder("Tick Delay").withValue(10).build());
    }
    
    @Override
    public void onUpdate() {
        if (AutoJump.mc.player.isInWater() || AutoJump.mc.player.isInLava()) {
            AutoJump.mc.player.motionY = 0.1;
        }
        else {
            this.jump();
        }
    }
    
    private void jump() {
        if (AutoJump.mc.player.onGround && this.timeout()) {
            AutoJump.mc.player.jump();
            AutoJump.startTime = 0L;
        }
    }
    
    private boolean timeout() {
        if (AutoJump.startTime == 0L) {
            AutoJump.startTime = System.currentTimeMillis();
        }
        if (AutoJump.startTime + this.delay.getValue() / 20 * 1000 <= System.currentTimeMillis()) {
            AutoJump.startTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
    
    static {
        AutoJump.startTime = 0L;
    }
}

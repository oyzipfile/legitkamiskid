// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.player;

import net.minecraft.util.math.MathHelper;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "YawLock", category = Category.PLAYER, description = "Locks your camera yaw")
public class YawLock extends Module
{
    private Setting<Boolean> auto;
    private Setting<Float> yaw;
    private Setting<Integer> slice;
    
    public YawLock() {
        this.auto = this.register(Settings.b("Auto", true));
        this.yaw = this.register(Settings.f("Yaw", 180.0f));
        this.slice = this.register(Settings.i("Slice", 8));
    }
    
    @Override
    public void onUpdate() {
        if (this.slice.getValue() == 0) {
            return;
        }
        if (this.auto.getValue()) {
            final int angle = 360 / this.slice.getValue();
            float yaw = YawLock.mc.player.rotationYaw;
            yaw = (float)(Math.round(yaw / angle) * angle);
            YawLock.mc.player.rotationYaw = yaw;
            if (YawLock.mc.player.isRiding()) {
                YawLock.mc.player.getRidingEntity().rotationYaw = yaw;
            }
        }
        else {
            YawLock.mc.player.rotationYaw = MathHelper.clamp(this.yaw.getValue() - 180.0f, -180.0f, 180.0f);
        }
    }
}

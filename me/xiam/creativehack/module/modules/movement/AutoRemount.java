// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.movement;

import java.util.Iterator;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.Entity;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AutoRemount", description = "Automatically remounts your horse", category = Category.MOVEMENT)
public class AutoRemount extends Module
{
    private Setting<Mode> modeSetting;
    private Setting<Float> range;
    
    public AutoRemount() {
        this.modeSetting = this.register(Settings.e("Mode", Mode.HORSE));
        this.range = this.register((Setting<Float>)Settings.floatBuilder("Range").withMinimum(1.0f).withValue(1.5f).withMaximum(10.0f).build());
    }
    
    @Override
    public void onUpdate() {
        switch (this.modeSetting.getValue()) {
            case HORSE: {
                for (final Entity e : AutoRemount.mc.world.getLoadedEntityList()) {
                    if (e instanceof EntityHorse && !AutoRemount.mc.player.isRidingHorse()) {
                        final EntityHorse horse = (EntityHorse)e;
                        if (AutoRemount.mc.player.getDistance((Entity)horse) > this.range.getValue()) {
                            continue;
                        }
                        AutoRemount.mc.playerController.interactWithEntity((EntityPlayer)AutoRemount.mc.player, (Entity)horse, EnumHand.MAIN_HAND);
                    }
                }
            }
            case DONKEY: {
                for (final Entity e : AutoRemount.mc.world.getLoadedEntityList()) {
                    if (e instanceof EntityDonkey && !AutoRemount.mc.player.isRidingHorse()) {
                        final EntityDonkey donkey = (EntityDonkey)e;
                        if (AutoRemount.mc.player.getDistance((Entity)donkey) > this.range.getValue()) {
                            continue;
                        }
                        AutoRemount.mc.playerController.interactWithEntity((EntityPlayer)AutoRemount.mc.player, (Entity)donkey, EnumHand.MAIN_HAND);
                    }
                }
                break;
            }
        }
    }
    
    private enum Mode
    {
        HORSE, 
        DONKEY;
    }
}

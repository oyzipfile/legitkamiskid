// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.entity.MoverType;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.event.events.PlayerTravelEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.EntityLivingBase;

@Mixin(value = { EntityPlayer.class }, priority = Integer.MAX_VALUE)
public abstract class MixinEntityPlayer extends EntityLivingBase
{
    public MixinEntityPlayer(final World worldIn) {
        super(worldIn);
    }
    
    @Inject(method = { "travel" }, at = { @At("HEAD") }, cancellable = true)
    public void travel(final float strafe, final float vertical, final float forward, final CallbackInfo info) {
        final PlayerTravelEvent event = new PlayerTravelEvent();
        KamiMod.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            info.cancel();
        }
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import me.xiam.creativehack.module.modules.player.Scaffold;
import me.xiam.creativehack.module.modules.movement.SafeWalk;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.event.events.EntityEvent;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { Entity.class }, priority = Integer.MAX_VALUE)
public class MixinEntity
{
    @Redirect(method = { "applyEntityCollision" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void addVelocity(final Entity entity, final double x, final double y, final double z) {
        final EntityEvent.EntityCollision entityCollisionEvent = new EntityEvent.EntityCollision(entity, x, y, z);
        KamiMod.EVENT_BUS.post(entityCollisionEvent);
        if (entityCollisionEvent.isCancelled()) {
            return;
        }
        entity.motionX += x;
        entity.motionY += y;
        entity.motionZ += z;
        entity.isAirBorne = true;
    }
    
    @Redirect(method = { "move" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"))
    public boolean isSneaking(final Entity entity) {
        return SafeWalk.shouldSafewalk() || Scaffold.shouldScaffold() || entity.isSneaking();
    }
}

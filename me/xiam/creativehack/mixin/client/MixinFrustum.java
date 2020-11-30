// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.modules.player.Freecam;
import me.xiam.creativehack.KamiMod;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Frustum.class })
public abstract class MixinFrustum
{
    @Inject(method = { "Lnet/minecraft/client/renderer/culling/Frustum;isBoundingBoxInFrustum(Lnet/minecraft/util/math/AxisAlignedBB;)Z" }, at = { @At("HEAD") }, cancellable = true)
    public void isBoundingBoxEtc(final AxisAlignedBB ignore, final CallbackInfoReturnable<Boolean> info) {
        if (KamiMod.MODULE_MANAGER.isModuleEnabled(Freecam.class)) {
            info.setReturnValue(true);
        }
    }
}

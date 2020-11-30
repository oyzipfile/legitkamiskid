// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.modules.render.Nametags;
import me.xiam.creativehack.KamiMod;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderPlayer.class })
public class MixinRenderPlayer
{
    @Inject(method = { "renderEntityName" }, at = { @At("HEAD") }, cancellable = true)
    public void renderLivingLabel(final AbstractClientPlayer entityIn, final double x, final double y, final double z, final String name, final double distanceSq, final CallbackInfo info) {
        if (KamiMod.MODULE_MANAGER.isModuleEnabled(Nametags.class)) {
            info.cancel();
        }
    }
}

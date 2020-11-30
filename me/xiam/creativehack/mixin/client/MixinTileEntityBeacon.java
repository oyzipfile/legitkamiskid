// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.modules.render.NoRender;
import me.xiam.creativehack.KamiMod;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.tileentity.TileEntityBeacon;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ TileEntityBeacon.class })
public class MixinTileEntityBeacon
{
    @Inject(method = { "shouldBeamRender" }, at = { @At("HEAD") }, cancellable = true)
    public void shouldBeamRender(final CallbackInfoReturnable<Float> returnable) {
        if (KamiMod.MODULE_MANAGER.isModuleEnabled(NoRender.class) && KamiMod.MODULE_MANAGER.getModuleT(NoRender.class).beacon.getValue()) {
            returnable.setReturnValue(0.0f);
            returnable.cancel();
        }
    }
}

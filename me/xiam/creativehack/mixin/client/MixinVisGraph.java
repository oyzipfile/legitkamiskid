// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import java.util.EnumSet;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.modules.player.Freecam;
import me.xiam.creativehack.KamiMod;
import net.minecraft.util.EnumFacing;
import java.util.Set;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.renderer.chunk.VisGraph;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ VisGraph.class })
public class MixinVisGraph
{
    @Inject(method = { "getVisibleFacings" }, at = { @At("HEAD") }, cancellable = true)
    public void getVisibleFacings(final CallbackInfoReturnable<Set<EnumFacing>> callbackInfo) {
        if (KamiMod.MODULE_MANAGER.isModuleEnabled(Freecam.class)) {
            callbackInfo.setReturnValue(EnumSet.allOf(EnumFacing.class));
        }
    }
}

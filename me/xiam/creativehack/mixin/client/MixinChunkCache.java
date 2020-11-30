// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.modules.render.XRay;
import me.xiam.creativehack.KamiMod;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ChunkCache.class })
public class MixinChunkCache
{
    @Inject(method = { "getBlockState" }, at = { @At("RETURN") }, cancellable = true)
    public void getState(final BlockPos pos, final CallbackInfoReturnable<IBlockState> info) {
        if (KamiMod.MODULE_MANAGER.isModuleEnabled(XRay.class)) {
            info.setReturnValue(XRay.transform(info.getReturnValue()));
        }
    }
}

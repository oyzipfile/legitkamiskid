// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import net.minecraft.block.properties.IProperty;
import me.xiam.creativehack.module.modules.player.LiquidInteract;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.modules.movement.Velocity;
import me.xiam.creativehack.KamiMod;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.BlockLiquid;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ BlockLiquid.class })
public class MixinBlockLiquid
{
    @Inject(method = { "modifyAcceleration" }, at = { @At("HEAD") }, cancellable = true)
    public void modifyAcceleration(final World worldIn, final BlockPos pos, final Entity entityIn, final Vec3d motion, final CallbackInfoReturnable<Vec3d> returnable) {
        if (KamiMod.MODULE_MANAGER.isModuleEnabled(Velocity.class)) {
            returnable.setReturnValue(motion);
            returnable.cancel();
        }
    }
    
    @Inject(method = { "canCollideCheck" }, at = { @At("HEAD") }, cancellable = true)
    public void canCollideCheck(final IBlockState blockState, final boolean b, final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(KamiMod.MODULE_MANAGER.isModuleEnabled(LiquidInteract.class) || (b && (int)blockState.getValue((IProperty)BlockLiquid.LEVEL) == 0));
    }
}

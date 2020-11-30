// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import me.xiam.creativehack.module.modules.player.Freecam;
import net.minecraft.client.entity.AbstractClientPlayer;
import java.util.ArrayList;
import me.xiam.creativehack.module.modules.player.NoEntityTrace;
import java.util.List;
import com.google.common.base.Predicate;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.EntityLivingBase;
import me.xiam.creativehack.module.modules.render.Brightness;
import net.minecraft.potion.Potion;
import net.minecraft.client.entity.EntityPlayerSP;
import me.xiam.creativehack.module.modules.render.NoHurtCam;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.Inject;
import me.xiam.creativehack.module.modules.render.AntiFog;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.modules.misc.CameraClip;
import me.xiam.creativehack.KamiMod;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { EntityRenderer.class }, priority = Integer.MAX_VALUE)
public class MixinEntityRenderer
{
    private boolean nightVision;
    
    public MixinEntityRenderer() {
        this.nightVision = false;
    }
    
    @Redirect(method = { "orientCamera" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;rayTraceBlocks(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/RayTraceResult;"))
    public RayTraceResult rayTraceBlocks(final WorldClient world, final Vec3d start, final Vec3d end) {
        if (KamiMod.MODULE_MANAGER.isModuleEnabled(CameraClip.class)) {
            return null;
        }
        return world.rayTraceBlocks(start, end);
    }
    
    @Inject(method = { "setupFog" }, at = { @At("HEAD") }, cancellable = true)
    public void setupFog(final int startCoords, final float partialTicks, final CallbackInfo callbackInfo) {
        if (AntiFog.enabled() && AntiFog.mode.getValue() == AntiFog.VisionMode.NOFOG) {
            callbackInfo.cancel();
        }
    }
    
    @Redirect(method = { "setupFog" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ActiveRenderInfo;getBlockStateAtEntityViewpoint(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;F)Lnet/minecraft/block/state/IBlockState;"))
    public IBlockState getBlockStateAtEntityViewpoint(final World worldIn, final Entity entityIn, final float p_186703_2_) {
        if (AntiFog.enabled() && AntiFog.mode.getValue() == AntiFog.VisionMode.AIR) {
            return Blocks.AIR.defaultBlockState;
        }
        return ActiveRenderInfo.getBlockStateAtEntityViewpoint(worldIn, entityIn, p_186703_2_);
    }
    
    @Inject(method = { "hurtCameraEffect" }, at = { @At("HEAD") }, cancellable = true)
    public void hurtCameraEffect(final float ticks, final CallbackInfo info) {
        if (NoHurtCam.shouldDisable()) {
            info.cancel();
        }
    }
    
    @Redirect(method = { "updateLightmap" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isPotionActive(Lnet/minecraft/potion/Potion;)Z"))
    public boolean isPotionActive(final EntityPlayerSP player, final Potion potion) {
        final boolean shouldBeActive = Brightness.shouldBeActive();
        this.nightVision = shouldBeActive;
        return shouldBeActive || player.isPotionActive(potion);
    }
    
    @Redirect(method = { "updateLightmap" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;getNightVisionBrightness(Lnet/minecraft/entity/EntityLivingBase;F)F"))
    public float getNightVisionBrightnessMixin(final EntityRenderer renderer, final EntityLivingBase entity, final float partialTicks) {
        if (this.nightVision) {
            return Brightness.getCurrentBrightness();
        }
        return renderer.getNightVisionBrightness(entity, partialTicks);
    }
    
    @Redirect(method = { "getMouseOver" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    public List<Entity> getEntitiesInAABBexcluding(final WorldClient worldClient, final Entity entityIn, final AxisAlignedBB boundingBox, final Predicate predicate) {
        if (NoEntityTrace.shouldBlock()) {
            return new ArrayList<Entity>();
        }
        return (List<Entity>)worldClient.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
    }
    
    @Redirect(method = { "renderWorldPass" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;isSpectator()Z"))
    public boolean noclipIsSpectator(final AbstractClientPlayer acp) {
        return KamiMod.MODULE_MANAGER.isModuleEnabled(Freecam.class) || acp.isSpectator();
    }
}

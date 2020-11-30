// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.renderer.GlStateManager;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.modules.movement.EntitySpeed;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.util.Wrapper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBoat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ModelBoat.class })
public class MixinModelBoat
{
    @Inject(method = { "render" }, at = { @At("HEAD") })
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final CallbackInfo info) {
        if (Wrapper.getPlayer().getRidingEntity() == entityIn && KamiMod.MODULE_MANAGER.isModuleEnabled(EntitySpeed.class)) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, EntitySpeed.getOpacity());
            GlStateManager.enableBlend();
        }
    }
}

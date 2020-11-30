// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.modules.movement.EntitySpeed;
import me.xiam.creativehack.KamiMod;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.entity.passive.EntityLlama;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityLlama.class })
public class MixinEntityLlama
{
    @Inject(method = { "canBeSteered" }, at = { @At("RETURN") }, cancellable = true)
    public void canBeSteered(final CallbackInfoReturnable<Boolean> returnable) {
        if (KamiMod.MODULE_MANAGER.isModuleEnabled(EntitySpeed.class)) {
            returnable.setReturnValue(true);
        }
    }
}

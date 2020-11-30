// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.xiam.creativehack.module.modules.capes.Capes;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ AbstractClientPlayer.class })
public class MixinAbstractClientPlayer
{
    @Inject(method = { "getLocationCape" }, at = { @At("RETURN") }, cancellable = true)
    public void getCape(final CallbackInfoReturnable<ResourceLocation> callbackInfo) {
        if (Capes.INSTANCE == null) {
            return;
        }
        if (!Capes.INSTANCE.overrideOtherCapes.getValue() && callbackInfo.getReturnValue() != null) {
            return;
        }
        final ResourceLocation kamiCape = Capes.getCapeResource((AbstractClientPlayer)this);
        if (kamiCape != null) {
            callbackInfo.setReturnValue(kamiCape);
        }
    }
}

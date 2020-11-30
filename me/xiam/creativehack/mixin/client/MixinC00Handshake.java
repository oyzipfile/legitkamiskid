// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.modules.misc.FakeVanillaClient;
import me.xiam.creativehack.KamiMod;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.EnumConnectionState;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ C00Handshake.class })
public class MixinC00Handshake
{
    @Shadow
    int protocolVersion;
    @Shadow
    String ip;
    @Shadow
    int port;
    @Shadow
    EnumConnectionState requestedState;
    
    @Inject(method = { "writePacketData" }, at = { @At("HEAD") }, cancellable = true)
    public void writePacketData(final PacketBuffer buf, final CallbackInfo info) {
        if (KamiMod.MODULE_MANAGER.isModuleEnabled(FakeVanillaClient.class)) {
            info.cancel();
            buf.writeVarInt(this.protocolVersion);
            buf.writeString(this.ip);
            buf.writeShort(this.port);
            buf.writeVarInt(this.requestedState.getId());
        }
    }
}

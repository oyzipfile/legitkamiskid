// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.player;

import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketInput;
import me.xiam.creativehack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "IllegalBypass", description = "You need this for creatice mode as well", category = Category.PLAYER)
public class PacketCancel extends Module
{
    private Setting<Boolean> all;
    private Setting<Boolean> packetInput;
    private Setting<Boolean> packetPlayer;
    private Setting<Boolean> packetEntityAction;
    private Setting<Boolean> packetUseEntity;
    private Setting<Boolean> packetVehicleMove;
    private int numPackets;
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener;
    
    public PacketCancel() {
        this.all = this.register(Settings.b("IllegalBypassV1", false));
        this.packetInput = this.register(Settings.booleanBuilder("coming soon...").withValue(true).withVisibility(v -> !this.all.getValue()));
        this.packetPlayer = this.register(Settings.booleanBuilder("coming soon...").withValue(true).withVisibility(v -> !this.all.getValue()));
        this.packetEntityAction = this.register(Settings.booleanBuilder("coming soon...").withValue(true).withVisibility(v -> !this.all.getValue()));
        this.packetUseEntity = this.register(Settings.booleanBuilder("coming soon...").withValue(true).withVisibility(v -> !this.all.getValue()));
        this.packetVehicleMove = this.register(Settings.booleanBuilder("coming soon...").withValue(true).withVisibility(v -> !this.all.getValue()));
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (this.all.getValue() || (this.packetInput.getValue() && event.getPacket() instanceof CPacketInput) || (this.packetPlayer.getValue() && event.getPacket() instanceof CPacketPlayer) || (this.packetEntityAction.getValue() && event.getPacket() instanceof CPacketEntityAction) || (this.packetUseEntity.getValue() && event.getPacket() instanceof CPacketUseEntity) || (this.packetVehicleMove.getValue() && event.getPacket() instanceof CPacketVehicleMove)) {
                event.cancel();
                ++this.numPackets;
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    public void onDisable() {
        this.numPackets = 0;
    }
    
    @Override
    public String getHudInfo() {
        return Integer.toString(this.numPackets);
    }
}

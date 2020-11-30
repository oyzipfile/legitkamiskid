// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.misc;

import java.util.function.Predicate;
import net.minecraft.network.Packet;
import me.xiam.creativehack.util.Wrapper;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.network.play.client.CPacketCustomPayload;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.module.Module;

@Info(name = "BeaconSelector", category = Category.MISC, description = "Choose any of the 5 beacon effects regardless of beacon base height")
public class BeaconSelector extends Module
{
    public static int effect;
    private boolean doCancelPacket;
    @EventHandler
    public Listener<PacketEvent.Send> packetListener;
    
    public BeaconSelector() {
        this.doCancelPacket = true;
        PacketBuffer data;
        int i1;
        int k1;
        PacketBuffer buf;
        this.packetListener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketCustomPayload && ((CPacketCustomPayload)event.getPacket()).getChannelName().equals("MC|Beacon") && this.doCancelPacket) {
                this.doCancelPacket = false;
                data = ((CPacketCustomPayload)event.getPacket()).getBufferData();
                i1 = data.readInt();
                k1 = data.readInt();
                event.cancel();
                buf = new PacketBuffer(Unpooled.buffer());
                buf.writeInt(BeaconSelector.effect);
                buf.writeInt(k1);
                Wrapper.getPlayer().connection.sendPacket((Packet)new CPacketCustomPayload("MC|Beacon", buf));
                this.doCancelPacket = true;
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    static {
        BeaconSelector.effect = -1;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.misc;

import me.xiam.creativehack.util.MessageSendHelper;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import java.util.function.Predicate;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.network.play.client.CPacketUpdateSign;
import me.xiam.creativehack.util.Wrapper;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.module.Module;

@Info(name = "ConsoleSpam", description = "Spams Spigot consoles by sending invalid UpdateSign packets", category = Category.MISC)
public class ConsoleSpam extends Module
{
    @EventHandler
    public Listener<PacketEvent.Send> sendListener;
    
    public ConsoleSpam() {
        BlockPos location;
        NetHandlerPlayClient connection;
        final CPacketUpdateSign cPacketUpdateSign;
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                location = ((CPacketPlayerTryUseItemOnBlock)event.getPacket()).getPos();
                connection = Wrapper.getPlayer().connection;
                new CPacketUpdateSign(location, new TileEntitySign().signText);
                connection.sendPacket((Packet)cPacketUpdateSign);
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    public void onEnable() {
        MessageSendHelper.sendChatMessage(this.getChatName() + " Every time you right click a sign, a warning will appear in console.");
        MessageSendHelper.sendChatMessage(this.getChatName() + " Use an autoclicker to automate this process.");
    }
}

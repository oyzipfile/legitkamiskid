// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.chat;

import me.xiam.creativehack.util.MessageSendHelper;
import net.minecraft.client.Minecraft;
import java.util.function.Predicate;
import net.minecraft.network.Packet;
import me.xiam.creativehack.util.Wrapper;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.module.Module;

@Info(name = "FormatChat", description = "Add colour and linebreak support to upstream chat packets", category = Category.CHAT)
public class FormatChat extends Module
{
    @EventHandler
    public Listener<PacketEvent.Send> sendListener;
    
    public FormatChat() {
        String message;
        String message2;
        String message3;
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketChatMessage) {
                message = ((CPacketChatMessage)event.getPacket()).message;
                if (message.contains("&") || message.contains("#n")) {
                    message2 = message.replaceAll("&", "§");
                    message3 = message2.replaceAll("#n", "\n");
                    Wrapper.getPlayer().connection.sendPacket((Packet)new CPacketChatMessage(message3));
                    event.cancel();
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    public void onEnable() {
        if (Minecraft.getMinecraft().getCurrentServerData() == null) {
            MessageSendHelper.sendWarningMessage(this.getChatName() + " &6&lWarning: &r&6This does not work in singleplayer");
            this.disable();
        }
        else {
            MessageSendHelper.sendWarningMessage(this.getChatName() + " &6&lWarning: &r&6This will kick you on most servers!");
        }
    }
}

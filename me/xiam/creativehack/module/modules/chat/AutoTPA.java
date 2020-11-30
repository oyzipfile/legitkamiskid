// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.chat;

import java.util.function.Predicate;
import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.util.Friends;
import net.minecraft.network.play.server.SPacketChat;
import me.xiam.creativehack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AutoTPA", description = "Automatically decline or accept TPA requests", category = Category.CHAT)
public class AutoTPA extends Module
{
    private Setting<Boolean> friends;
    private Setting<mode> mod;
    @EventHandler
    public Listener<PacketEvent.Receive> receiveListener;
    
    public AutoTPA() {
        this.friends = this.register(Settings.b("Always accept friends", true));
        this.mod = this.register(Settings.e("Response", mode.DENY));
        String firstWord;
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (event.getPacket() instanceof SPacketChat && ((SPacketChat)event.getPacket()).getChatComponent().getUnformattedText().contains(" has requested to teleport to you.")) {
                firstWord = ((SPacketChat)event.getPacket()).getChatComponent().getUnformattedText().split("\\s+")[0];
                if (this.friends.getValue() && Friends.isFriend(firstWord)) {
                    MessageSendHelper.sendServerMessage("/tpaccept");
                }
                else {
                    switch (this.mod.getValue()) {
                        case ACCEPT: {
                            MessageSendHelper.sendServerMessage("/tpaccept");
                            break;
                        }
                        case DENY: {
                            MessageSendHelper.sendServerMessage("/tpdeny");
                            break;
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    public enum mode
    {
        ACCEPT, 
        DENY;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.chat;

import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.command.Command;
import com.mrpowergamerbr.temmiewebhook.DiscordMessage;
import com.mrpowergamerbr.temmiewebhook.TemmieWebhook;
import me.xiam.creativehack.util.TimeUtil;
import me.xiam.creativehack.module.modules.client.InfoOverlay;
import net.minecraft.client.gui.GuiDisconnected;
import me.xiam.creativehack.KamiMod;
import net.minecraft.client.multiplayer.GuiConnecting;
import java.util.function.Predicate;
import me.xiam.creativehack.util.MessageDetectionHelper;
import net.minecraft.network.play.server.SPacketChat;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.event.events.GuiScreenEvent;
import me.xiam.creativehack.event.events.ServerDisconnectedEvent;
import me.xiam.creativehack.event.events.ServerConnectedEvent;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.multiplayer.ServerData;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "DiscordNotifs", category = Category.CHAT, description = "Sends your chat to a set Discord channel", alwaysListening = true)
public class DiscordNotifs extends Module
{
    private Setting<Boolean> timeout;
    private Setting<Integer> timeoutTime;
    private Setting<Boolean> time;
    private Setting<Boolean> importantPings;
    private Setting<Boolean> disconnect;
    private Setting<Boolean> all;
    private Setting<Boolean> queue;
    private Setting<Boolean> restart;
    private Setting<Boolean> direct;
    private Setting<Boolean> directSent;
    public Setting<String> url;
    public Setting<String> pingID;
    public Setting<String> avatar;
    private static ServerData cServer;
    @EventHandler
    public Listener<PacketEvent.Receive> listener0;
    @EventHandler
    public Listener<ServerConnectedEvent> listener1;
    @EventHandler
    public Listener<ServerDisconnectedEvent> listener2;
    private static long startTime;
    @EventHandler
    public Listener<GuiScreenEvent.Closed> serverConnectedEvent;
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> serverDisconnectedEvent;
    
    public DiscordNotifs() {
        this.timeout = this.register(Settings.b("Timeout", true));
        this.timeoutTime = this.register(Settings.integerBuilder().withName("Seconds").withMinimum(0).withMaximum(120).withValue(10).withVisibility(v -> this.timeout.getValue()).build());
        this.time = this.register(Settings.b("Timestamp", true));
        this.importantPings = this.register(Settings.b("Important Pings", false));
        this.disconnect = this.register(Settings.b("Disconnect Msgs", true));
        this.all = this.register(Settings.b("All Messages", false));
        this.queue = this.register(Settings.booleanBuilder("Queue Position").withValue(true).withVisibility(v -> !this.all.getValue()).build());
        this.restart = this.register(Settings.booleanBuilder("Restart Msgs").withValue(true).withVisibility(v -> !this.all.getValue()).build());
        this.direct = this.register(Settings.booleanBuilder("Received DMs").withValue(true).withVisibility(v -> !this.all.getValue()).build());
        this.directSent = this.register(Settings.booleanBuilder("Send DMs").withValue(true).withVisibility(v -> !this.all.getValue()).build());
        this.url = this.register(Settings.s("URL", "unchanged"));
        this.pingID = this.register(Settings.s("Ping ID", "unchanged"));
        this.avatar = this.register(Settings.s("Avatar", "https://i.imgur.com/1BfO0hT.png"));
        SPacketChat sPacketChat;
        String message;
        this.listener0 = new Listener<PacketEvent.Receive>(event -> {
            if (DiscordNotifs.mc.player == null || this.isDisabled()) {
                return;
            }
            else if (!(event.getPacket() instanceof SPacketChat)) {
                return;
            }
            else {
                sPacketChat = (SPacketChat)event.getPacket();
                message = sPacketChat.getChatComponent().getUnformattedText();
                if (this.timeout(message) && MessageDetectionHelper.shouldSend(this.all.getValue(), this.restart.getValue(), this.direct.getValue(), this.directSent.getValue(), this.queue.getValue(), this.importantPings.getValue(), message)) {
                    this.sendMessage(this.getPingID(message) + MessageDetectionHelper.getMessageType(this.direct.getValue(), this.directSent.getValue(), message, this.getServer()) + this.getTime() + message, this.avatar.getValue());
                }
                return;
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        this.listener1 = new Listener<ServerConnectedEvent>(event -> {
            if (this.isDisabled()) {
                return;
            }
            else if (!this.disconnect.getValue()) {
                return;
            }
            else {
                this.sendMessage(this.getPingID("KamiBlueMessageType1") + this.getTime() + MessageDetectionHelper.getMessageType(this.direct.getValue(), this.directSent.getValue(), "KamiBlueMessageType1", this.getServer()), this.avatar.getValue());
                return;
            }
        }, (Predicate<ServerConnectedEvent>[])new Predicate[0]);
        this.listener2 = new Listener<ServerDisconnectedEvent>(event -> {
            if (this.isDisabled()) {
                return;
            }
            else if (!this.disconnect.getValue()) {
                return;
            }
            else {
                this.sendMessage(this.getPingID("KamiBlueMessageType2") + this.getTime() + MessageDetectionHelper.getMessageType(this.direct.getValue(), this.directSent.getValue(), "KamiBlueMessageType2", this.getServer()), this.avatar.getValue());
                return;
            }
        }, (Predicate<ServerDisconnectedEvent>[])new Predicate[0]);
        this.serverConnectedEvent = new Listener<GuiScreenEvent.Closed>(event -> {
            if (this.isEnabled() && event.getScreen() instanceof GuiConnecting) {
                DiscordNotifs.cServer = DiscordNotifs.mc.currentServerData;
                KamiMod.EVENT_BUS.post(new ServerConnectedEvent());
            }
            return;
        }, (Predicate<GuiScreenEvent.Closed>[])new Predicate[0]);
        this.serverDisconnectedEvent = new Listener<GuiScreenEvent.Displayed>(event -> {
            if (this.isEnabled() && event.getScreen() instanceof GuiDisconnected && (DiscordNotifs.cServer != null || DiscordNotifs.mc.currentServerData != null)) {
                KamiMod.EVENT_BUS.post(new ServerDisconnectedEvent());
            }
        }, (Predicate<GuiScreenEvent.Displayed>[])new Predicate[0]);
    }
    
    private boolean timeout(final String message) {
        if (!this.timeout.getValue()) {
            return true;
        }
        if (MessageDetectionHelper.isRestart(this.restart.getValue(), message) || MessageDetectionHelper.isDirect(this.direct.getValue(), message) || MessageDetectionHelper.isDirectOther(this.directSent.getValue(), message)) {
            return true;
        }
        if (DiscordNotifs.startTime == 0L) {
            DiscordNotifs.startTime = System.currentTimeMillis();
        }
        if (DiscordNotifs.startTime + this.timeoutTime.getValue() * 1000 <= System.currentTimeMillis()) {
            DiscordNotifs.startTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
    
    private String getPingID(final String message) {
        if (MessageDetectionHelper.isRestart(this.restart.getValue(), message) || MessageDetectionHelper.isDirect(this.direct.getValue(), message) || MessageDetectionHelper.isDirectOther(this.directSent.getValue(), message) || MessageDetectionHelper.isImportantQueue(this.importantPings.getValue(), message)) {
            return this.formatPingID();
        }
        if (message.equals("KamiBlueMessageType1") || message.equals("KamiBlueMessageType2")) {
            return this.formatPingID();
        }
        return "";
    }
    
    private String formatPingID() {
        if (!this.importantPings.getValue()) {
            return "";
        }
        return "<@!" + this.pingID.getValue() + ">: ";
    }
    
    private String getServer() {
        if (DiscordNotifs.cServer == null) {
            return "the server";
        }
        return DiscordNotifs.cServer.serverIP;
    }
    
    private String getTime() {
        if (!this.time.getValue() || KamiMod.MODULE_MANAGER.isModuleEnabled(ChatTimestamp.class)) {
            return "";
        }
        final InfoOverlay info = KamiMod.MODULE_MANAGER.getModuleT(InfoOverlay.class);
        return "[" + TimeUtil.getFinalTime(info.timeUnitSetting.getValue(), info.timeTypeSetting.getValue(), info.doLocale.getValue()) + "] ";
    }
    
    private void sendMessage(final String content, final String avatarUrl) {
        final TemmieWebhook tm = new TemmieWebhook(this.url.getValue());
        final DiscordMessage dm = new DiscordMessage("KAMI Blue v1.1.3", content, avatarUrl);
        tm.sendMessage(dm);
    }
    
    @Override
    public void onUpdate() {
        if (this.isDisabled()) {
            return;
        }
        if (this.url.getValue().equals("unchanged")) {
            MessageSendHelper.sendErrorMessage(this.getChatName() + "You must first set a webhook url with the '&7" + Command.getCommandPrefix() + "discordnotifs&r' command");
            this.disable();
        }
        else if (this.pingID.getValue().equals("unchanged") && this.importantPings.getValue()) {
            MessageSendHelper.sendErrorMessage(this.getChatName() + "For Pings to work, you must set a Discord ID with the '&7" + Command.getCommandPrefix() + "discordnotifs&r' command");
            this.disable();
        }
    }
    
    static {
        DiscordNotifs.startTime = 0L;
    }
}

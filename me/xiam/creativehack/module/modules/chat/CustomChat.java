// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.chat;

import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.command.Command;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.xiam.creativehack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "CustomChat", category = Category.CHAT, description = "Add a custom suffix to the end of your message!", showOnArray = ShowOnArray.OFF)
public class CustomChat extends Module
{
    public Setting<TextMode> textMode;
    private Setting<DecoMode> decoMode;
    private Setting<Boolean> commands;
    public Setting<String> customText;
    public static String[] cmdCheck;
    @EventHandler
    public Listener<PacketEvent.Send> listener;
    private static long startTime;
    
    public CustomChat() {
        this.textMode = this.register(Settings.e("Message", TextMode.ON_TOP));
        this.decoMode = this.register(Settings.e("Separator", DecoMode.NONE));
        this.commands = this.register(Settings.b("Commands", false));
        this.customText = this.register(Settings.stringBuilder("Custom Text").withValue("unchanged").withConsumer((old, value) -> {}).build());
        String s;
        String s2;
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketChatMessage) {
                s = ((CPacketChatMessage)event.getPacket()).getMessage();
                if (this.commands.getValue() || !this.isCommand(s)) {
                    s2 = s + this.getFull(this.decoMode.getValue());
                    if (s2.length() >= 256) {
                        s2 = s2.substring(0, 256);
                    }
                    ((CPacketChatMessage)event.getPacket()).message = s2;
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    private String getText(final TextMode t) {
        switch (t) {
            case NAME: {
                return "\u1d0b\u1d00\u1d0d\u026a \u0299\u029f\u1d1c\u1d07";
            }
            case ON_TOP: {
                return "\u1d0b\u1d00\u1d0d\u026a \u0299\u029f\u1d1c\u1d07 \u1d0f\u0274 \u1d1b\u1d0f\u1d18";
            }
            case WEBSITE: {
                return "\u0299\u029f\u1d1c\u1d07.\u0299\u1d07\u029f\u029f\u1d00.\u1d21\u1d1b\u0493";
            }
            case JAPANESE: {
                return "\u4e0a\u306b\u30ab\u30df\u30d6\u30eb\u30fc";
            }
            case CUSTOM: {
                return this.customText.getValue();
            }
            default: {
                return "";
            }
        }
    }
    
    private String getFull(final DecoMode d) {
        switch (d) {
            case NONE: {
                return " " + this.getText(this.textMode.getValue());
            }
            case CLASSIC: {
                return " « " + this.getText(this.textMode.getValue()) + " »";
            }
            case SEPARATOR: {
                return " \u23d0 " + this.getText(this.textMode.getValue());
            }
            default: {
                return "";
            }
        }
    }
    
    private boolean isCommand(final String s) {
        for (final String value : CustomChat.cmdCheck) {
            if (s.startsWith(value)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onUpdate() {
        if (CustomChat.startTime == 0L) {
            CustomChat.startTime = System.currentTimeMillis();
        }
        if (CustomChat.startTime + 5000L <= System.currentTimeMillis()) {
            if (this.textMode.getValue().equals(TextMode.CUSTOM) && this.customText.getValue().equalsIgnoreCase("unchanged") && CustomChat.mc.player != null) {
                MessageSendHelper.sendWarningMessage(this.getChatName() + " Warning: In order to use the custom " + this.getName() + ", please run the &7" + Command.getCommandPrefix() + "customchat&r command to change it");
            }
            CustomChat.startTime = System.currentTimeMillis();
        }
    }
    
    static {
        CustomChat.cmdCheck = new String[] { "/", ",", ".", "-", ";", "?", "*", "^", "&", Command.getCommandPrefix() };
        CustomChat.startTime = 0L;
    }
    
    private enum DecoMode
    {
        SEPARATOR, 
        CLASSIC, 
        NONE;
    }
    
    public enum TextMode
    {
        NAME, 
        ON_TOP, 
        WEBSITE, 
        JAPANESE, 
        CUSTOM;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.launchwrapper.LogWrapper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.Minecraft;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.KamiMod;

public class MessageSendHelper
{
    public static void sendChatMessage(final String message) {
        sendRawChatMessage("&7[&9Cr4tiv3h4ck&7] &r" + message);
    }
    
    public static void sendWarningMessage(final String message) {
        sendRawChatMessage("&7[&6Cr4tiv3h4ck&7] &r" + message);
    }
    
    public static void sendErrorMessage(final String message) {
        sendRawChatMessage("&7[&4Cr4tiv3h4ck&7] &r" + message);
    }
    
    public static void sendCustomMessage(final String message, final String colour) {
        sendRawChatMessage("&7[" + colour + "Cr4tiv3h4ck" + "&7] &r" + message);
    }
    
    public static void sendStringChatMessage(final String[] messages) {
        sendChatMessage("");
        for (final String s : messages) {
            sendRawChatMessage(s);
        }
    }
    
    public static void sendDisableMessage(final Class clazz) {
        sendErrorMessage("Error: The " + KamiMod.MODULE_MANAGER.getModule(clazz).getName() + " module is only for configuring the GUI element. In order to show the GUI element you need to hit the pin in the upper left of the GUI element");
        KamiMod.MODULE_MANAGER.getModule(clazz).enable();
    }
    
    public static void sendRawChatMessage(final String message) {
        if (Minecraft.getMinecraft().player != null) {
            Wrapper.getPlayer().sendMessage((ITextComponent)new ChatMessage(message));
        }
        else {
            LogWrapper.info(message, new Object[0]);
        }
    }
    
    public static void sendServerMessage(final String message) {
        if (Minecraft.getMinecraft().player != null) {
            Wrapper.getPlayer().connection.sendPacket((Packet)new CPacketChatMessage(message));
        }
        else {
            LogWrapper.warning("Could not send server message: \"" + message + "\"", new Object[0]);
        }
    }
    
    public static class ChatMessage extends TextComponentBase
    {
        String text;
        
        ChatMessage(final String text) {
            final Pattern p = Pattern.compile("&[0123456789abcdefrlosmk]");
            final Matcher m = p.matcher(text);
            final StringBuffer sb = new StringBuffer();
            while (m.find()) {
                final String replacement = "§" + m.group().substring(1);
                m.appendReplacement(sb, replacement);
            }
            m.appendTail(sb);
            this.text = sb.toString();
        }
        
        public String getUnformattedComponentText() {
            return this.text;
        }
        
        public ITextComponent createCopy() {
            return (ITextComponent)new ChatMessage(this.text);
        }
    }
}

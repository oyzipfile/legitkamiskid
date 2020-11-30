// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.util;

import java.util.regex.Pattern;

public class MessageDetectionHelper
{
    public static String getMessageType(final boolean direct, final boolean directSent, final String message, final String server) {
        if (isDirect(direct, message)) {
            return "You got a direct message!\n";
        }
        if (isDirectOther(directSent, message)) {
            return "You sent a direct message!\n";
        }
        if (message.equals("KamiBlueMessageType1")) {
            return "Connected to " + server;
        }
        if (message.equals("KamiBlueMessageType2")) {
            return "Disconnected from " + server;
        }
        return "";
    }
    
    public static boolean isDirect(final boolean direct, final String message) {
        return direct && message.contains("whispers:");
    }
    
    public static boolean isDirectOther(final boolean directSent, final String message) {
        return directSent && Pattern.compile("to [0-9A-Za-z_]+:", 2).matcher(message).find();
    }
    
    public static boolean isQueue(final boolean queue, final String message) {
        return (queue && message.contains("Position in queue:")) || (queue && message.contains("2b2t is full"));
    }
    
    public static boolean isImportantQueue(final boolean importantPings, final String message) {
        return importantPings && (message.equals("Position in queue: 1") || message.equals("Position in queue: 2") || message.equals("Position in queue: 3"));
    }
    
    public static boolean isRestart(final boolean restart, final String message) {
        return restart && message.contains("[SERVER] Server restarting in");
    }
    
    public static boolean shouldSend(final boolean all, final boolean restart, final boolean direct, final boolean directSent, final boolean queue, final boolean importantPings, final String message) {
        return all || isRestart(restart, message) || isDirect(direct, message) || isDirectOther(directSent, message) || isQueue(queue, message) || isImportantQueue(importantPings, message);
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack;

import net.minecraft.client.Minecraft;
import me.xiam.creativehack.util.RichPresence;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordPresence
{
    public static DiscordRichPresence presence;
    private static boolean connected;
    private static final DiscordRPC rpc;
    private static String details;
    private static String state;
    private static me.xiam.creativehack.module.modules.misc.DiscordRPC discordRPC;
    
    public static void start() {
        KamiMod.log.info("Starting Discord RPC");
        if (DiscordPresence.connected) {
            return;
        }
        DiscordPresence.connected = true;
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        DiscordPresence.rpc.Discord_Initialize("782918759219593216", handlers, true, "");
        DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        setRpcFromSettings();
        new Thread(DiscordPresence::setRpcFromSettingsNonInt, "Discord-RPC-Callback-Handler").start();
        KamiMod.log.info("Discord RPC initialised successfully");
    }
    
    public static void end() {
        KamiMod.log.info("Shutting down Discord RPC...");
        DiscordPresence.connected = false;
        DiscordPresence.rpc.Discord_Shutdown();
    }
    
    private static void setRpcFromSettingsNonInt() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                DiscordPresence.rpc.Discord_RunCallbacks();
                DiscordPresence.discordRPC = KamiMod.MODULE_MANAGER.getModuleT(me.xiam.creativehack.module.modules.misc.DiscordRPC.class);
                final String separator = " | ";
                DiscordPresence.details = DiscordPresence.discordRPC.getLine(DiscordPresence.discordRPC.line1Setting.getValue()) + separator + DiscordPresence.discordRPC.getLine(DiscordPresence.discordRPC.line3Setting.getValue());
                DiscordPresence.state = DiscordPresence.discordRPC.getLine(DiscordPresence.discordRPC.line2Setting.getValue()) + separator + DiscordPresence.discordRPC.getLine(DiscordPresence.discordRPC.line4Setting.getValue());
                DiscordPresence.presence.details = DiscordPresence.details;
                DiscordPresence.presence.state = DiscordPresence.state;
                DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                Thread.sleep(4000L);
            }
            catch (InterruptedException e3) {
                e3.printStackTrace();
            }
        }
    }
    
    private static void setRpcFromSettings() {
        DiscordPresence.discordRPC = KamiMod.MODULE_MANAGER.getModuleT(me.xiam.creativehack.module.modules.misc.DiscordRPC.class);
        DiscordPresence.details = DiscordPresence.discordRPC.getLine(DiscordPresence.discordRPC.line1Setting.getValue()) + " " + DiscordPresence.discordRPC.getLine(DiscordPresence.discordRPC.line3Setting.getValue());
        DiscordPresence.state = DiscordPresence.discordRPC.getLine(DiscordPresence.discordRPC.line2Setting.getValue()) + " " + DiscordPresence.discordRPC.getLine(DiscordPresence.discordRPC.line4Setting.getValue());
        DiscordPresence.presence.details = DiscordPresence.details;
        DiscordPresence.presence.state = DiscordPresence.state;
        DiscordPresence.presence.largeImageKey = "cr4tiv3h4ck";
        DiscordPresence.presence.largeImageText = "Best client ever made";
        DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
    }
    
    public static void setCustomIcons() {
        if (RichPresence.INSTANCE.customUsers != null) {
            for (final RichPresence.CustomUser user : RichPresence.INSTANCE.customUsers) {
                if (user.uuid.equalsIgnoreCase(Minecraft.getMinecraft().session.getProfile().getId().toString())) {
                    switch (Integer.parseInt(user.type)) {
                        case 0: {
                            DiscordPresence.presence.smallImageKey = "booster";
                            DiscordPresence.presence.smallImageText = "booster uwu";
                            break;
                        }
                        case 1: {
                            DiscordPresence.presence.smallImageKey = "inviter";
                            DiscordPresence.presence.smallImageText = "inviter owo";
                            break;
                        }
                        case 2: {
                            DiscordPresence.presence.smallImageKey = "giveaway";
                            DiscordPresence.presence.smallImageText = "giveaway winner";
                            break;
                        }
                        case 3: {
                            DiscordPresence.presence.smallImageKey = "contest";
                            DiscordPresence.presence.smallImageText = "contest winner";
                            break;
                        }
                        case 4: {
                            DiscordPresence.presence.smallImageKey = "nine";
                            DiscordPresence.presence.smallImageText = "900th member";
                            break;
                        }
                        case 5: {
                            DiscordPresence.presence.smallImageKey = "github1";
                            DiscordPresence.presence.smallImageText = "contributor!! uwu";
                            break;
                        }
                        default: {
                            DiscordPresence.presence.smallImageKey = "donator2";
                            DiscordPresence.presence.smallImageText = "donator <3";
                            break;
                        }
                    }
                }
            }
        }
    }
    
    static {
        rpc = DiscordRPC.INSTANCE;
        DiscordPresence.presence = new DiscordRichPresence();
        DiscordPresence.connected = false;
    }
}

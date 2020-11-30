// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.module.modules.chat.DiscordNotifs;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import me.xiam.creativehack.command.Command;

public class DiscordNotifsCommand extends Command
{
    public DiscordNotifsCommand() {
        super("discordnotifs", new ChunkBuilder().append("webhook url").append("discord id").append("avatar url").build(), new String[] { "webhook" });
    }
    
    @Override
    public void call(final String[] args) {
        final DiscordNotifs df = KamiMod.MODULE_MANAGER.getModuleT(DiscordNotifs.class);
        if (args[0] != null && !args[0].equals("")) {
            df.url.setValue(args[0]);
            MessageSendHelper.sendChatMessage(df.getChatName() + "Set URL to \"" + args[0] + "\"!");
        }
        else if (args[0] == null) {
            MessageSendHelper.sendErrorMessage(df.getChatName() + "Error: you must specify a URL or \"\" for the first parameter when running the command");
        }
        if (args[1] == null) {
            return;
        }
        if (!args[1].equals("")) {
            df.pingID.setValue(args[1]);
            MessageSendHelper.sendChatMessage(df.getChatName() + "Set Discord ID to \"" + df.pingID.getValue() + "\"!");
        }
        if (args[2] == null) {
            return;
        }
        if (!args[2].equals("")) {
            df.avatar.setValue(args[2]);
            MessageSendHelper.sendChatMessage(df.getChatName() + "Set Avatar to \"" + args[2] + "\"!");
        }
        else {
            df.avatar.setValue("https://i.imgur.com/1BfO0hT.png");
            MessageSendHelper.sendChatMessage(df.getChatName() + "Reset Avatar!");
        }
    }
}

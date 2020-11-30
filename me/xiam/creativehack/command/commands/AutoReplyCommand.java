// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.module.modules.chat.AutoReply;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import me.xiam.creativehack.command.Command;

public class AutoReplyCommand extends Command
{
    public AutoReplyCommand() {
        super("autoreply", new ChunkBuilder().append("message").append("=listener").append("-replyCommand").build(), new String[] { "reply" });
        this.setDescription("Allows you to customize AutoReply's settings");
    }
    
    @Override
    public void call(final String[] args) {
        final AutoReply autoReply = KamiMod.MODULE_MANAGER.getModuleT(AutoReply.class);
        if (autoReply == null) {
            MessageSendHelper.sendErrorMessage("&cThe AutoReply module is not available for some reason. Make sure the name you're calling is correct and that you have the module installed!!");
            return;
        }
        if (!autoReply.isEnabled()) {
            MessageSendHelper.sendWarningMessage("&6Warning: The AutoReply module is not enabled!");
            MessageSendHelper.sendWarningMessage("The command will still work, but will not visibly do anything.");
        }
        for (final String s : args) {
            if (s != null) {
                if (s.startsWith("=")) {
                    final String sT = s.replace("=", "");
                    autoReply.listener.setValue(sT);
                    MessageSendHelper.sendChatMessage("Set the AutoReply listener to <" + sT + ">");
                    if (!autoReply.customListener.getValue()) {
                        MessageSendHelper.sendWarningMessage("&6Warning: You don't have Custom Listener enabled in AutoReply!");
                        MessageSendHelper.sendWarningMessage("The command will still work, but will not visibly do anything.");
                    }
                }
                else if (s.startsWith("-")) {
                    final String sT = s.replace("-", "");
                    autoReply.replyCommand.setValue(sT);
                    MessageSendHelper.sendChatMessage("Set the AutoReply reply command to <" + sT + ">");
                    if (!autoReply.customReplyCommand.getValue()) {
                        MessageSendHelper.sendWarningMessage("&6Warning: You don't have Custom Reply Command enabled in AutoReply!");
                        MessageSendHelper.sendWarningMessage("The command will still work, but will not visibly do anything.");
                    }
                }
                else {
                    autoReply.message.setValue(s);
                    MessageSendHelper.sendChatMessage("Set the AutoReply message to <" + s + ">");
                    if (!autoReply.customMessage.getValue()) {
                        MessageSendHelper.sendWarningMessage("&6Warning: You don't have Custom Message enabled in AutoReply!");
                        MessageSendHelper.sendWarningMessage("The command will still work, but will not visibly do anything.");
                    }
                }
            }
        }
    }
}

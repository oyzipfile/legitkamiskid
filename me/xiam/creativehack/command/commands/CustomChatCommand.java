// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.module.modules.chat.CustomChat;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import me.xiam.creativehack.command.Command;

public class CustomChatCommand extends Command
{
    public CustomChatCommand() {
        super("customchat", new ChunkBuilder().append("ending").build(), new String[] { "chat" });
        this.setDescription("Allows you to customize CustomChat's custom setting");
    }
    
    @Override
    public void call(final String[] args) {
        final CustomChat cC = KamiMod.MODULE_MANAGER.getModuleT(CustomChat.class);
        if (cC == null) {
            MessageSendHelper.sendErrorMessage("&cThe CustomChat module is not available for some reason. Make sure the name you're calling is correct and that you have the module installed!!");
            return;
        }
        if (!cC.isEnabled()) {
            MessageSendHelper.sendWarningMessage("&6Warning: The CustomChat module is not enabled!");
            MessageSendHelper.sendWarningMessage("The command will still work, but will not visibly do anything.");
        }
        if (!cC.textMode.getValue().equals(CustomChat.TextMode.CUSTOM)) {
            MessageSendHelper.sendWarningMessage("&6Warning: You don't have custom mode enabled in CustomChat!");
            MessageSendHelper.sendWarningMessage("The command will still work, but will not visibly do anything.");
        }
        for (final String s : args) {
            if (s != null) {
                cC.customText.setValue(s);
                MessageSendHelper.sendChatMessage("Set the Custom Text Mode to <" + s + ">");
            }
        }
    }
}

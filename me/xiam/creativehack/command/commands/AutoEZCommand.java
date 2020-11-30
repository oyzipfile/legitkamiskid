// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.module.modules.combat.AutoEZ;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import me.xiam.creativehack.command.Command;

public class AutoEZCommand extends Command
{
    public AutoEZCommand() {
        super("autoez", new ChunkBuilder().append("message").build(), new String[0]);
        this.setDescription("Allows you to customize AutoEZ's custom setting");
    }
    
    @Override
    public void call(final String[] args) {
        final AutoEZ az = KamiMod.MODULE_MANAGER.getModuleT(AutoEZ.class);
        if (az == null) {
            MessageSendHelper.sendErrorMessage("&cThe AutoEZ module is not available for some reason. Make sure the name you're calling is correct and that you have the module installed!!");
            return;
        }
        if (!az.isEnabled()) {
            MessageSendHelper.sendWarningMessage("&6Warning: The AutoEZ module is not enabled!");
            MessageSendHelper.sendWarningMessage("The command will still work, but will not visibly do anything.");
        }
        if (!az.mode.getValue().equals(AutoEZ.Mode.CUSTOM)) {
            MessageSendHelper.sendWarningMessage("&6Warning: You don't have custom mode enabled in AutoEZ!");
            MessageSendHelper.sendWarningMessage("The command will still work, but will not visibly do anything.");
        }
        for (final String s : args) {
            if (s != null) {
                az.customText.setValue(s);
                MessageSendHelper.sendChatMessage("Set the Custom Mode to <" + s + ">");
            }
        }
    }
}

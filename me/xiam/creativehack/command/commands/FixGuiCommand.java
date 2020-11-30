// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.module.modules.hidden.FixGui;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import me.xiam.creativehack.command.Command;

public class FixGuiCommand extends Command
{
    public FixGuiCommand() {
        super("fixgui", new ChunkBuilder().build(), new String[0]);
        this.setDescription("Allows you to disable the automatic gui positioning");
    }
    
    @Override
    public void call(final String[] args) {
        KamiMod.MODULE_MANAGER.getModuleT(FixGui.class).enable();
        MessageSendHelper.sendChatMessage(this.getChatLabel() + "Ran");
    }
}

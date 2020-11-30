// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.ModuleManager;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.command.syntax.SyntaxParser;
import me.xiam.creativehack.command.syntax.parsers.ModuleParser;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import me.xiam.creativehack.command.Command;

public class ToggleCommand extends Command
{
    public ToggleCommand() {
        super("toggle", new ChunkBuilder().append("module", true, new ModuleParser()).build(), new String[] { "t" });
        this.setDescription("Quickly toggle a module on and off");
    }
    
    @Override
    public void call(final String[] args) {
        if (args.length == 0) {
            MessageSendHelper.sendChatMessage("Please specify a module!");
            return;
        }
        try {
            final Module m = KamiMod.MODULE_MANAGER.getModule(args[0]);
            m.toggle();
            MessageSendHelper.sendChatMessage(m.getName() + (m.isEnabled() ? " &aenabled" : " &cdisabled"));
        }
        catch (ModuleManager.ModuleNotFoundException x) {
            MessageSendHelper.sendChatMessage("Unknown module '" + args[0] + "'");
        }
    }
}

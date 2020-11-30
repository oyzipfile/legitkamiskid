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

public class RenameModuleCommand extends Command
{
    public RenameModuleCommand() {
        super("renamemodule", new ChunkBuilder().append("module", true, new ModuleParser()).append("name").build(), new String[0]);
        this.setDescription("Rename a module to something else");
    }
    
    @Override
    public void call(final String[] args) {
        if (args.length == 0) {
            MessageSendHelper.sendChatMessage("Please specify a module!");
            return;
        }
        try {
            final Module module = KamiMod.MODULE_MANAGER.getModule(args[0]);
            final String name = (args.length == 1) ? module.getOriginalName() : args[1];
            if (!name.matches("[a-zA-Z]+")) {
                MessageSendHelper.sendChatMessage("Name must be alphabetic!");
                return;
            }
            MessageSendHelper.sendChatMessage("&b" + module.getName() + "&r renamed to &b" + name);
            module.setName(name);
        }
        catch (ModuleManager.ModuleNotFoundException x) {
            MessageSendHelper.sendChatMessage("Unknown module '" + args[0] + "'!");
        }
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import me.xiam.creativehack.util.MessageSendHelper;
import java.util.function.Function;
import java.util.Comparator;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.command.syntax.SyntaxChunk;
import me.xiam.creativehack.command.Command;

public class CommandsCommand extends Command
{
    public CommandsCommand() {
        super("commands", SyntaxChunk.EMPTY, new String[] { "cmds" });
        this.setDescription("Gives you this list of commands");
    }
    
    @Override
    public void call(final String[] args) {
        KamiMod.getInstance().getCommandManager().getCommands().stream().sorted(Comparator.comparing((Function<? super Object, ? extends Comparable>)Command::getLabel)).forEach(command -> MessageSendHelper.sendChatMessage("&f" + Command.getCommandPrefix() + command.getLabel() + "&r ~ &7" + command.getDescription()));
    }
}

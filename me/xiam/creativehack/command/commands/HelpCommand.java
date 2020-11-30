// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import java.util.Arrays;
import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.modules.ClickGUI;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.command.syntax.SyntaxChunk;
import me.xiam.creativehack.command.Command;

public class HelpCommand extends Command
{
    private static final Subject[] subjects;
    private static String subjectsList;
    
    public HelpCommand() {
        super("help", null, new String[] { "?" });
        this.setDescription("Delivers help on certain subjects. Use &f" + Command.getCommandPrefix() + "help subjects&7 for a list.");
    }
    
    @Override
    public void call(final String[] args) {
        final String commandPrefix = Command.getCommandPrefix();
        if (args[0] == null) {
            MessageSendHelper.sendStringChatMessage(new String[] { "KAMI Blue v1.1.3", "&7Press &r" + KamiMod.MODULE_MANAGER.getModule(ClickGUI.class).getBindName() + "&7 to open GUI", "&7see &bhttps://blue.bella.wtf&7 for a full version of the faq", commandPrefix + "description&7 to see the description of a module", commandPrefix + "commands&7 to view all available commands", commandPrefix + "bind <module> <key>&7 to bind mods", commandPrefix + "prefix <prefix>&r to change the command prefix.", commandPrefix + "help &7<bind|subjects:[subject]>&r for more help." });
        }
        else {
            final String subject3 = args[0];
            if (subject3.equals("subjects")) {
                MessageSendHelper.sendChatMessage("Subjects: " + HelpCommand.subjectsList);
            }
            else if (subject3.equals("bind")) {
                MessageSendHelper.sendChatMessage("You can also use &7.bind&r modifiers on to allow modules to be bound to keybinds with modifiers, e.g &7ctrl + shift + w or ctrl + c.&r");
                MessageSendHelper.sendChatMessage("You can unbind modules with backspace in the GUI or by running &7.bind <module> none&r");
            }
            else {
                final String[] names;
                final int length;
                int i = 0;
                String name;
                final String anotherString;
                final Subject subject4 = Arrays.stream(HelpCommand.subjects).filter(subject2 -> {
                    names = subject2.names;
                    length = names.length;
                    while (i < length) {
                        name = names[i];
                        if (name.equalsIgnoreCase(anotherString)) {
                            return true;
                        }
                        else {
                            ++i;
                        }
                    }
                    return false;
                }).findFirst().orElse(null);
                if (subject4 == null) {
                    MessageSendHelper.sendChatMessage("No help found for &b" + args[0]);
                    return;
                }
                MessageSendHelper.sendStringChatMessage(subject4.info);
            }
        }
    }
    
    static {
        subjects = new Subject[] { new Subject(new String[] { "type", "int", "boolean", "double", "float" }, new String[] { "Every module has a value, and that value is always of a certain &btype.\n", "These types are displayed in kami as the ones java use. They mean the following:", "&bboolean&r: Enabled or not. Values &3true/false", "&bfloat&r: A number with a decimal point", "&bdouble&r: Like a float, but a more accurate decimal point", "&bint&r: A number with no decimal point" }) };
        HelpCommand.subjectsList = "";
        for (final Subject subject : HelpCommand.subjects) {
            HelpCommand.subjectsList = HelpCommand.subjectsList + subject.names[0] + ", ";
        }
        HelpCommand.subjectsList = HelpCommand.subjectsList.substring(0, HelpCommand.subjectsList.length() - 2);
    }
    
    private static class Subject
    {
        String[] names;
        String[] info;
        
        Subject(final String[] names, final String[] info) {
            this.names = names;
            this.info = info;
        }
    }
}

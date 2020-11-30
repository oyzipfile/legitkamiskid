// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import me.xiam.creativehack.util.MessageSendHelper;
import java.util.function.Function;
import java.util.Comparator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.xiam.creativehack.KamiMod;
import java.util.List;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.command.syntax.SyntaxChunk;
import me.xiam.creativehack.command.Command;

public class GenerateWebsiteCommand extends Command
{
    public GenerateWebsiteCommand() {
        super("genwebsite", null, new String[0]);
        this.setDescription("Generates the module page for the website");
    }
    
    private static String nameAndDescription(final Module module) {
        return "<li>" + module.getName() + "<p><i>" + module.getDescription() + "</i></p></li>";
    }
    
    @Override
    public void call(final String[] args) {
        final List<Module> mods = KamiMod.MODULE_MANAGER.getModules().stream().filter(Module::isProduction).collect((Collector<? super Module, ?, List<Module>>)Collectors.toList());
        final String[] modCategories = { "Chat", "Combat", "Client", "Misc", "Movement", "Player", "Render" };
        final List<String> modCategoriesList = new ArrayList<String>(Arrays.asList(modCategories));
        final List<String> modsChat = new ArrayList<String>();
        final List<String> modsCombat = new ArrayList<String>();
        final List<String> modsClient = new ArrayList<String>();
        final List<String> modsMisc = new ArrayList<String>();
        final List<String> modsMovement = new ArrayList<String>();
        final List<String> modsPlayer = new ArrayList<String>();
        final List<String> modsRender = new ArrayList<String>();
        final List<String> list;
        final List<String> list2;
        final List<String> list3;
        final List<String> list4;
        final List<String> list5;
        final List<String> list6;
        final List<String> list7;
        mods.forEach(module -> {
            switch (module.getCategory()) {
                case CHAT: {
                    list.add(nameAndDescription(module));
                }
                case COMBAT: {
                    list2.add(nameAndDescription(module));
                }
                case CLIENT: {
                    list3.add(nameAndDescription(module));
                }
                case MISC: {
                    list4.add(nameAndDescription(module));
                }
                case MOVEMENT: {
                    list5.add(nameAndDescription(module));
                }
                case PLAYER: {
                    list6.add(nameAndDescription(module));
                }
                case RENDER: {
                    list7.add(nameAndDescription(module));
                    break;
                }
            }
            return;
        });
        KamiMod.log.info("\n---\nlayout: default\ntitle: Modules\ndescription: A list of modules and commands this mod has\n---\n## Modules (" + mods.size() + ")\n");
        final Collection collection;
        final int totalMods;
        modCategoriesList.forEach(modCategory -> {
            totalMods = (int)collection.stream().filter(module -> module.getCategory().toString().equalsIgnoreCase(modCategory)).count();
            KamiMod.log.info("<details>");
            KamiMod.log.info("    <summary>" + modCategory + " (" + totalMods + ")</summary>");
            KamiMod.log.info("    <p><ul>");
            collection.forEach(module -> {
                if (module.getCategory().toString().equalsIgnoreCase(modCategory)) {
                    KamiMod.log.info("        <li>" + module.getName() + "<p><i>" + module.getDescription() + "</i></p></li>");
                }
                return;
            });
            KamiMod.log.info("    </ul></p>");
            KamiMod.log.info("</details>");
            return;
        });
        KamiMod.log.info("\n## Commands (" + KamiMod.getInstance().getCommandManager().getCommands().size() + ")\n");
        KamiMod.getInstance().getCommandManager().getCommands().stream().sorted(Comparator.comparing((Function<? super Object, ? extends Comparable>)Command::getLabel)).forEach(command -> {
            KamiMod.log.info("<details>");
            KamiMod.log.info("    <summary>" + command.getLabel() + "</summary>");
            KamiMod.log.info("    <p><ul>");
            KamiMod.log.info("        <li>" + command.getDescription() + "<p><i>Aliases: " + command.getAliases() + "</i></p></li>");
            KamiMod.log.info("    </ul></p>");
            KamiMod.log.info("</details>");
            return;
        });
        MessageSendHelper.sendChatMessage(this.getLabel().substring(0, 1).toUpperCase() + this.getLabel().substring(1) + ": Generated website to log file!");
    }
}

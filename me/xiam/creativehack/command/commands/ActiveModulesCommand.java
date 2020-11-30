// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import java.util.Arrays;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.module.modules.client.ActiveModules;
import java.util.regex.Pattern;
import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import me.xiam.creativehack.command.Command;

public class ActiveModulesCommand extends Command
{
    public ActiveModulesCommand() {
        super("activemodules", new ChunkBuilder().append("r").append("g").append("b").append("category").build(), new String[] { "activemods", "modules" });
        this.setDescription("Allows you to customize ActiveModule's category colours");
    }
    
    @Override
    public void call(final String[] args) {
        if (args[0] == null || args[1] == null || args[2] == null || args[3] == null) {
            MessageSendHelper.sendErrorMessage(this.getChatLabel() + "Missing arguments! Please fill out the command syntax properly");
            return;
        }
        int argPos = 0;
        for (final String arg : args) {
            if (++argPos < 3 && Pattern.compile("[^0-9]").matcher(arg).find()) {
                MessageSendHelper.sendErrorMessage(this.getChatLabel() + "Error: argument '" + arg + "' contains a non-numeric character. You can only set numbers as the RGB");
                return;
            }
        }
        final ActiveModules am = KamiMod.MODULE_MANAGER.getModuleT(ActiveModules.class);
        final String lowerCase = args[3].toLowerCase();
        switch (lowerCase) {
            case "chat": {
                am.chat.setValue(args[0] + "," + args[1] + "," + args[2]);
                MessageSendHelper.sendChatMessage("Set " + am.chat.getName() + " colour to " + args[0] + " " + args[1] + " " + args[2]);
            }
            case "combat": {
                am.combat.setValue(args[0] + "," + args[1] + "," + args[2]);
                MessageSendHelper.sendChatMessage("Set " + am.combat.getName() + " colour to " + args[0] + " " + args[1] + " " + args[2]);
            }
            case "experimental": {
                am.experimental.setValue(args[0] + "," + args[1] + "," + args[2]);
                MessageSendHelper.sendChatMessage("Set " + am.experimental.getName() + " colour to " + args[0] + " " + args[1] + " " + args[2]);
            }
            case "client": {
                am.client.setValue(args[0] + "," + args[1] + "," + args[2]);
                MessageSendHelper.sendChatMessage("Set " + am.client.getName() + " colour to " + args[0] + " " + args[1] + " " + args[2]);
            }
            case "render": {
                am.render.setValue(args[0] + "," + args[1] + "," + args[2]);
                MessageSendHelper.sendChatMessage("Set " + am.render.getName() + " colour to " + args[0] + " " + args[1] + " " + args[2]);
            }
            case "player": {
                am.player.setValue(args[0] + "," + args[1] + "," + args[2]);
                MessageSendHelper.sendChatMessage("Set " + am.player.getName() + " colour to " + args[0] + " " + args[1] + " " + args[2]);
            }
            case "movement": {
                am.movement.setValue(args[0] + "," + args[1] + "," + args[2]);
                MessageSendHelper.sendChatMessage("Set " + am.movement.getName() + " colour to " + args[0] + " " + args[1] + " " + args[2]);
            }
            case "misc": {
                am.misc.setValue(args[0] + "," + args[1] + "," + args[2]);
                MessageSendHelper.sendChatMessage("Set " + am.misc.getName() + " colour to " + args[0] + " " + args[1] + " " + args[2]);
            }
            default: {
                MessageSendHelper.sendErrorMessage("Category '" + args[3] + "' not found! Valid categories: \n" + Arrays.toString(Arrays.stream(Module.Category.values()).filter(Module.Category::isHidden).toArray()));
            }
        }
    }
}

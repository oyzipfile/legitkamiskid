// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.util;

import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.module.modules.client.CommandConfig;
import me.xiam.creativehack.command.Command;

public class CommandUtil
{
    public static void runAliases(final Command command) {
        if (!KamiMod.MODULE_MANAGER.getModuleT(CommandConfig.class).aliasInfo.getValue()) {
            return;
        }
        final int amount = command.getAliases().size();
        if (amount > 0) {
            MessageSendHelper.sendChatMessage("'" + command.getLabel() + "' has " + grammar1(amount) + "alias" + grammar2(amount));
            MessageSendHelper.sendChatMessage(command.getAliases().toString());
        }
    }
    
    private static String grammar1(final int amount) {
        if (amount == 1) {
            return "an ";
        }
        return amount + " ";
    }
    
    private static String grammar2(final int amount) {
        if (amount == 1) {
            return "!";
        }
        return "es!";
    }
}

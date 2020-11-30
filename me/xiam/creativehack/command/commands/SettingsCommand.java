// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import java.util.List;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.ModuleManager;
import me.xiam.creativehack.setting.impl.EnumSetting;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.command.syntax.SyntaxParser;
import me.xiam.creativehack.command.syntax.parsers.ModuleParser;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import me.xiam.creativehack.command.Command;

public class SettingsCommand extends Command
{
    public SettingsCommand() {
        super("settings", new ChunkBuilder().append("module", true, new ModuleParser()).build(), new String[0]);
        this.setDescription("List the possible settings of a command");
    }
    
    @Override
    public void call(final String[] args) {
        if (args[0] == null) {
            MessageSendHelper.sendChatMessage("Please specify a module to display the settings of.");
            return;
        }
        try {
            final Module m = KamiMod.MODULE_MANAGER.getModule(args[0]);
            final List<Setting> settings = m.settingList;
            final String[] result = new String[settings.size()];
            for (int i = 0; i < settings.size(); ++i) {
                final Setting setting = settings.get(i);
                result[i] = "&b" + setting.getName() + "&3(=" + setting.getValue() + ")  &ftype: &3" + setting.getValue().getClass().getSimpleName();
                if (setting instanceof EnumSetting) {
                    final StringBuilder sb = new StringBuilder();
                    final String[] array = result;
                    final int n = i;
                    array[n] = sb.append(array[n]).append("  (").toString();
                    final Enum[] array2;
                    final Enum[] enums = array2 = (Enum[])((EnumSetting)setting).clazz.getEnumConstants();
                    for (final Enum e : array2) {
                        final StringBuilder sb2 = new StringBuilder();
                        final String[] array3 = result;
                        final int n2 = i;
                        array3[n2] = sb2.append(array3[n2]).append(e.name()).append(", ").toString();
                    }
                    result[i] = result[i].substring(0, result[i].length() - 2) + ")";
                }
            }
            MessageSendHelper.sendStringChatMessage(result);
        }
        catch (ModuleManager.ModuleNotFoundException x) {
            MessageSendHelper.sendChatMessage("Couldn't find a module &b" + args[0] + "!");
        }
    }
}

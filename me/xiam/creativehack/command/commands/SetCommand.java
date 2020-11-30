// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import me.xiam.creativehack.setting.ISettingUnknown;
import java.util.Optional;
import me.xiam.creativehack.module.Module;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.command.syntax.SyntaxParser;
import me.xiam.creativehack.command.syntax.parsers.ModuleParser;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import me.xiam.creativehack.command.Command;

public class SetCommand extends Command
{
    public SetCommand() {
        super("set", new ChunkBuilder().append("module", true, new ModuleParser()).append("setting", true).append("value", true).build(), new String[0]);
        this.setDescription("Change the setting of a certain module");
    }
    
    @Override
    public void call(final String[] args) {
        if (args[0] == null) {
            MessageSendHelper.sendChatMessage("Please specify a module!");
            return;
        }
        final Module m = KamiMod.MODULE_MANAGER.getModule(args[0]);
        if (m == null) {
            MessageSendHelper.sendChatMessage("Unknown module &b" + args[0] + "&r!");
            return;
        }
        if (args[1] == null) {
            final String settings = m.settingList.stream().map((Function<? super Object, ?>)Setting::getName).collect((Collector<? super Object, ?, String>)Collectors.joining(", "));
            if (settings.isEmpty()) {
                MessageSendHelper.sendChatMessage("Module &b" + m.getName() + "&r has no settings.");
            }
            else {
                MessageSendHelper.sendStringChatMessage(new String[] { "Please specify a setting! Choose one of the following:", settings });
            }
            return;
        }
        final Optional<Setting> optionalSetting = (Optional<Setting>)m.settingList.stream().filter(setting1 -> setting1.getName().equalsIgnoreCase(args[1])).findFirst();
        if (!optionalSetting.isPresent()) {
            MessageSendHelper.sendChatMessage("Unknown setting &b" + args[1] + "&r in &b" + m.getName() + "&r!");
            return;
        }
        final ISettingUnknown setting2 = optionalSetting.get();
        if (args[2] == null) {
            MessageSendHelper.sendChatMessage("&b" + setting2.getName() + "&r is a &3" + setting2.getValueClass().getSimpleName() + "&r. Its current value is &3" + setting2.getValueAsString());
            return;
        }
        try {
            String arg2 = args[2];
            if (setting2.getClass().getSimpleName().equals("EnumSetting")) {
                arg2 = arg2.toUpperCase();
            }
            setting2.setValueFromString(arg2);
            MessageSendHelper.sendChatMessage("Set &b" + setting2.getName() + "&r to &3" + arg2 + "&r.");
            Module.closeSettings();
        }
        catch (Exception e) {
            e.printStackTrace();
            MessageSendHelper.sendChatMessage("Unable to set value! &6" + e.getMessage());
        }
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import me.xiam.creativehack.setting.builder.SettingBuilder;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.ModuleManager;
import me.xiam.creativehack.util.Wrapper;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.command.syntax.SyntaxParser;
import me.xiam.creativehack.command.syntax.parsers.ModuleParser;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.command.Command;

public class BindCommand extends Command
{
    public static Setting<Boolean> modifiersEnabled;
    
    public BindCommand() {
        super("bind", new ChunkBuilder().append("[module]|modifiers", true, new ModuleParser()).append("[key]|[on|off]", true).build(), new String[0]);
        this.setDescription("Binds a module to a key, or allows you to change modifier options");
    }
    
    @Override
    public void call(final String[] args) {
        if (args.length == 1) {
            MessageSendHelper.sendChatMessage("Please specify a module.");
            return;
        }
        final String module = args[0];
        final String rkey = args[1];
        if (!module.equalsIgnoreCase("modifiers")) {
            try {
                final Module m = KamiMod.MODULE_MANAGER.getModule(module);
                if (rkey == null) {
                    MessageSendHelper.sendChatMessage(m.getName() + " is bound to &b" + m.getBindName());
                    return;
                }
                int key = Wrapper.getKey(rkey);
                if (rkey.equalsIgnoreCase("none")) {
                    key = -1;
                }
                if (key == 0) {
                    MessageSendHelper.sendChatMessage("Unknown key '" + rkey + "'!");
                    return;
                }
                m.getBind().setKey(key);
                MessageSendHelper.sendChatMessage("Bind for &b" + m.getName() + "&r set to &b" + rkey.toUpperCase());
            }
            catch (ModuleManager.ModuleNotFoundException x) {
                MessageSendHelper.sendChatMessage("Unknown module '" + module + "'!");
            }
            return;
        }
        if (rkey == null) {
            MessageSendHelper.sendChatMessage("Expected: on or off");
            return;
        }
        if (rkey.equalsIgnoreCase("on")) {
            BindCommand.modifiersEnabled.setValue(true);
            MessageSendHelper.sendChatMessage("Turned modifiers on.");
        }
        else if (rkey.equalsIgnoreCase("off")) {
            BindCommand.modifiersEnabled.setValue(false);
            MessageSendHelper.sendChatMessage("Turned modifiers off.");
        }
        else {
            MessageSendHelper.sendChatMessage("Expected: on or off");
        }
    }
    
    static {
        BindCommand.modifiersEnabled = SettingBuilder.register(Settings.b("modifiersEnabled", false), "binds");
    }
}

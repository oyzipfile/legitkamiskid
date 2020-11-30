// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.client;

import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "CommandConfig", category = Category.CLIENT, description = "Configures PrefixChat and Alias options", showOnArray = ShowOnArray.OFF)
public class CommandConfig extends Module
{
    public Setting<Boolean> aliasInfo;
    public Setting<Boolean> prefixChat;
    
    public CommandConfig() {
        this.aliasInfo = this.register(Settings.b("Alias Info", true));
        this.prefixChat = this.register(Settings.b("PrefixChat", true));
    }
    
    public void onDisable() {
        this.sendDisableMessage(this.getClass());
    }
    
    private void sendDisableMessage(final Class clazz) {
        MessageSendHelper.sendErrorMessage("Error: The " + KamiMod.MODULE_MANAGER.getModule(clazz).getName() + " module is only for configuring command options, disabling it doesn't do anything.");
        KamiMod.MODULE_MANAGER.getModule(clazz).enable();
    }
}

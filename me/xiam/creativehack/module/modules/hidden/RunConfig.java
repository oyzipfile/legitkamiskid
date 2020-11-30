// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.hidden;

import me.xiam.creativehack.module.modules.client.Tooltips;
import me.xiam.creativehack.module.modules.chat.CustomChat;
import me.xiam.creativehack.module.modules.render.TabFriends;
import me.xiam.creativehack.module.modules.misc.DiscordRPC;
import me.xiam.creativehack.module.modules.capes.Capes;
import me.xiam.creativehack.module.modules.client.InventoryViewer;
import me.xiam.creativehack.module.modules.client.InfoOverlay;
import me.xiam.creativehack.module.modules.client.CommandConfig;
import me.xiam.creativehack.module.modules.client.ActiveModules;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "RunConfig", category = Category.HIDDEN, showOnArray = ShowOnArray.OFF, description = "Default manager for first runs")
public class RunConfig extends Module
{
    private Setting<Boolean> hasRunCapes;
    private Setting<Boolean> hasRunDiscordSettings;
    private Setting<Boolean> hasRunFixGui;
    private Setting<Boolean> hasRunTabFriends;
    private Setting<Boolean> hasRunCustomChat;
    private Setting<Boolean> hasRunTooltips;
    
    public RunConfig() {
        this.hasRunCapes = this.register(Settings.b("Capes", false));
        this.hasRunDiscordSettings = this.register(Settings.b("DiscordRPC", false));
        this.hasRunFixGui = this.register(Settings.b("FixGui", false));
        this.hasRunTabFriends = this.register(Settings.b("TabFriends", false));
        this.hasRunCustomChat = this.register(Settings.b("CustomChat", false));
        this.hasRunTooltips = this.register(Settings.b("Tooltips", false));
    }
    
    public void onEnable() {
        KamiMod.MODULE_MANAGER.getModule(ActiveModules.class).enable();
        KamiMod.MODULE_MANAGER.getModule(CommandConfig.class).enable();
        KamiMod.MODULE_MANAGER.getModule(InfoOverlay.class).enable();
        KamiMod.MODULE_MANAGER.getModule(InventoryViewer.class).enable();
        if (!this.hasRunCapes.getValue()) {
            KamiMod.MODULE_MANAGER.getModule(Capes.class).enable();
            this.hasRunCapes.setValue(true);
        }
        if (!this.hasRunDiscordSettings.getValue()) {
            KamiMod.MODULE_MANAGER.getModule(DiscordRPC.class).enable();
            this.hasRunDiscordSettings.setValue(true);
        }
        if (!this.hasRunFixGui.getValue()) {
            KamiMod.MODULE_MANAGER.getModule(FixGui.class).enable();
            this.hasRunFixGui.setValue(true);
        }
        if (!this.hasRunTabFriends.getValue()) {
            KamiMod.MODULE_MANAGER.getModule(TabFriends.class).enable();
            this.hasRunTabFriends.setValue(true);
        }
        if (!this.hasRunCustomChat.getValue()) {
            KamiMod.MODULE_MANAGER.getModule(CustomChat.class).enable();
            this.hasRunCustomChat.setValue(true);
        }
        if (!this.hasRunTooltips.getValue()) {
            KamiMod.MODULE_MANAGER.getModule(Tooltips.class).enable();
            this.hasRunTooltips.setValue(true);
        }
        this.disable();
    }
}

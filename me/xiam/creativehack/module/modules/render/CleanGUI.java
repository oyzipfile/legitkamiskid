// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.render;

import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "CleanGUI", category = Category.RENDER, showOnArray = ShowOnArray.OFF, description = "Modifies parts of the GUI to be transparent")
public class CleanGUI extends Module
{
    public Setting<Boolean> inventoryGlobal;
    public static Setting<Boolean> chatGlobal;
    private static CleanGUI INSTANCE;
    
    public CleanGUI() {
        this.inventoryGlobal = this.register(Settings.b("Inventory", true));
        (CleanGUI.INSTANCE = this).register(CleanGUI.chatGlobal);
    }
    
    public static boolean enabled() {
        return CleanGUI.INSTANCE.isEnabled();
    }
    
    static {
        CleanGUI.chatGlobal = Settings.b("Chat", false);
        CleanGUI.INSTANCE = new CleanGUI();
    }
}

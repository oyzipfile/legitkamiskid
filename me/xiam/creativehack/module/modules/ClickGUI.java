// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules;

import net.minecraft.client.gui.GuiScreen;
import me.xiam.creativehack.gui.kami.DisplayGuiScreen;
import me.xiam.creativehack.module.Module;

@Info(name = "clickGUI", description = "Opens the Click GUI", category = Category.HIDDEN)
public class ClickGUI extends Module
{
    public ClickGUI() {
        this.getBind().setKey(21);
    }
    
    @Override
    protected void onEnable() {
        if (!(ClickGUI.mc.currentScreen instanceof DisplayGuiScreen)) {
            ClickGUI.mc.displayGuiScreen((GuiScreen)new DisplayGuiScreen(ClickGUI.mc.currentScreen));
        }
        this.disable();
    }
}

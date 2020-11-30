// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.hidden;

import me.xiam.creativehack.util.GuiFrameUtil;
import me.xiam.creativehack.module.Module;

@Info(name = "FixGui", category = Category.HIDDEN, showOnArray = ShowOnArray.OFF, description = "Moves GUI elements back on screen")
public class FixGui extends Module
{
    @Override
    public void onUpdate() {
        if (FixGui.mc.player == null) {
            return;
        }
        GuiFrameUtil.fixFrames(FixGui.mc);
        this.disable();
    }
}

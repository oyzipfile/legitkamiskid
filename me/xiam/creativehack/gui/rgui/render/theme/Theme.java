// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.gui.rgui.render.theme;

import me.xiam.creativehack.gui.rgui.render.font.FontRenderer;
import me.xiam.creativehack.gui.rgui.render.ComponentUI;
import me.xiam.creativehack.gui.rgui.component.Component;

public interface Theme
{
    ComponentUI getUIForComponent(final Component p0);
    
    FontRenderer getFontRenderer();
}

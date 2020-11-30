// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.gui.kami.theme.kami;

import me.xiam.creativehack.gui.rgui.component.Component;
import me.xiam.creativehack.gui.kami.RenderHelper;
import org.lwjgl.opengl.GL11;
import me.xiam.creativehack.gui.rgui.render.font.FontRenderer;
import me.xiam.creativehack.gui.kami.component.SettingsPanel;
import me.xiam.creativehack.gui.rgui.render.AbstractComponentUI;

public class RootSettingsPanelUI extends AbstractComponentUI<SettingsPanel>
{
    @Override
    public void renderComponent(final SettingsPanel component, final FontRenderer fontRenderer) {
        GL11.glColor4f(1.0f, 0.33f, 0.33f, 0.2f);
        RenderHelper.drawOutlinedRoundedRectangle(0, 0, component.getWidth(), component.getHeight(), 6.0f, 0.14f, 0.14f, 0.14f, component.getOpacity(), 1.0f);
    }
}

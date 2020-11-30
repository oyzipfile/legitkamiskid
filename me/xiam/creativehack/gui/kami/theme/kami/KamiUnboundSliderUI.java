// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.gui.kami.theme.kami;

import me.xiam.creativehack.gui.rgui.component.Component;
import me.xiam.creativehack.gui.rgui.component.container.Container;
import org.lwjgl.opengl.GL11;
import me.xiam.creativehack.gui.rgui.render.font.FontRenderer;
import me.xiam.creativehack.gui.kami.component.UnboundSlider;
import me.xiam.creativehack.gui.rgui.render.AbstractComponentUI;

public class KamiUnboundSliderUI extends AbstractComponentUI<UnboundSlider>
{
    @Override
    public void renderComponent(final UnboundSlider component, final FontRenderer fontRenderer) {
        final String s = component.getText() + ": " + component.getValue();
        int c = component.isPressed() ? 11184810 : 14540253;
        if (component.isHovered()) {
            c = (c & 0x7F7F7F) << 1;
        }
        fontRenderer.drawString(component.getWidth() / 2 - fontRenderer.getStringWidth(s) / 2, component.getHeight() - fontRenderer.getFontHeight() / 2 - 4, c, s);
        GL11.glDisable(3042);
    }
    
    @Override
    public void handleAddComponent(final UnboundSlider component, final Container container) {
        component.setHeight(component.getTheme().getFontRenderer().getFontHeight());
        component.setWidth(component.getTheme().getFontRenderer().getStringWidth(component.getText()));
    }
}

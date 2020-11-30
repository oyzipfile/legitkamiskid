// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.gui.kami.theme.kami;

import me.xiam.creativehack.gui.rgui.component.Component;
import java.awt.Color;
import me.xiam.creativehack.gui.kami.KamiGUI;
import org.lwjgl.opengl.GL11;
import me.xiam.creativehack.util.ColourConverter;
import me.xiam.creativehack.gui.rgui.render.font.FontRenderer;
import me.xiam.creativehack.gui.rgui.component.use.CheckButton;
import me.xiam.creativehack.gui.kami.RootSmallFontRenderer;
import me.xiam.creativehack.gui.kami.component.ColorizedCheckButton;

public class RootColorizedCheckButtonUI extends RootCheckButtonUI<ColorizedCheckButton>
{
    RootSmallFontRenderer ff;
    
    public RootColorizedCheckButtonUI() {
        this.ff = new RootSmallFontRenderer();
    }
    
    @Override
    public void renderComponent(final CheckButton component, final FontRenderer aa) {
        GL11.glColor4f(ColourConverter.toF(KamiGuiColors.GuiC.buttonIdleN.color.getRed()), ColourConverter.toF(KamiGuiColors.GuiC.buttonIdleN.color.getGreen()), ColourConverter.toF(KamiGuiColors.GuiC.buttonIdleN.color.getBlue()), component.getOpacity());
        if (component.isHovered() || component.isPressed()) {
            GL11.glColor4f(ColourConverter.toF(KamiGuiColors.GuiC.buttonPressed.color.getRed()), ColourConverter.toF(KamiGuiColors.GuiC.buttonPressed.color.getGreen()), ColourConverter.toF(KamiGuiColors.GuiC.buttonPressed.color.getBlue()), component.getOpacity());
        }
        if (component.isToggled()) {
            GL11.glColor3f(ColourConverter.toF(KamiGuiColors.GuiC.buttonIdleT.color.getRed()), ColourConverter.toF(KamiGuiColors.GuiC.buttonIdleT.color.getGreen()), ColourConverter.toF(KamiGuiColors.GuiC.buttonIdleT.color.getBlue()));
        }
        GL11.glLineWidth(2.5f);
        GL11.glBegin(1);
        GL11.glVertex2d(0.0, (double)component.getHeight());
        GL11.glVertex2d((double)component.getWidth(), (double)component.getHeight());
        GL11.glEnd();
        final Color idleColour = component.isToggled() ? KamiGuiColors.GuiC.buttonIdleT.color : KamiGuiColors.GuiC.buttonIdleN.color;
        final Color downColour = component.isToggled() ? KamiGuiColors.GuiC.buttonHoveredT.color : KamiGuiColors.GuiC.buttonHoveredN.color;
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        this.ff.drawString(component.getWidth() / 2 - KamiGUI.fontRenderer.getStringWidth(component.getName()) / 2, 0, component.isPressed() ? downColour : idleColour, component.getName());
        GL11.glDisable(3553);
    }
}

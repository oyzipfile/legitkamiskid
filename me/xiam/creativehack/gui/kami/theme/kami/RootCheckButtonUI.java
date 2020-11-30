// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.gui.kami.theme.kami;

import java.util.Iterator;
import java.util.List;
import me.xiam.creativehack.gui.rgui.util.ContainerHelper;
import me.xiam.creativehack.gui.kami.component.SettingsPanel;
import me.xiam.creativehack.gui.rgui.component.container.Container;
import me.xiam.creativehack.gui.rgui.component.Component;
import me.xiam.creativehack.gui.kami.RenderHelper;
import org.lwjgl.input.Mouse;
import me.xiam.creativehack.util.Wrapper;
import me.xiam.creativehack.gui.kami.KamiGUI;
import me.xiam.creativehack.gui.kami.DisplayGuiScreen;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.modules.client.Tooltips;
import me.xiam.creativehack.KamiMod;
import org.lwjgl.opengl.GL11;
import me.xiam.creativehack.util.ColourConverter;
import me.xiam.creativehack.gui.rgui.render.font.FontRenderer;
import me.xiam.creativehack.gui.rgui.render.AbstractComponentUI;
import me.xiam.creativehack.gui.rgui.component.use.CheckButton;

public class RootCheckButtonUI<T extends CheckButton> extends AbstractComponentUI<CheckButton>
{
    @Override
    public void renderComponent(final CheckButton component, final FontRenderer ff) {
        GL11.glColor4f(ColourConverter.toF(KamiGuiColors.GuiC.bgColour.color.getRed()), ColourConverter.toF(KamiGuiColors.GuiC.bgColour.color.getGreen()), ColourConverter.toF(KamiGuiColors.GuiC.bgColour.color.getBlue()), component.getOpacity());
        if (component.isToggled()) {
            GL11.glColor3f(ColourConverter.toF(KamiGuiColors.GuiC.bgColour.color.getRed()), ColourConverter.toF(KamiGuiColors.GuiC.bgColour.color.getGreen()), ColourConverter.toF(KamiGuiColors.GuiC.bgColour.color.getBlue()));
        }
        if (component.isHovered() || component.isPressed()) {
            GL11.glColor4f(ColourConverter.toF(KamiGuiColors.GuiC.bgColourHover.color.getRed()), ColourConverter.toF(KamiGuiColors.GuiC.bgColourHover.color.getGreen()), ColourConverter.toF(KamiGuiColors.GuiC.bgColourHover.color.getBlue()), component.getOpacity());
        }
        final String text = component.getName();
        int c = component.isPressed() ? KamiGuiColors.GuiC.buttonPressed.color.getRGB() : (component.isToggled() ? KamiGuiColors.GuiC.buttonIdleT.color.getRGB() : KamiGuiColors.GuiC.buttonHoveredT.color.getRGB());
        if (component.isHovered()) {
            c = (c & KamiGuiColors.GuiC.buttonHoveredN.color.getRGB()) << 1;
            if (component.hasDescription() && !this.isSettingsOpen() && KamiMod.MODULE_MANAGER.isModuleEnabled(Tooltips.class)) {
                final Component componentAt = KamiMod.getInstance().guiManager.getComponentAt(DisplayGuiScreen.mouseX, DisplayGuiScreen.mouseY);
                if (componentAt.getHeight() != 11) {
                    return;
                }
                if (componentAt.getWidth() != component.getWidth()) {
                    return;
                }
                GL11.glDisable(3089);
                GL11.glDepthRange(0.0, 0.01);
                int tooltipX = 14;
                final int tooltipWidth = KamiGUI.fontRenderer.getStringWidth(component.getDescription() + 2);
                final boolean tooBig = Wrapper.getMinecraft().displayWidth < Mouse.getX() + (tooltipWidth * 2 + component.getWidth() * 2);
                if (tooBig) {
                    tooltipX = -tooltipX - tooltipWidth - component.getWidth();
                }
                RenderHelper.drawTooltip(component.getWidth() + tooltipX, 0, tooltipWidth, KamiGUI.fontRenderer.getFontHeight() + 6, 1.5f, 0.17f, 0.17f, 0.18f, 0.9f, ColourConverter.toF(KamiGuiColors.GuiC.windowOutline.color.getRed()), ColourConverter.toF(KamiGuiColors.GuiC.windowOutline.color.getGreen()), ColourConverter.toF(KamiGuiColors.GuiC.windowOutline.color.getBlue()));
                RenderHelper.drawText(component.getWidth() + tooltipX + 3, KamiGUI.fontRenderer.getFontHeight() / 2, ColourConverter.rgbToInt(255, 255, 255), component.getDescription());
                GL11.glEnable(3089);
                GL11.glDepthRange(0.0, 1.0);
            }
        }
        RenderHelper.drawText(component.getWidth() / 2 - KamiGUI.fontRenderer.getStringWidth(text) / 2, KamiGUI.fontRenderer.getFontHeight() / 2 - 2, c, text);
        GL11.glDisable(3042);
    }
    
    @Override
    public void handleAddComponent(final CheckButton component, final Container container) {
        component.setWidth(KamiGUI.fontRenderer.getStringWidth(component.getName()) + 14);
        component.setHeight(KamiGUI.fontRenderer.getFontHeight() + 2);
    }
    
    private boolean isSettingsOpen() {
        final List<SettingsPanel> panels = ContainerHelper.getAllChildren((Class<? extends SettingsPanel>)SettingsPanel.class, (Container)KamiMod.getInstance().getGuiManager());
        for (final SettingsPanel settingsPanel : panels) {
            if (settingsPanel.isVisible()) {
                return true;
            }
        }
        return false;
    }
}

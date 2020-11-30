// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.gui.kami.theme.kami;

import me.xiam.creativehack.gui.rgui.component.Component;
import me.xiam.creativehack.gui.kami.RenderHelper;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.module.modules.experimental.GUIColour;
import org.lwjgl.opengl.GL11;
import me.xiam.creativehack.gui.rgui.render.font.FontRenderer;
import me.xiam.creativehack.gui.kami.component.SettingsPanel;
import me.xiam.creativehack.gui.rgui.render.AbstractComponentUI;

public class KamiSettingsPanelUI extends AbstractComponentUI<SettingsPanel>
{
    @Override
    public void renderComponent(final SettingsPanel component, final FontRenderer fontRenderer) {
        super.renderComponent(component, fontRenderer);
        GL11.glLineWidth(2.0f);
        final float red = KamiMod.MODULE_MANAGER.getModuleT(GUIColour.class).red.getValue() / 255.0f;
        final float green = KamiMod.MODULE_MANAGER.getModuleT(GUIColour.class).green.getValue() / 255.0f;
        final float blue = KamiMod.MODULE_MANAGER.getModuleT(GUIColour.class).blue.getValue() / 255.0f;
        final float alpha = KamiMod.MODULE_MANAGER.getModuleT(GUIColour.class).alpha.getValue() / 255.0f;
        if (KamiMod.MODULE_MANAGER.getModule(GUIColour.class).isEnabled()) {
            GL11.glColor4f(red, green, blue, alpha);
        }
        else {
            GL11.glColor4f(0.17f, 0.17f, 0.18f, 0.9f);
        }
        RenderHelper.drawFilledRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight());
        GL11.glColor3f(0.6f, 0.56f, 1.0f);
        GL11.glLineWidth(1.5f);
        RenderHelper.drawRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight());
    }
}

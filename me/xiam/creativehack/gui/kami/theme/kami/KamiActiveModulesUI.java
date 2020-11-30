// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.gui.kami.theme.kami;

import me.xiam.creativehack.gui.rgui.component.Component;
import java.util.function.Function;
import java.awt.Color;
import me.xiam.creativehack.util.ColourConverter;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.module.Module;
import java.util.List;
import me.xiam.creativehack.util.Wrapper;
import org.lwjgl.opengl.GL11;
import me.xiam.creativehack.gui.rgui.render.font.FontRenderer;
import me.xiam.creativehack.gui.kami.component.ActiveModules;
import me.xiam.creativehack.gui.rgui.render.AbstractComponentUI;

public class KamiActiveModulesUI extends AbstractComponentUI<ActiveModules>
{
    me.xiam.creativehack.module.modules.client.ActiveModules activeMods;
    
    @Override
    public void renderComponent(final ActiveModules component, final FontRenderer f) {
        GL11.glDisable(2884);
        GL11.glEnable(3042);
        GL11.glEnable(3553);
        final FontRenderer renderer = Wrapper.getFontRenderer();
        String string;
        final FontRenderer fontRenderer;
        final StringBuilder sb;
        final List<Module> mods = KamiMod.MODULE_MANAGER.getModules().stream().filter(Module::isEnabled).filter(Module::isOnArray).sorted(Comparator.comparing(module -> {
            new StringBuilder().append(module.getName());
            if (module.getHudInfo() == null) {
                string = "";
            }
            else {
                string = module.getHudInfo() + " ";
            }
            return Integer.valueOf(fontRenderer.getStringWidth(sb.append(string).toString()) * (component.sort_up ? -1 : 1));
        })).collect((Collector<? super Module, ?, List<Module>>)Collectors.toList());
        final int[] y = { 2 };
        this.activeMods = KamiMod.MODULE_MANAGER.getModuleT(me.xiam.creativehack.module.modules.client.ActiveModules.class);
        if (component.getParent().getY() < 26 && Wrapper.getPlayer().getActivePotionEffects().size() > 0 && component.getParent().getOpacity() == 0.0f) {
            y[0] = Math.max(component.getParent().getY(), 26 - component.getParent().getY());
        }
        final float[] hue = { System.currentTimeMillis() % (360 * this.activeMods.getRainbowSpeed()) / (360.0f * this.activeMods.getRainbowSpeed()) };
        Function<Integer, Integer> xFunc = null;
        switch (component.getAlignment()) {
            case RIGHT: {
                xFunc = (Function<Integer, Integer>)(i -> component.getWidth() - i);
                break;
            }
            case CENTER: {
                xFunc = (Function<Integer, Integer>)(i -> component.getWidth() / 2 - i / 2);
                break;
            }
            default: {
                xFunc = (Function<Integer, Integer>)(i -> 0);
                break;
            }
        }
        for (int j = 0; j < mods.size(); ++j) {
            final Module module2 = mods.get(j);
            int rgb = 0;
            switch (this.activeMods.mode.getValue()) {
                case RAINBOW: {
                    rgb = Color.HSBtoRGB(hue[0], ColourConverter.toF(this.activeMods.saturationR.getValue()), ColourConverter.toF(this.activeMods.brightnessR.getValue()));
                    break;
                }
                case CATEGORY: {
                    rgb = this.activeMods.getCategoryColour(module2);
                    break;
                }
                case CUSTOM: {
                    rgb = Color.HSBtoRGB(ColourConverter.toF(this.activeMods.hueC.getValue()), ColourConverter.toF(this.activeMods.saturationC.getValue()), ColourConverter.toF(this.activeMods.brightnessC.getValue()));
                    break;
                }
                case INFO_OVERLAY: {
                    rgb = this.activeMods.getInfoColour(j);
                    break;
                }
                default: {
                    rgb = 0;
                    break;
                }
            }
            final String hudInfo = module2.getHudInfo();
            final String text = this.activeMods.fHax() + module2.getName() + ((hudInfo == null) ? "" : (" §7" + hudInfo));
            final int textWidth = renderer.getStringWidth(text);
            final int textHeight = renderer.getFontHeight() + 1;
            final int red = rgb >> 16 & 0xFF;
            final int green = rgb >> 8 & 0xFF;
            final int blue = rgb & 0xFF;
            renderer.drawStringWithShadow(xFunc.apply(textWidth), y[0], red, green, blue, text);
            final float[] array = hue;
            final int n = 0;
            array[n] += 0.02f;
            final int[] array2 = y;
            final int n2 = 0;
            array2[n2] += textHeight;
        }
        component.setHeight(y[0]);
        GL11.glEnable(2884);
        GL11.glDisable(3042);
    }
    
    @Override
    public void handleSizeComponent(final ActiveModules component) {
        component.setWidth(100);
        component.setHeight(100);
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.util;

import org.lwjgl.opengl.Display;
import me.xiam.creativehack.gui.kami.DisplayGuiScreen;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import java.util.List;
import me.xiam.creativehack.gui.kami.KamiGUI;
import me.xiam.creativehack.gui.rgui.component.container.Container;
import me.xiam.creativehack.gui.rgui.util.ContainerHelper;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.gui.rgui.component.container.use.Frame;

public class GuiFrameUtil
{
    public static Frame getFrameByName(final String name) {
        final KamiGUI kamiGUI = KamiMod.getInstance().getGuiManager();
        if (kamiGUI == null) {
            return null;
        }
        final List<Frame> frames = ContainerHelper.getAllChildren((Class<? extends Frame>)Frame.class, (Container)kamiGUI);
        for (final Frame frame : frames) {
            if (frame.getTitle().equalsIgnoreCase(name)) {
                return frame;
            }
        }
        return null;
    }
    
    public static Frame getFrameByName(final KamiGUI kamiGUI, final String name) {
        if (kamiGUI == null) {
            return null;
        }
        final List<Frame> frames = ContainerHelper.getAllChildren((Class<? extends Frame>)Frame.class, (Container)kamiGUI);
        for (final Frame frame : frames) {
            if (frame.getTitle().equalsIgnoreCase(name)) {
                return frame;
            }
        }
        return null;
    }
    
    public static void fixFrames(final Minecraft mc) {
        final KamiGUI kamiGUI = KamiMod.getInstance().getGuiManager();
        if (kamiGUI == null || mc.player == null) {
            return;
        }
        final List<Frame> frames = ContainerHelper.getAllChildren((Class<? extends Frame>)Frame.class, (Container)kamiGUI);
        for (final Frame frame : frames) {
            final int divider = DisplayGuiScreen.getScale();
            if (frame.getX() > Display.getWidth() / divider) {
                frame.setX(Display.getWidth() / divider - frame.getWidth());
            }
            if (frame.getY() > Display.getHeight() / divider) {
                frame.setY(Display.getHeight() / divider - frame.getHeight());
            }
            if (frame.getX() < 0) {
                frame.setX(0);
            }
            if (frame.getY() < 0) {
                frame.setY(0);
            }
        }
    }
}

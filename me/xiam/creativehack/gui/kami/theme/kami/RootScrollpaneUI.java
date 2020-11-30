// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.gui.kami.theme.kami;

import me.xiam.creativehack.gui.kami.RenderHelper;
import org.lwjgl.opengl.GL11;
import me.xiam.creativehack.util.ColourConverter;
import me.xiam.creativehack.gui.rgui.component.listen.RenderListener;
import me.xiam.creativehack.gui.rgui.component.listen.MouseListener;
import me.xiam.creativehack.gui.rgui.component.container.Container;
import me.xiam.creativehack.gui.rgui.render.font.FontRenderer;
import me.xiam.creativehack.gui.rgui.component.Component;
import me.xiam.creativehack.gui.rgui.component.container.use.Scrollpane;
import me.xiam.creativehack.gui.rgui.render.AbstractComponentUI;

public class RootScrollpaneUI extends AbstractComponentUI<Scrollpane>
{
    long lastScroll;
    Component scrollComponent;
    float barLife;
    boolean dragBar;
    int dY;
    double hovering;
    
    public RootScrollpaneUI() {
        this.lastScroll = 0L;
        this.scrollComponent = null;
        this.barLife = 1220.0f;
        this.dragBar = false;
        this.dY = 0;
        this.hovering = 0.0;
    }
    
    @Override
    public void renderComponent(final Scrollpane component, final FontRenderer fontRenderer) {
    }
    
    @Override
    public void handleAddComponent(final Scrollpane component, final Container container) {
        component.addMouseListener(new MouseListener() {
            @Override
            public void onMouseDown(final MouseButtonEvent event) {
                if (component.canScrollY()) {
                    final double progress = component.getScrolledY() / (double)component.getMaxScrollY();
                    final int barHeight = 30;
                    final int y = (int)((component.getHeight() - barHeight) * progress);
                    if (event.getX() > component.getWidth() - 10 && event.getY() > y && event.getY() < y + barHeight) {
                        RootScrollpaneUI.this.dragBar = true;
                        RootScrollpaneUI.this.dY = event.getY() - y;
                        event.cancel();
                    }
                }
            }
            
            @Override
            public void onMouseRelease(final MouseButtonEvent event) {
                RootScrollpaneUI.this.dragBar = false;
            }
            
            @Override
            public void onMouseDrag(final MouseButtonEvent event) {
                if (RootScrollpaneUI.this.dragBar) {
                    double progress = event.getY() / (double)component.getHeight();
                    progress = Math.max(Math.min(progress, 1.0), 0.0);
                    component.setScrolledY((int)(component.getMaxScrollY() * progress));
                    event.cancel();
                }
            }
            
            @Override
            public void onMouseMove(final MouseMoveEvent event) {
            }
            
            @Override
            public void onScroll(final MouseScrollEvent event) {
                RootScrollpaneUI.this.lastScroll = System.currentTimeMillis();
                RootScrollpaneUI.this.scrollComponent = event.getComponent();
            }
        });
        component.addRenderListener(new RenderListener() {
            @Override
            public void onPreRender() {
            }
            
            @Override
            public void onPostRender() {
                RootScrollpaneUI.this.lastScroll = System.currentTimeMillis();
                if (component.canScrollY()) {
                    float alpha = Math.min(1.0f, (RootScrollpaneUI.this.barLife - (System.currentTimeMillis() - RootScrollpaneUI.this.lastScroll)) / 100.0f) / 3.0f;
                    if (RootScrollpaneUI.this.dragBar) {
                        alpha = 0.4f;
                    }
                    GL11.glColor4f(ColourConverter.toF(KamiGuiColors.GuiC.scrollBar.color.getRed()), ColourConverter.toF(KamiGuiColors.GuiC.scrollBar.color.getGreen()), ColourConverter.toF(KamiGuiColors.GuiC.scrollBar.color.getBlue()), alpha);
                    GL11.glDisable(3553);
                    final int barHeight = 30;
                    final double progress = component.getScrolledY() / (double)component.getMaxScrollY();
                    final int y = (int)((component.getHeight() - barHeight) * progress);
                    RenderHelper.drawRoundedRectangle((float)(component.getWidth() - 6), (float)y, 4.0f, (float)barHeight, 1.0f);
                }
            }
        });
    }
}
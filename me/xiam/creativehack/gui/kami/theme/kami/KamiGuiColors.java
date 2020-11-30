// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.gui.kami.theme.kami;

import java.awt.Color;

public class KamiGuiColors
{
    public enum GuiC
    {
        bgColour(new Color(0, 204, 0)), 
        bgColourHover(new Color(255, 213, 0)), 
        buttonPressed(new Color(0, 42, 255)), 
        buttonIdleN(new Color(255, 213, 0)), 
        buttonHoveredN(new Color(255, 230, 0)), 
        buttonIdleT(new Color(0, 204, 0)), 
        buttonHoveredT(new Color(GuiC.buttonIdleT.color.getRGB()).brighter()), 
        windowOutline(new Color(255, 230, 0)), 
        windowOutlineWidth(1.8f), 
        pinnedWindow(new Color(255, 102, 25)), 
        unpinnedWindow(168.3), 
        lineWindow(112.2), 
        sliderColour(new Color(255, 25, 25)), 
        enumColour(new Color(255, 102, 25)), 
        chatOutline(new Color(255, 102, 25)), 
        scrollBar(new Color(255, 102, 25));
        
        public Color color;
        public float aFloat;
        public double aDouble;
        
        private GuiC(final Color color) {
            this.color = color;
        }
        
        private GuiC(final float aFloat) {
            this.aFloat = aFloat;
        }
        
        private GuiC(final double aDouble) {
            this.aDouble = aDouble;
        }
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.gui.rgui.poof.use;

import me.xiam.creativehack.gui.rgui.poof.PoofInfo;
import me.xiam.creativehack.gui.rgui.component.Component;

public abstract class FramePoof<T extends Component, S extends PoofInfo> extends Poof<T, S>
{
    public static class FramePoofInfo extends PoofInfo
    {
        private Action action;
        
        public FramePoofInfo(final Action action) {
            this.action = action;
        }
        
        public Action getAction() {
            return this.action;
        }
    }
    
    public enum Action
    {
        MINIMIZE, 
        MAXIMIZE, 
        CLOSE;
    }
}

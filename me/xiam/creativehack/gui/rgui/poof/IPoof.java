// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.gui.rgui.poof;

import me.xiam.creativehack.gui.rgui.component.Component;

public interface IPoof<T extends Component, S extends PoofInfo>
{
    void execute(final T p0, final S p1);
    
    Class getComponentClass();
    
    Class getInfoClass();
}

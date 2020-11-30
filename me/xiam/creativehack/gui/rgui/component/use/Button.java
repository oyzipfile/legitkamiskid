// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.gui.rgui.component.use;

import me.xiam.creativehack.gui.rgui.poof.use.Poof;
import me.xiam.creativehack.gui.rgui.poof.PoofInfo;
import me.xiam.creativehack.gui.rgui.poof.IPoof;
import me.xiam.creativehack.gui.rgui.component.listen.MouseListener;
import me.xiam.creativehack.gui.rgui.component.AbstractComponent;

public class Button extends AbstractComponent
{
    private String name;
    private String description;
    
    public Button(final String name, final String description) {
        this(name, description, 0, 0);
        this.addMouseListener(new MouseListener() {
            @Override
            public void onMouseDown(final MouseButtonEvent event) {
                Button.this.callPoof(ButtonPoof.class, new ButtonPoof.ButtonInfo(event.getButton(), event.getX(), event.getY()));
            }
            
            @Override
            public void onMouseRelease(final MouseButtonEvent event) {
            }
            
            @Override
            public void onMouseDrag(final MouseButtonEvent event) {
            }
            
            @Override
            public void onMouseMove(final MouseMoveEvent event) {
            }
            
            @Override
            public void onScroll(final MouseScrollEvent event) {
            }
        });
    }
    
    public Button(final String name, final String description, final int x, final int y) {
        this.name = name;
        this.description = description;
        this.setX(x);
        this.setY(y);
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public boolean hasDescription() {
        return this.description != null;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    @Override
    public void kill() {
    }
    
    public abstract static class ButtonPoof<T extends Button, S extends ButtonInfo> extends Poof<T, S>
    {
        ButtonInfo info;
        
        public static class ButtonInfo extends PoofInfo
        {
            int button;
            int x;
            int y;
            
            public ButtonInfo(final int button, final int x, final int y) {
                this.button = button;
                this.x = x;
                this.y = y;
            }
            
            public int getX() {
                return this.x;
            }
            
            public int getY() {
                return this.y;
            }
            
            public int getButton() {
                return this.button;
            }
        }
    }
}

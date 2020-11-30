// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.event.events;

import net.minecraft.entity.Entity;
import me.xiam.creativehack.event.KamiEvent;

public class EntityUseTotemEvent extends KamiEvent
{
    private Entity entity;
    
    public EntityUseTotemEvent(final Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
}

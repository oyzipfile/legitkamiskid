// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.event.events;

import javax.annotation.Nonnull;
import net.minecraft.entity.Entity;
import me.xiam.creativehack.event.KamiEvent;

public class ClientPlayerAttackEvent extends KamiEvent
{
    private Entity targetEntity;
    
    public ClientPlayerAttackEvent(@Nonnull final Entity targetEntity) {
        if (this.targetEntity == null) {
            throw new IllegalArgumentException("Target Entity cannot be null");
        }
        this.targetEntity = targetEntity;
    }
    
    public Entity getTargetEntity() {
        return this.targetEntity;
    }
}

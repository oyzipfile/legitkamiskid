// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.combat;

import net.minecraft.entity.Entity;
import java.util.function.Predicate;
import me.xiam.creativehack.util.Friends;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.ClientPlayerAttackEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.module.Module;

@Info(name = "AntiFriendHit", description = "Don't hit your friends", category = Category.COMBAT, alwaysListening = true)
public class AntiFriendHit extends Module
{
    @EventHandler
    Listener<ClientPlayerAttackEvent> listener;
    
    public AntiFriendHit() {
        Entity e;
        this.listener = new Listener<ClientPlayerAttackEvent>(event -> {
            if (!this.isDisabled()) {
                e = AntiFriendHit.mc.objectMouseOver.entityHit;
                if (e instanceof EntityOtherPlayerMP && Friends.isFriend(e.getName())) {
                    event.cancel();
                }
            }
        }, (Predicate<ClientPlayerAttackEvent>[])new Predicate[0]);
    }
}

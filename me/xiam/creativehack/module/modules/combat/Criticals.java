// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.combat;

import java.util.function.Predicate;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.module.Module;

@Info(name = "Criticals", category = Category.COMBAT, description = "Always do critical attacks")
public class Criticals extends Module
{
    @EventHandler
    private Listener<AttackEntityEvent> attackEntityEventListener;
    
    public Criticals() {
        this.attackEntityEventListener = new Listener<AttackEntityEvent>(event -> {
            if (!Criticals.mc.player.isInWater() && !Criticals.mc.player.isInLava() && Criticals.mc.player.onGround) {
                Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.10000000149011612, Criticals.mc.player.posZ, false));
                Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                Criticals.mc.player.onCriticalHit(event.getTarget());
            }
        }, (Predicate<AttackEntityEvent>[])new Predicate[0]);
    }
}

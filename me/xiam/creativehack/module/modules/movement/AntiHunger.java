// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.movement;

import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketEntityAction;
import me.xiam.creativehack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AntiHunger", category = Category.MOVEMENT, description = "Reduces hunger lost when moving around")
public class AntiHunger extends Module
{
    private Setting<Boolean> cancelMovementState;
    @EventHandler
    public Listener<PacketEvent.Send> packetListener;
    
    public AntiHunger() {
        this.cancelMovementState = this.register(Settings.b("Cancel Movement State", true));
        CPacketEntityAction packet;
        this.packetListener = new Listener<PacketEvent.Send>(event -> {
            if (AntiHunger.mc.player != null && !AntiHunger.mc.player.isElytraFlying()) {
                if (this.cancelMovementState.getValue() && event.getPacket() instanceof CPacketEntityAction) {
                    packet = (CPacketEntityAction)event.getPacket();
                    if (packet.getAction() == CPacketEntityAction.Action.START_SPRINTING || packet.getAction() == CPacketEntityAction.Action.STOP_SPRINTING) {
                        event.cancel();
                    }
                }
                if (event.getPacket() instanceof CPacketPlayer) {
                    ((CPacketPlayer)event.getPacket()).onGround = (AntiHunger.mc.player.fallDistance > 0.0f || AntiHunger.mc.playerController.isHittingBlock);
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
}

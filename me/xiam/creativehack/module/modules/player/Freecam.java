// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.player;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import java.util.function.Predicate;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.event.events.PacketEvent;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PlayerMoveEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "Freecam", category = Category.PLAYER, description = "Leave your body and trascend into the realm of the gods")
public class Freecam extends Module
{
    private Setting<Integer> speed;
    private Setting<Boolean> packetCancel;
    private double posX;
    private double posY;
    private double posZ;
    private float pitch;
    private float yaw;
    private EntityOtherPlayerMP clonedPlayer;
    private boolean isRidingEntity;
    private Entity ridingEntity;
    @EventHandler
    private Listener<PlayerMoveEvent> moveListener;
    @EventHandler
    private Listener<PlayerSPPushOutOfBlocksEvent> pushListener;
    @EventHandler
    private Listener<PacketEvent.Send> sendListener;
    
    public Freecam() {
        this.speed = this.register(Settings.i("Speed", 5));
        this.packetCancel = this.register(Settings.b("Packet Cancel", false));
        this.moveListener = new Listener<PlayerMoveEvent>(event -> Freecam.mc.player.noClip = true, (Predicate<PlayerMoveEvent>[])new Predicate[0]);
        this.pushListener = new Listener<PlayerSPPushOutOfBlocksEvent>(event -> event.setCanceled(true), (Predicate<PlayerSPPushOutOfBlocksEvent>[])new Predicate[0]);
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketInput) {
                event.cancel();
            }
            if (this.packetCancel.getValue() && (event.getPacket() instanceof CPacketUseEntity || event.getPacket() instanceof CPacketVehicleMove)) {
                event.cancel();
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    @Override
    protected void onEnable() {
        if (Freecam.mc.player != null) {
            this.isRidingEntity = (Freecam.mc.player.getRidingEntity() != null);
            if (Freecam.mc.player.getRidingEntity() == null) {
                this.posX = Freecam.mc.player.posX;
                this.posY = Freecam.mc.player.posY;
                this.posZ = Freecam.mc.player.posZ;
            }
            else {
                this.ridingEntity = Freecam.mc.player.getRidingEntity();
                Freecam.mc.player.dismountRidingEntity();
            }
            this.pitch = Freecam.mc.player.rotationPitch;
            this.yaw = Freecam.mc.player.rotationYaw;
            (this.clonedPlayer = new EntityOtherPlayerMP((World)Freecam.mc.world, Freecam.mc.getSession().getProfile())).copyLocationAndAnglesFrom((Entity)Freecam.mc.player);
            this.clonedPlayer.rotationYawHead = Freecam.mc.player.rotationYawHead;
            Freecam.mc.world.addEntityToWorld(-100, (Entity)this.clonedPlayer);
            Freecam.mc.player.capabilities.isFlying = true;
            Freecam.mc.player.capabilities.setFlySpeed(this.speed.getValue() / 100.0f);
            Freecam.mc.player.noClip = true;
            Freecam.mc.renderChunksMany = false;
            Freecam.mc.renderGlobal.loadRenderers();
        }
    }
    
    @Override
    protected void onDisable() {
        final EntityPlayer localPlayer = (EntityPlayer)Freecam.mc.player;
        if (localPlayer != null) {
            Freecam.mc.player.setPositionAndRotation(this.posX, this.posY, this.posZ, this.yaw, this.pitch);
            Freecam.mc.world.removeEntityFromWorld(-100);
            this.clonedPlayer = null;
            final double posX = 0.0;
            this.posZ = posX;
            this.posY = posX;
            this.posX = posX;
            final float n = 0.0f;
            this.yaw = n;
            this.pitch = n;
            Freecam.mc.player.capabilities.isFlying = false;
            Freecam.mc.player.capabilities.setFlySpeed(0.05f);
            Freecam.mc.player.noClip = false;
            final EntityPlayerSP player = Freecam.mc.player;
            final EntityPlayerSP player2 = Freecam.mc.player;
            final EntityPlayerSP player3 = Freecam.mc.player;
            final double motionX = 0.0;
            player3.motionZ = motionX;
            player2.motionY = motionX;
            player.motionX = motionX;
            if (this.isRidingEntity) {
                Freecam.mc.player.startRiding(this.ridingEntity, true);
            }
            Freecam.mc.renderChunksMany = true;
            Freecam.mc.renderGlobal.loadRenderers();
        }
    }
    
    @Override
    public void onUpdate() {
        Freecam.mc.player.capabilities.isFlying = true;
        Freecam.mc.player.capabilities.setFlySpeed(this.speed.getValue() / 100.0f);
        Freecam.mc.player.noClip = true;
        Freecam.mc.player.onGround = false;
        Freecam.mc.player.fallDistance = 0.0f;
    }
}

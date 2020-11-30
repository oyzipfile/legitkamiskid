// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.EnumSkyBlock;
import me.xiam.creativehack.event.events.WorldCheckLightForEvent;
import net.minecraft.network.Packet;
import java.util.function.Predicate;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import me.xiam.creativehack.setting.Settings;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "NoRender", category = Category.RENDER, description = "Ignore entity spawn packets")
public class NoRender extends Module
{
    private Setting<Boolean> mob;
    private Setting<Boolean> sand;
    private Setting<Boolean> gentity;
    private Setting<Boolean> object;
    private Setting<Boolean> xp;
    private Setting<Boolean> paint;
    private Setting<Boolean> fire;
    private Setting<Boolean> explosion;
    public Setting<Boolean> beacon;
    private Setting<Boolean> skylight;
    @EventHandler
    public Listener<PacketEvent.Receive> receiveListener;
    @EventHandler
    public Listener<RenderBlockOverlayEvent> blockOverlayEventListener;
    
    public NoRender() {
        this.mob = this.register(Settings.b("Mob", false));
        this.sand = this.register(Settings.b("Sand", false));
        this.gentity = this.register(Settings.b("GEntity", false));
        this.object = this.register(Settings.b("Object", false));
        this.xp = this.register(Settings.b("XP", false));
        this.paint = this.register(Settings.b("Paintings", false));
        this.fire = this.register(Settings.b("Fire", true));
        this.explosion = this.register(Settings.b("Explosions", true));
        this.beacon = this.register(Settings.b("Beacon Beams", false));
        this.skylight = this.register(Settings.b("Skylight Updates", false));
        final Packet packet;
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> {
            packet = event.getPacket();
            if ((packet instanceof SPacketSpawnMob && this.mob.getValue()) || (packet instanceof SPacketSpawnGlobalEntity && this.gentity.getValue()) || (packet instanceof SPacketSpawnObject && this.object.getValue()) || (packet instanceof SPacketSpawnExperienceOrb && this.xp.getValue()) || (packet instanceof SPacketSpawnObject && this.sand.getValue()) || (packet instanceof SPacketExplosion && this.explosion.getValue()) || (packet instanceof SPacketSpawnPainting && this.paint.getValue())) {
                event.cancel();
            }
            return;
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        this.blockOverlayEventListener = new Listener<RenderBlockOverlayEvent>(event -> {
            if (this.fire.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) {
                event.setCanceled(true);
            }
        }, (Predicate<RenderBlockOverlayEvent>[])new Predicate[0]);
    }
    
    @SubscribeEvent
    public void onLightingUpdate(final WorldCheckLightForEvent event) {
        if (this.skylight.getValue() && event.getEnumSkyBlock() == EnumSkyBlock.SKY) {
            event.setCanceled(true);
        }
    }
}

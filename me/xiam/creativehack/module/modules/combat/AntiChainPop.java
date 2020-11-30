// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.combat;

import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.module.modules.client.InfoOverlay;
import net.minecraft.init.Items;
import net.minecraft.entity.Entity;
import java.util.function.Predicate;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import me.xiam.creativehack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AntiChainPop", description = "Enables Surround when popping a totem", category = Category.COMBAT)
public class AntiChainPop extends Module
{
    private Setting<Mode> mode;
    private int totems;
    @EventHandler
    public Listener<PacketEvent.Receive> selfPopListener;
    
    public AntiChainPop() {
        this.mode = this.register(Settings.e("Mode", Mode.PACKET));
        this.totems = 0;
        SPacketEntityStatus packet;
        Entity entity;
        this.selfPopListener = new Listener<PacketEvent.Receive>(event -> {
            if (AntiChainPop.mc.player != null && this.mode.getValue().equals(Mode.PACKET)) {
                if (event.getPacket() instanceof SPacketEntityStatus) {
                    packet = (SPacketEntityStatus)event.getPacket();
                    if (packet.getOpCode() == 35) {
                        entity = packet.getEntity((World)AntiChainPop.mc.world);
                        if (entity.getDisplayName().equals(AntiChainPop.mc.player.getDisplayName())) {
                            this.packetMode();
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (AntiChainPop.mc.player == null) {
            return;
        }
        if (this.mode.getValue().equals(Mode.ITEMS)) {
            this.itemMode();
        }
    }
    
    private void itemMode() {
        final int old = this.totems;
        if (InfoOverlay.getItems(Items.TOTEM_OF_UNDYING) < old) {
            final Surround surround = KamiMod.MODULE_MANAGER.getModuleT(Surround.class);
            surround.autoDisable.setValue(true);
            surround.enable();
        }
        this.totems = InfoOverlay.getItems(Items.TOTEM_OF_UNDYING);
    }
    
    private void packetMode() {
        final Surround surround = KamiMod.MODULE_MANAGER.getModuleT(Surround.class);
        surround.autoDisable.setValue(true);
        surround.enable();
    }
    
    public void onEnable() {
        this.totems = 0;
    }
    
    public void onDisable() {
        this.totems = 0;
    }
    
    private enum Mode
    {
        ITEMS, 
        PACKET;
    }
}

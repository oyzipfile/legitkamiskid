// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.misc;

import me.xiam.creativehack.util.MessageSendHelper;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.client.gui.GuiDisconnected;
import me.xiam.creativehack.event.events.ServerConnectedEvent;
import me.xiam.creativehack.KamiMod;
import net.minecraft.client.multiplayer.GuiConnecting;
import java.util.function.Predicate;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.event.events.GuiScreenEvent;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.ServerDisconnectedEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.setting.Setting;
import java.util.Random;
import net.minecraft.client.multiplayer.ServerData;
import me.xiam.creativehack.module.Module;

@Info(name = "AutoFish", category = Category.MISC, description = "Automatically catch fish", alwaysListening = true)
public class AutoFish extends Module
{
    private boolean recastHide;
    private static ServerData cServer;
    Random random;
    private Setting<Boolean> defaultSetting;
    private Setting<Integer> baseDelay;
    private Setting<Integer> extraDelay;
    private Setting<Integer> variation;
    private Setting<Boolean> recast;
    @EventHandler
    public Listener<ServerDisconnectedEvent> disconnectedEventListener;
    @EventHandler
    private Listener<PacketEvent.Receive> receiveListener;
    @EventHandler
    public Listener<GuiScreenEvent.Closed> serverConnectedEvent;
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> serverDisconnectedEvent;
    
    public AutoFish() {
        this.recastHide = false;
        this.defaultSetting = this.register(Settings.b("Defaults", false));
        this.baseDelay = this.register((Setting<Integer>)Settings.integerBuilder("Throw Delay").withValue(450).withMinimum(50).withMaximum(1000).build());
        this.extraDelay = this.register((Setting<Integer>)Settings.integerBuilder("Catch Delay").withValue(300).withMinimum(0).withMaximum(1000).build());
        this.variation = this.register((Setting<Integer>)Settings.integerBuilder("Variation").withValue(50).withMinimum(0).withMaximum(1000).build());
        this.recast = this.register(Settings.booleanBuilder("Recast").withValue(false).withVisibility(v -> this.recastHide).build());
        this.disconnectedEventListener = new Listener<ServerDisconnectedEvent>(event -> {
            if (this.isDisabled()) {
                return;
            }
            else {
                this.recast.setValue(true);
                return;
            }
        }, (Predicate<ServerDisconnectedEvent>[])new Predicate[0]);
        this.receiveListener = new Listener<PacketEvent.Receive>(this::invoke, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        this.serverConnectedEvent = new Listener<GuiScreenEvent.Closed>(event -> {
            if (this.isEnabled() && event.getScreen() instanceof GuiConnecting) {
                AutoFish.cServer = AutoFish.mc.currentServerData;
                KamiMod.EVENT_BUS.post(new ServerConnectedEvent());
            }
            return;
        }, (Predicate<GuiScreenEvent.Closed>[])new Predicate[0]);
        this.serverDisconnectedEvent = new Listener<GuiScreenEvent.Displayed>(event -> {
            if (this.isEnabled() && event.getScreen() instanceof GuiDisconnected && (AutoFish.cServer != null || AutoFish.mc.currentServerData != null)) {
                KamiMod.EVENT_BUS.post(new ServerDisconnectedEvent());
            }
        }, (Predicate<GuiScreenEvent.Displayed>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.defaultSetting.getValue()) {
            this.defaults();
        }
        if (AutoFish.mc.player != null && this.recast.getValue()) {
            AutoFish.mc.rightClickMouse();
            this.recast.setValue(false);
        }
    }
    
    private void invoke(final PacketEvent.Receive e) {
        if (this.isEnabled() && e.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect pck = (SPacketSoundEffect)e.getPacket();
            if (pck.getSound().getSoundName().toString().toLowerCase().contains("entity.bobber.splash")) {
                if (AutoFish.mc.player.fishEntity == null) {
                    return;
                }
                final int soundX = (int)pck.getX();
                final int soundZ = (int)pck.getZ();
                final int fishX = (int)AutoFish.mc.player.fishEntity.posX;
                final int fishZ = (int)AutoFish.mc.player.fishEntity.posZ;
                if (this.kindaEquals(soundX, fishX) && this.kindaEquals(fishZ, soundZ)) {
                    new Thread(() -> {
                        this.random = new Random();
                        try {
                            Thread.sleep(this.extraDelay.getValue() + this.random.ints(1L, -this.variation.getValue(), this.variation.getValue()).findFirst().getAsInt());
                        }
                        catch (InterruptedException ex) {}
                        AutoFish.mc.rightClickMouse();
                        this.random = new Random();
                        try {
                            Thread.sleep(this.baseDelay.getValue() + this.random.ints(1L, -this.variation.getValue(), this.variation.getValue()).findFirst().getAsInt());
                        }
                        catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        AutoFish.mc.rightClickMouse();
                    }).start();
                }
            }
        }
    }
    
    private boolean kindaEquals(final int kara, final int ni) {
        return ni == kara || ni == kara - 1 || ni == kara + 1;
    }
    
    private void defaults() {
        this.baseDelay.setValue(450);
        this.extraDelay.setValue(300);
        this.variation.setValue(50);
        this.defaultSetting.setValue(false);
        MessageSendHelper.sendChatMessage(this.getChatName() + "Set to defaults!");
        closeSettings();
    }
}

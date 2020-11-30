// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.player;

import me.xiam.creativehack.util.InfoCalculator;
import me.xiam.creativehack.gui.rgui.render.font.FontRenderer;
import me.xiam.creativehack.gui.kami.DisplayGuiScreen;
import me.xiam.creativehack.util.Wrapper;
import java.util.function.Predicate;
import me.xiam.creativehack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "LagNotifier", description = "Displays a warning when the server is lagging", category = Category.PLAYER)
public class LagNotifier extends Module
{
    private Setting<Double> timeout;
    private long serverLastUpdated;
    @EventHandler
    private Listener<PacketEvent.Receive> receiveListener;
    
    public LagNotifier() {
        this.timeout = this.register((Setting<Double>)Settings.doubleBuilder().withName("Timeout").withValue(1.0).withMinimum(0.0).withMaximum(10.0).build());
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> this.serverLastUpdated = System.currentTimeMillis(), (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    @Override
    public void onRender() {
        if (this.timeout.getValue() * 1000.0 > System.currentTimeMillis() - this.serverLastUpdated) {
            return;
        }
        final String text = "Server Not Responding! " + this.timeDifference() + "s";
        final FontRenderer renderer = Wrapper.getFontRenderer();
        final int divider = DisplayGuiScreen.getScale();
        renderer.drawStringWithShadow(LagNotifier.mc.displayWidth / divider / 2 - renderer.getStringWidth(text) / 2, LagNotifier.mc.displayHeight / divider / 2 - 217, 255, 85, 85, text);
    }
    
    private double timeDifference() {
        return InfoCalculator.round((System.currentTimeMillis() - this.serverLastUpdated) / 1000.0, 1);
    }
}

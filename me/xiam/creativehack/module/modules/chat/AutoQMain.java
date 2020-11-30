// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.chat;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.xiam.creativehack.util.MessageSendHelper;
import java.util.Date;
import java.text.SimpleDateFormat;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AutoQMain", description = "Automatically does '/queue main' on servers", category = Category.CHAT, showOnArray = ShowOnArray.OFF)
public class AutoQMain extends Module
{
    private Setting<Boolean> showWarns;
    private Setting<Boolean> connectionWarning;
    private Setting<Boolean> dimensionWarning;
    private Setting<Double> delay;
    private double delayTime;
    private double oldDelay;
    
    public AutoQMain() {
        this.showWarns = this.register(Settings.b("Show Warnings", true));
        this.connectionWarning = this.register(Settings.b("Connection Warning", true));
        this.dimensionWarning = this.register(Settings.b("Dimension Warning", true));
        this.delay = this.register((Setting<Double>)Settings.doubleBuilder("Wait time").withMinimum(0.2).withValue(7.1).withMaximum(10.0).build());
        this.oldDelay = 0.0;
    }
    
    @Override
    public void onUpdate() {
        if (AutoQMain.mc.player == null) {
            return;
        }
        if (this.oldDelay == 0.0) {
            this.oldDelay = this.delay.getValue();
        }
        else if (this.oldDelay != this.delay.getValue()) {
            this.delayTime = this.delay.getValue();
            this.oldDelay = this.delay.getValue();
        }
        if (this.delayTime <= 0.0) {
            this.delayTime = (int)(this.delay.getValue() * 2400.0);
        }
        else if (this.delayTime > 0.0) {
            --this.delayTime;
            return;
        }
        if (AutoQMain.mc.getCurrentServerData() == null && this.connectionWarning.getValue()) {
            this.sendMessage("&l&6Error: &r&6You are in singleplayer");
            return;
        }
        if (!AutoQMain.mc.getCurrentServerData().serverIP.equalsIgnoreCase("2b2t.org") && this.connectionWarning.getValue()) {
            this.sendMessage("&l&6Warning: &r&6You are not connected to 2b2t.org");
            return;
        }
        if (AutoQMain.mc.player.dimension != 1 && this.dimensionWarning.getValue()) {
            this.sendMessage("&l&6Warning: &r&6You are not in the end. Not running &b/queue main&7.");
            return;
        }
        this.sendQueueMain();
    }
    
    private void sendQueueMain() {
        final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));
        MessageSendHelper.sendChatMessage("&7Run &b/queue main&7 at " + formatter.format(date));
        AutoQMain.mc.playerController.connection.sendPacket((Packet)new CPacketChatMessage("/queue main"));
    }
    
    private void sendMessage(final String message) {
        if (this.showWarns.getValue()) {
            MessageSendHelper.sendWarningMessage(this.getChatName() + message);
        }
    }
    
    public void onDisable() {
        this.delayTime = 0.0;
    }
}

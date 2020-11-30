// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import me.xiam.creativehack.setting.Settings;
import java.util.Random;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AntiAFK", category = Category.MISC, description = "Prevents being kicked for AFK")
public class AntiAFK extends Module
{
    private Setting<Boolean> swing;
    private Setting<Boolean> turn;
    private Random random;
    
    public AntiAFK() {
        this.swing = this.register(Settings.b("Swing", true));
        this.turn = this.register(Settings.b("Turn", true));
        this.random = new Random();
    }
    
    @Override
    public void onUpdate() {
        if (AntiAFK.mc.playerController.getIsHittingBlock()) {
            return;
        }
        if (AntiAFK.mc.player.ticksExisted % 40 == 0 && this.swing.getValue()) {
            AntiAFK.mc.getConnection().sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        }
        if (AntiAFK.mc.player.ticksExisted % 15 == 0 && this.turn.getValue()) {
            AntiAFK.mc.player.rotationYaw = (float)(this.random.nextInt(360) - 180);
        }
        if (!this.swing.getValue() && !this.turn.getValue() && AntiAFK.mc.player.ticksExisted % 80 == 0) {
            AntiAFK.mc.player.jump();
        }
    }
}

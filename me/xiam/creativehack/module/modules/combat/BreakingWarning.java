// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.combat;

import me.xiam.creativehack.gui.rgui.render.font.FontRenderer;
import me.xiam.creativehack.gui.kami.DisplayGuiScreen;
import me.xiam.creativehack.util.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import java.util.function.Predicate;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import me.xiam.creativehack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "BreakingWarning", category = Category.COMBAT, description = "Notifies you when someone is breaking a block near you.")
public class BreakingWarning extends Module
{
    private Setting<Double> minRange;
    private Setting<Boolean> obsidianOnly;
    private Setting<Boolean> pickaxeOnly;
    private Boolean warn;
    private String playerName;
    private int delay;
    @EventHandler
    private Listener<PacketEvent.Receive> receiveListener;
    
    public BreakingWarning() {
        this.minRange = this.register((Setting<Double>)Settings.doubleBuilder("Min Range").withMinimum(0.0).withValue(1.5).withMaximum(10.0).build());
        this.obsidianOnly = this.register(Settings.b("Obsidian Only", true));
        this.pickaxeOnly = this.register(Settings.b("Pickaxe Only", true));
        this.warn = false;
        SPacketBlockBreakAnim packet;
        int progress;
        int breakerId;
        BlockPos pos;
        Block block;
        EntityPlayer breaker;
        this.receiveListener = new Listener<PacketEvent.Receive>(event -> {
            if (event.getPacket() instanceof SPacketBlockBreakAnim) {
                packet = (SPacketBlockBreakAnim)event.getPacket();
                progress = packet.getProgress();
                breakerId = packet.getBreakerId();
                pos = packet.getPosition();
                block = BreakingWarning.mc.world.getBlockState(pos).getBlock();
                breaker = (EntityPlayer)BreakingWarning.mc.world.getEntityByID(breakerId);
                if (breaker != null) {
                    if (!this.obsidianOnly.getValue() || block.equals(Blocks.OBSIDIAN)) {
                        if (!this.pickaxeOnly.getValue() || (!breaker.itemStackMainHand.isEmpty() && breaker.itemStackMainHand.getItem() instanceof ItemPickaxe)) {
                            if (this.pastDistance((EntityPlayer)BreakingWarning.mc.player, pos, this.minRange.getValue())) {
                                this.playerName = breaker.getName();
                                this.warn = true;
                                this.delay = 0;
                                if (progress == 255) {
                                    this.warn = false;
                                }
                            }
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    @Override
    public void onRender() {
        if (!this.warn) {
            return;
        }
        if (this.delay++ > 100) {
            this.warn = false;
        }
        final String text = this.playerName + " is breaking blocks near you!";
        final FontRenderer renderer = Wrapper.getFontRenderer();
        final int divider = DisplayGuiScreen.getScale();
        renderer.drawStringWithShadow(BreakingWarning.mc.displayWidth / divider / 2 - renderer.getStringWidth(text) / 2, BreakingWarning.mc.displayHeight / divider / 2 - 16, 240, 87, 70, text);
    }
    
    private boolean pastDistance(final EntityPlayer player, final BlockPos pos, final double dist) {
        return player.getDistanceSqToCenter(pos) <= Math.pow(dist, 2.0);
    }
}

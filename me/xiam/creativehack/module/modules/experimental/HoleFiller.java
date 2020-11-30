// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.experimental;

import me.xiam.creativehack.util.EntityUtil;
import java.util.Iterator;
import me.xiam.creativehack.module.modules.player.NoBreakAnimation;
import me.xiam.creativehack.util.BlockInteractionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import me.xiam.creativehack.util.MessageSendHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.init.Blocks;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.module.modules.combat.CrystalAura;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.xiam.creativehack.util.Friends;
import java.util.Collection;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import me.xiam.creativehack.util.Wrapper;
import net.minecraft.network.Packet;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import java.util.ArrayList;
import me.xiam.creativehack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "HoleFiller", category = Category.EXPERIMENTAL, description = "Fills holes around the player to make people easier to crystal.")
public class HoleFiller extends Module
{
    private Setting<Double> distance;
    private Setting<Boolean> render;
    private Setting<Boolean> holeCheck;
    private Setting<Boolean> ignoreWalls;
    public List<BlockPos> blockPosList;
    List<Entity> entities;
    public boolean isHole;
    static boolean isSpoofingAngles;
    static float yaw;
    static float pitch;
    private final BlockPos[] surroundOffset;
    @EventHandler
    private Listener<PacketEvent.Send> cPacketListener;
    
    public HoleFiller() {
        this.distance = this.register(Settings.d("Range", 4.0));
        this.render = this.register(Settings.b("Render Filled Blocks", false));
        this.holeCheck = this.register(Settings.b("Only Fill in Hole", true));
        this.ignoreWalls = this.register(Settings.b("Ignore Walls", false));
        this.entities = new ArrayList<Entity>();
        this.surroundOffset = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
        final Packet packet;
        this.cPacketListener = new Listener<PacketEvent.Send>(event -> {
            packet = event.getPacket();
            if (packet instanceof CPacketPlayer && HoleFiller.isSpoofingAngles) {
                ((CPacketPlayer)packet).yaw = HoleFiller.yaw;
                ((CPacketPlayer)packet).pitch = HoleFiller.pitch;
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock)stack.getItem()).getBlock();
                if (block instanceof BlockObsidian) {
                    slot = i;
                    break;
                }
            }
        }
        return slot;
    }
    
    @Override
    public void onUpdate() {
        if (HoleFiller.mc.player == null || HoleFiller.mc.world == null) {
            return;
        }
        final Vec3d[] holeOffset = { HoleFiller.mc.player.getPositionVector().add(1.0, 0.0, 0.0), HoleFiller.mc.player.getPositionVector().add(-1.0, 0.0, 0.0), HoleFiller.mc.player.getPositionVector().add(0.0, 0.0, 1.0), HoleFiller.mc.player.getPositionVector().add(0.0, 0.0, -1.0), HoleFiller.mc.player.getPositionVector().add(0.0, -1.0, 0.0) };
        this.entities.addAll((Collection<? extends Entity>)HoleFiller.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        final int range = (int)Math.ceil(this.distance.getValue());
        final CrystalAura ca = KamiMod.MODULE_MANAGER.getModuleT(CrystalAura.class);
        this.blockPosList = ca.getSphere(CrystalAura.getPlayerPos(), (float)range, range, false, true, 0);
        for (final Entity p : this.entities) {
            final List<BlockPos> maybe = ca.getSphere(p.getPosition(), (float)range, range, false, true, 0);
            for (final BlockPos pos : maybe) {
                if (this.ignoreWalls.getValue()) {
                    this.blockPosList.add(pos);
                }
                else {
                    if (HoleFiller.mc.world.rayTraceBlocks(new Vec3d((double)HoleFiller.mc.player.getPosition().x, (double)(HoleFiller.mc.player.getPosition().y + p.getEyeHeight()), (double)HoleFiller.mc.player.getPosition().z), new Vec3d((double)pos.x, pos.y + (double)p.getEyeHeight(), (double)pos.z), false, true, false) == null) {
                        continue;
                    }
                    this.blockPosList.add(pos);
                }
            }
        }
        if (this.blockPosList == null) {
            return;
        }
        for (final BlockPos p2 : this.blockPosList) {
            if (p2 == null) {
                return;
            }
            this.isHole = true;
            if (!HoleFiller.mc.world.getBlockState(p2).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!HoleFiller.mc.world.getBlockState(p2.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!HoleFiller.mc.world.getBlockState(p2.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            for (final BlockPos o : this.surroundOffset) {
                final Block block = HoleFiller.mc.world.getBlockState(p2.add((Vec3i)o)).getBlock();
                if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                    this.isHole = false;
                    break;
                }
            }
            int h = 0;
            if (this.holeCheck.getValue()) {
                for (final Vec3d o2 : holeOffset) {
                    final BlockPos q = new BlockPos(o2.x, o2.y, o2.z);
                    final Block b = HoleFiller.mc.world.getBlockState(q).getBlock();
                    if (b == Blocks.OBSIDIAN || b == Blocks.BEDROCK) {
                        ++h;
                    }
                }
                if (h != 5) {
                    return;
                }
            }
            if (!this.isHole) {
                continue;
            }
            if (HoleFiller.mc.player.getPositionVector().squareDistanceTo((double)p2.x, (double)p2.y, (double)p2.z) <= 1.2) {
                return;
            }
            for (final Entity e : this.entities) {
                if (e.getPositionVector().squareDistanceTo((double)p2.x, (double)p2.y, (double)p2.z) <= 1.2) {
                    return;
                }
            }
            final int oldSlot = HoleFiller.mc.player.inventory.currentItem;
            int obiSlot = -1;
            obiSlot = this.findObiInHotbar();
            if (obiSlot == -1) {
                MessageSendHelper.sendChatMessage("&cError: &rNo obsidian in hotbar! disabling.");
                this.disable();
            }
            HoleFiller.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(obiSlot));
            this.lookAtPacket(p2.x, p2.y, p2.z, (EntityPlayer)HoleFiller.mc.player);
            BlockInteractionHelper.placeBlockScaffold(p2);
            if (KamiMod.MODULE_MANAGER.isModuleEnabled(NoBreakAnimation.class)) {
                KamiMod.MODULE_MANAGER.getModuleT(NoBreakAnimation.class).resetMining();
            }
            resetRotation();
            HoleFiller.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(oldSlot));
        }
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1] + 1.0f);
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        HoleFiller.yaw = yaw1;
        HoleFiller.pitch = pitch1;
        HoleFiller.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (HoleFiller.isSpoofingAngles) {
            HoleFiller.yaw = HoleFiller.mc.player.rotationYaw;
            HoleFiller.pitch = HoleFiller.mc.player.rotationPitch;
            HoleFiller.isSpoofingAngles = false;
        }
    }
}

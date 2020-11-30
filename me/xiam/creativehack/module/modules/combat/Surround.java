// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.combat;

import net.minecraft.util.math.MathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import me.xiam.creativehack.module.modules.player.NoBreakAnimation;
import net.minecraft.network.play.client.CPacketAnimation;
import me.xiam.creativehack.util.BlockInteractionHelper;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3i;
import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.module.modules.player.Freecam;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.setting.Settings;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "Surround", category = Category.COMBAT, description = "Surrounds you with obsidian to take less damage")
public class Surround extends Module
{
    public Setting<Boolean> autoDisable;
    private Setting<Boolean> spoofRotations;
    private Setting<Boolean> spoofHotbar;
    private Setting<Double> blockPerTick;
    private Setting<DebugMsgs> debugMsgs;
    private Setting<AutoCenter> autoCenter;
    private Setting<Boolean> placeAnimation;
    private final Vec3d[] surroundTargets;
    private Vec3d playerPos;
    private BlockPos basePos;
    private int offsetStep;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    
    public Surround() {
        this.autoDisable = this.register(Settings.b("Disable on place", true));
        this.spoofRotations = this.register(Settings.b("Spoof Rotations", true));
        this.spoofHotbar = this.register(Settings.b("Spoof Hotbar", false));
        this.blockPerTick = this.register((Setting<Double>)Settings.doubleBuilder("Blocks per Tick").withMinimum(1.0).withValue(4.0).withMaximum(10.0).build());
        this.debugMsgs = this.register(Settings.e("Debug Messages", DebugMsgs.IMPORTANT));
        this.autoCenter = this.register(Settings.e("Auto Center", AutoCenter.TP));
        this.placeAnimation = this.register(Settings.b("Place Animation", false));
        this.surroundTargets = new Vec3d[] { new Vec3d(0.0, 0.0, 0.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, -1.0) };
        this.offsetStep = 0;
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
    }
    
    @Override
    public void onUpdate() {
        if (Surround.mc.player != null && !KamiMod.MODULE_MANAGER.isModuleEnabled(Freecam.class)) {
            if (this.offsetStep == 0) {
                this.basePos = new BlockPos(Surround.mc.player.getPositionVector()).down();
                this.playerHotbarSlot = Surround.mc.player.inventory.currentItem;
                if (this.debugMsgs.getValue().equals(DebugMsgs.ALL)) {
                    MessageSendHelper.sendChatMessage(this.getChatName() + " Starting Loop, current Player Slot: " + this.playerHotbarSlot);
                }
                if (!this.spoofHotbar.getValue()) {
                    this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
                }
            }
            for (int i = 0; i < (int)Math.floor(this.blockPerTick.getValue()); ++i) {
                if (this.debugMsgs.getValue().equals(DebugMsgs.ALL)) {
                    MessageSendHelper.sendChatMessage(this.getChatName() + " Loop iteration: " + this.offsetStep);
                }
                if (this.offsetStep >= this.surroundTargets.length) {
                    this.endLoop();
                    return;
                }
                final Vec3d offset = this.surroundTargets[this.offsetStep];
                this.placeBlock(new BlockPos((Vec3i)this.basePos.add(offset.x, offset.y, offset.z)));
                ++this.offsetStep;
            }
        }
    }
    
    private void centerPlayer(final double x, final double y, final double z) {
        if (this.debugMsgs.getValue().equals(DebugMsgs.ALL) && this.playerPos != null) {
            MessageSendHelper.sendChatMessage(this.getChatName() + " Player position is " + this.playerPos.toString());
        }
        else if (this.debugMsgs.getValue().equals(DebugMsgs.ALL)) {
            MessageSendHelper.sendChatMessage(this.getChatName() + " Player position is null");
        }
        Surround.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y, z, true));
        Surround.mc.player.setPosition(x, y, z);
    }
    
    double getDst(final Vec3d vec) {
        return this.playerPos.distanceTo(vec);
    }
    
    public void onEnable() {
        if (Surround.mc.player == null) {
            return;
        }
        final BlockPos centerPos = Surround.mc.player.getPosition();
        this.playerPos = Surround.mc.player.getPositionVector();
        final double y = centerPos.getY();
        double x = centerPos.getX();
        double z = centerPos.getZ();
        final Vec3d plusPlus = new Vec3d(x + 0.5, y, z + 0.5);
        final Vec3d plusMinus = new Vec3d(x + 0.5, y, z - 0.5);
        final Vec3d minusMinus = new Vec3d(x - 0.5, y, z - 0.5);
        final Vec3d minusPlus = new Vec3d(x - 0.5, y, z + 0.5);
        if (this.autoCenter.getValue().equals(AutoCenter.TP)) {
            if (this.getDst(plusPlus) < this.getDst(plusMinus) && this.getDst(plusPlus) < this.getDst(minusMinus) && this.getDst(plusPlus) < this.getDst(minusPlus)) {
                x = centerPos.getX() + 0.5;
                z = centerPos.getZ() + 0.5;
                this.centerPlayer(x, y, z);
            }
            if (this.getDst(plusMinus) < this.getDst(plusPlus) && this.getDst(plusMinus) < this.getDst(minusMinus) && this.getDst(plusMinus) < this.getDst(minusPlus)) {
                x = centerPos.getX() + 0.5;
                z = centerPos.getZ() - 0.5;
                this.centerPlayer(x, y, z);
            }
            if (this.getDst(minusMinus) < this.getDst(plusPlus) && this.getDst(minusMinus) < this.getDst(plusMinus) && this.getDst(minusMinus) < this.getDst(minusPlus)) {
                x = centerPos.getX() - 0.5;
                z = centerPos.getZ() - 0.5;
                this.centerPlayer(x, y, z);
            }
            if (this.getDst(minusPlus) < this.getDst(plusPlus) && this.getDst(minusPlus) < this.getDst(plusMinus) && this.getDst(minusPlus) < this.getDst(minusMinus)) {
                x = centerPos.getX() - 0.5;
                z = centerPos.getZ() + 0.5;
                this.centerPlayer(x, y, z);
            }
        }
        this.playerHotbarSlot = Surround.mc.player.inventory.currentItem;
        this.lastHotbarSlot = -1;
        if (this.debugMsgs.getValue().equals(DebugMsgs.ALL)) {
            MessageSendHelper.sendChatMessage(this.getChatName() + " Saving initial Slot  = " + this.playerHotbarSlot);
        }
    }
    
    public void onDisable() {
        if (Surround.mc.player != null) {
            if (this.debugMsgs.getValue().equals(DebugMsgs.ALL)) {
                MessageSendHelper.sendChatMessage(this.getChatName() + " Disabling");
            }
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                if (this.spoofHotbar.getValue()) {
                    Surround.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.playerHotbarSlot));
                }
                else {
                    Surround.mc.player.inventory.currentItem = this.playerHotbarSlot;
                }
            }
            this.playerHotbarSlot = -1;
            this.lastHotbarSlot = -1;
        }
    }
    
    private void endLoop() {
        this.offsetStep = 0;
        if (this.debugMsgs.getValue().equals(DebugMsgs.ALL)) {
            MessageSendHelper.sendChatMessage(this.getChatName() + " Ending Loop");
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            if (this.debugMsgs.getValue().equals(DebugMsgs.ALL)) {
                MessageSendHelper.sendChatMessage(this.getChatName() + " Setting Slot back to  = " + this.playerHotbarSlot);
            }
            if (this.spoofHotbar.getValue()) {
                Surround.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.playerHotbarSlot));
            }
            else {
                Surround.mc.player.inventory.currentItem = this.playerHotbarSlot;
            }
            this.lastHotbarSlot = this.playerHotbarSlot;
        }
        if (this.autoDisable.getValue()) {
            this.disable();
        }
    }
    
    private void placeBlock(final BlockPos blockPos) {
        if (!Surround.mc.world.getBlockState(blockPos).getMaterial().isReplaceable()) {
            if (this.debugMsgs.getValue().equals(DebugMsgs.ALL)) {
                MessageSendHelper.sendChatMessage(this.getChatName() + " Block is already placed, skipping");
            }
        }
        else if (!BlockInteractionHelper.checkForNeighbours(blockPos) && this.debugMsgs.getValue().equals(DebugMsgs.ALL)) {
            MessageSendHelper.sendChatMessage(this.getChatName() + " !checkForNeighbours(blockPos), disabling! ");
        }
        else {
            if (this.placeAnimation.getValue()) {
                Surround.mc.player.connection.sendPacket((Packet)new CPacketAnimation(Surround.mc.player.getActiveHand()));
            }
            this.placeBlockExecute(blockPos);
        }
        if (KamiMod.MODULE_MANAGER.isModuleEnabled(NoBreakAnimation.class)) {
            ((NoBreakAnimation)KamiMod.MODULE_MANAGER.getModule(NoBreakAnimation.class)).resetMining();
        }
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = Surround.mc.player.inventory.getStackInSlot(i);
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
    
    public void placeBlockExecute(final BlockPos pos) {
        final Vec3d eyesPos = new Vec3d(Surround.mc.player.posX, Surround.mc.player.posY + Surround.mc.player.getEyeHeight(), Surround.mc.player.posZ);
        final EnumFacing[] values;
        final EnumFacing[] var3 = values = EnumFacing.values();
        for (final EnumFacing side : values) {
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (!canBeClicked(neighbor)) {
                if (this.debugMsgs.getValue().equals(DebugMsgs.ALL)) {
                    MessageSendHelper.sendChatMessage(this.getChatName() + " No neighbor to click at!");
                }
            }
            else {
                final Vec3d hitVec = new Vec3d((Vec3i)neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
                if (eyesPos.squareDistanceTo(hitVec) <= 18.0625) {
                    if (this.spoofRotations.getValue()) {
                        faceVectorPacketInstant(hitVec);
                    }
                    boolean needSneak = false;
                    final Block blockBelow = Surround.mc.world.getBlockState(neighbor).getBlock();
                    if (BlockInteractionHelper.blackList.contains(blockBelow) || BlockInteractionHelper.shulkerList.contains(blockBelow)) {
                        if (this.debugMsgs.getValue().equals(DebugMsgs.IMPORTANT)) {
                            MessageSendHelper.sendChatMessage(this.getChatName() + " Sneak enabled!");
                        }
                        needSneak = true;
                    }
                    if (needSneak) {
                        Surround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Surround.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    }
                    final int obiSlot = this.findObiInHotbar();
                    if (obiSlot == -1) {
                        if (this.debugMsgs.getValue().equals(DebugMsgs.IMPORTANT)) {
                            MessageSendHelper.sendChatMessage(this.getChatName() + " No obsidian in hotbar, disabling!");
                        }
                        this.disable();
                        return;
                    }
                    if (this.lastHotbarSlot != obiSlot) {
                        if (this.debugMsgs.getValue().equals(DebugMsgs.ALL)) {
                            MessageSendHelper.sendChatMessage(this.getChatName() + " Setting Slot to obsidian at  = " + obiSlot);
                        }
                        if (this.spoofHotbar.getValue()) {
                            Surround.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(obiSlot));
                        }
                        else {
                            Surround.mc.player.inventory.currentItem = obiSlot;
                        }
                        this.lastHotbarSlot = obiSlot;
                    }
                    Surround.mc.playerController.processRightClickBlock(Surround.mc.player, Surround.mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
                    Surround.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                    if (needSneak) {
                        if (this.debugMsgs.getValue().equals(DebugMsgs.IMPORTANT)) {
                            MessageSendHelper.sendChatMessage(this.getChatName() + " Sneak disabled!");
                        }
                        Surround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Surround.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    }
                    return;
                }
                else if (this.debugMsgs.getValue().equals(DebugMsgs.ALL)) {
                    MessageSendHelper.sendChatMessage(this.getChatName() + " Distance > 4.25 blocks!");
                }
            }
        }
    }
    
    private static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }
    
    public static Block getBlock(final BlockPos pos) {
        return getState(pos).getBlock();
    }
    
    private static IBlockState getState(final BlockPos pos) {
        return Surround.mc.world.getBlockState(pos);
    }
    
    private static void faceVectorPacketInstant(final Vec3d vec) {
        final float[] rotations = getLegitRotations(vec);
        Surround.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], Surround.mc.player.onGround));
    }
    
    private static float[] getLegitRotations(final Vec3d vec) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { Surround.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - Surround.mc.player.rotationYaw), Surround.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - Surround.mc.player.rotationPitch) };
    }
    
    private static Vec3d getEyesPos() {
        return new Vec3d(Surround.mc.player.posX, Surround.mc.player.posY + Surround.mc.player.getEyeHeight(), Surround.mc.player.posZ);
    }
    
    private enum DebugMsgs
    {
        NONE, 
        IMPORTANT, 
        ALL;
    }
    
    private enum AutoCenter
    {
        OFF, 
        TP;
    }
}

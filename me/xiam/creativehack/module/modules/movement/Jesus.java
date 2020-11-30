// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.movement;

import me.xiam.creativehack.util.Wrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import me.xiam.creativehack.module.modules.player.Freecam;
import me.xiam.creativehack.KamiMod;
import net.minecraft.network.play.client.CPacketPlayer;
import me.xiam.creativehack.event.KamiEvent;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import me.xiam.creativehack.util.EntityUtil;
import net.minecraft.block.BlockLiquid;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.AddCollisionBoxToListEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.util.math.AxisAlignedBB;
import me.xiam.creativehack.module.Module;

@Info(name = "Jesus", description = "Allows you to walk on water", category = Category.MOVEMENT)
public class Jesus extends Module
{
    private static final AxisAlignedBB WATER_WALK_AA;
    @EventHandler
    Listener<AddCollisionBoxToListEvent> addCollisionBoxToListEventListener;
    @EventHandler
    Listener<PacketEvent.Send> packetEventSendListener;
    
    public Jesus() {
        AxisAlignedBB axisalignedbb;
        this.addCollisionBoxToListEventListener = new Listener<AddCollisionBoxToListEvent>(event -> {
            if (Jesus.mc.player != null && event.getBlock() instanceof BlockLiquid && (EntityUtil.isDrivenByPlayer(event.getEntity()) || event.getEntity() == Jesus.mc.player) && !(event.getEntity() instanceof EntityBoat) && !Jesus.mc.player.isSneaking() && Jesus.mc.player.fallDistance < 3.0f && !EntityUtil.isInWater((Entity)Jesus.mc.player) && (EntityUtil.isAboveWater((Entity)Jesus.mc.player, false) || EntityUtil.isAboveWater(Jesus.mc.player.getRidingEntity(), false)) && isAboveBlock((Entity)Jesus.mc.player, event.getPos())) {
                axisalignedbb = Jesus.WATER_WALK_AA.offset(event.getPos());
                if (event.getEntityBox().intersects(axisalignedbb)) {
                    event.getCollidingBoxes().add(axisalignedbb);
                }
                event.cancel();
            }
            return;
        }, (Predicate<AddCollisionBoxToListEvent>[])new Predicate[0]);
        int ticks;
        CPacketPlayer cPacketPlayer;
        this.packetEventSendListener = new Listener<PacketEvent.Send>(event -> {
            if (event.getEra() == KamiEvent.Era.PRE && event.getPacket() instanceof CPacketPlayer && EntityUtil.isAboveWater((Entity)Jesus.mc.player, true) && !EntityUtil.isInWater((Entity)Jesus.mc.player) && !isAboveLand((Entity)Jesus.mc.player)) {
                ticks = Jesus.mc.player.ticksExisted % 2;
                if (ticks == 0) {
                    cPacketPlayer = (CPacketPlayer)event.getPacket();
                    cPacketPlayer.y += 0.02;
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (!KamiMod.MODULE_MANAGER.isModuleEnabled(Freecam.class) && EntityUtil.isInWater((Entity)Jesus.mc.player) && !Jesus.mc.player.isSneaking()) {
            Jesus.mc.player.motionY = 0.1;
            if (Jesus.mc.player.getRidingEntity() != null && !(Jesus.mc.player.getRidingEntity() instanceof EntityBoat)) {
                Jesus.mc.player.getRidingEntity().motionY = 0.3;
            }
        }
    }
    
    private static boolean isAboveLand(final Entity entity) {
        if (entity == null) {
            return false;
        }
        final double y = entity.posY - 0.01;
        for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                if (Wrapper.getWorld().getBlockState(pos).getBlock().isFullBlock(Wrapper.getWorld().getBlockState(pos))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean isAboveBlock(final Entity entity, final BlockPos pos) {
        return entity.posY >= pos.getY();
    }
    
    static {
        WATER_WALK_AA = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.99, 1.0);
    }
}

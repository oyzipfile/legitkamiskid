// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.combat;

import net.minecraft.util.math.MathHelper;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import me.xiam.creativehack.util.Friends;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.SoundCategory;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBow;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.setting.builder.SettingBuilder;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AimBot", description = "Automatically aims at entities for you.", category = Category.COMBAT)
public class AimBot extends Module
{
    private Setting<Integer> range;
    private Setting<Boolean> useBow;
    private Setting<Boolean> ignoreWalls;
    private Setting<Boolean> targetPlayers;
    private Setting<Boolean> targetFriends;
    private Setting<Boolean> targetSleeping;
    private Setting<Boolean> targetMobs;
    private Setting<Boolean> targetHostileMobs;
    private Setting<Boolean> targetPassiveMobs;
    
    public AimBot() {
        this.range = this.register(Settings.integerBuilder("Range").withMinimum(4).withMaximum(24).withValue(16));
        this.useBow = this.register(Settings.booleanBuilder("Use Bow").withValue(true));
        this.ignoreWalls = this.register(Settings.booleanBuilder("Ignore Walls").withValue(true));
        this.targetPlayers = this.register(Settings.booleanBuilder("Target Players").withValue(true));
        this.targetFriends = this.register(Settings.booleanBuilder("Friends").withValue(false).withVisibility(v -> this.targetPlayers.getValue().equals(true)));
        this.targetSleeping = this.register(Settings.booleanBuilder("Sleeping").withValue(false).withVisibility(v -> this.targetPlayers.getValue().equals(true)));
        this.targetMobs = this.register(Settings.booleanBuilder("Target Mobs").withValue(false));
        this.targetHostileMobs = this.register(Settings.booleanBuilder("Hostile").withValue(true).withVisibility(v -> this.targetMobs.getValue().equals(true)));
        this.targetPassiveMobs = this.register(Settings.booleanBuilder("Passive").withValue(false).withVisibility(v -> this.targetMobs.getValue().equals(true)));
    }
    
    @Override
    public void onUpdate() {
        if (KamiMod.MODULE_MANAGER.getModuleT(Aura.class).isEnabled()) {
            return;
        }
        if (this.useBow.getValue()) {
            int bowSlot = -1;
            for (int i = 0; i < 9; ++i) {
                final ItemStack potentialBow = AimBot.mc.player.inventory.getStackInSlot(i);
                if (potentialBow.getItem() instanceof ItemBow) {
                    bowSlot = AimBot.mc.player.inventory.getSlotFor(potentialBow);
                }
            }
            AimBot.mc.player.inventory.currentItem = bowSlot;
            AimBot.mc.playerController.syncCurrentPlayItem();
        }
        for (final Entity entity : AimBot.mc.world.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                final EntityLivingBase potentialTarget = (EntityLivingBase)entity;
                if (potentialTarget instanceof EntityPlayerSP || AimBot.mc.player.getDistance((Entity)potentialTarget) > this.range.getValue() || potentialTarget.getHealth() <= 0.0f) {
                    continue;
                }
                if (!this.ignoreWalls.getValue() && !AimBot.mc.player.canEntityBeSeen((Entity)potentialTarget)) {
                    return;
                }
                if (this.targetMobs.getValue()) {
                    if (this.targetHostileMobs.getValue() && potentialTarget.getSoundCategory().equals((Object)SoundCategory.HOSTILE)) {
                        this.faceEntity((Entity)potentialTarget);
                    }
                    if (this.targetPassiveMobs.getValue() && potentialTarget instanceof EntityAnimal) {
                        this.faceEntity((Entity)potentialTarget);
                    }
                }
                if (!this.targetPlayers.getValue()) {
                    continue;
                }
                if (potentialTarget.isPlayerSleeping() && potentialTarget instanceof EntityPlayer && this.targetSleeping.getValue()) {
                    this.faceEntity((Entity)potentialTarget);
                }
                if (this.targetFriends.getValue()) {
                    continue;
                }
                for (final Friends.Friend friend : Friends.friends.getValue()) {
                    if (!friend.getUsername().equals(potentialTarget.getName())) {
                        this.faceEntity((Entity)potentialTarget);
                    }
                }
            }
        }
    }
    
    private void faceEntity(final Entity entity) {
        final double diffX = entity.posX - AimBot.mc.player.posX;
        final double diffZ = entity.posZ - AimBot.mc.player.posZ;
        final double diffY = AimBot.mc.player.posY + AimBot.mc.player.getEyeHeight() - (entity.posY + entity.getEyeHeight());
        final double xz = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)this.normalizeAngle(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793 - 90.0);
        final float pitch = (float)this.normalizeAngle(-Math.atan2(diffY, xz) * 180.0 / 3.141592653589793);
        AimBot.mc.player.setPositionAndRotation(AimBot.mc.player.posX, AimBot.mc.player.posY, AimBot.mc.player.posZ, yaw, -pitch);
    }
    
    private double normalizeAngle(double angleIn) {
        while (angleIn <= -180.0) {
            angleIn += 360.0;
        }
        while (angleIn > 180.0) {
            angleIn -= 360.0;
        }
        return angleIn;
    }
}

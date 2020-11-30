// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.combat;

import me.xiam.creativehack.module.modules.client.InfoOverlay;
import java.util.Objects;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import me.xiam.creativehack.module.modules.render.Tracers;
import me.xiam.creativehack.util.ColourConverter;
import me.xiam.creativehack.util.KamiTessellator;
import me.xiam.creativehack.event.events.RenderEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.RayTraceResult;
import java.util.Iterator;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import me.xiam.creativehack.util.EntityUtil;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.xiam.creativehack.util.Friends;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.init.Items;
import me.xiam.creativehack.KamiMod;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import me.xiam.creativehack.util.Wrapper;
import net.minecraft.init.MobEffects;
import java.util.Comparator;
import net.minecraft.util.math.Vec3d;
import me.xiam.creativehack.util.MessageSendHelper;
import net.minecraft.network.Packet;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayer;
import me.xiam.creativehack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import java.util.List;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "CrystalAura", category = Category.COMBAT, description = "Places End Crystals to kill enemies")
public class CrystalAura extends Module
{
    private Setting<Boolean> defaultSetting;
    private Setting<Page> p;
    private Setting<ExplodeBehavior> explodeBehavior;
    private Setting<PlaceBehavior> placeBehavior;
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> place;
    private Setting<Boolean> explode;
    private Setting<Boolean> checkAbsorption;
    public Setting<Double> range;
    private Setting<Double> delay;
    private Setting<Integer> hitAttempts;
    private Setting<Double> minDmg;
    private Setting<Boolean> sneakEnable;
    private Setting<Boolean> placePriority;
    private Setting<Boolean> antiWeakness;
    private Setting<Boolean> noToolExplode;
    private Setting<Boolean> players;
    private Setting<Boolean> mobs;
    private Setting<Boolean> animals;
    private Setting<Boolean> tracer;
    private Setting<Boolean> customColours;
    private Setting<Integer> aBlock;
    private Setting<Integer> aTracer;
    private Setting<Integer> r;
    private Setting<Integer> g;
    private Setting<Integer> b;
    private Setting<Boolean> statusMessages;
    private BlockPos render;
    private Entity renderEnt;
    private long systemTime;
    private static boolean togglePitch;
    private boolean switchCoolDown;
    private boolean isAttacking;
    private int oldSlot;
    private static EntityEnderCrystal lastCrystal;
    private static List<EntityEnderCrystal> ignoredCrystals;
    private static int hitTries;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @EventHandler
    private Listener<PacketEvent.Send> cPacketListener;
    private static long startTime;
    
    public CrystalAura() {
        this.defaultSetting = this.register(Settings.b("Defaults", false));
        this.p = this.register((Setting<Page>)Settings.enumBuilder(Page.class).withName("Page").withValue(Page.ONE).build());
        this.explodeBehavior = this.register((Setting<ExplodeBehavior>)Settings.enumBuilder(ExplodeBehavior.class).withName("Explode Behavior").withValue(ExplodeBehavior.ALWAYS).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.placeBehavior = this.register((Setting<PlaceBehavior>)Settings.enumBuilder(PlaceBehavior.class).withName("Place Behavior").withValue(PlaceBehavior.TRADITIONAL).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.autoSwitch = this.register(Settings.booleanBuilder("Auto Switch").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.place = this.register(Settings.booleanBuilder("Place").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.explode = this.register(Settings.booleanBuilder("Explode").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.checkAbsorption = this.register(Settings.booleanBuilder("Check Absorption").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.range = this.register(Settings.doubleBuilder("Range").withMinimum(1.0).withValue(4.0).withMaximum(10.0).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.delay = this.register(Settings.doubleBuilder("Hit Delay").withMinimum(0.0).withValue(5.0).withMaximum(10.0).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.hitAttempts = this.register(Settings.integerBuilder("Hit Attempts").withValue(-1).withMinimum(-1).withMaximum(20).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.minDmg = this.register(Settings.doubleBuilder("Minimum Damage").withMinimum(0.0).withValue(0.0).withMaximum(32.0).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.sneakEnable = this.register(Settings.booleanBuilder("Sneak Surround").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.placePriority = this.register(Settings.booleanBuilder("Prioritize Manual").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.antiWeakness = this.register(Settings.booleanBuilder("Anti Weakness").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.noToolExplode = this.register(Settings.booleanBuilder("No Tool Explode").withValue(true).withVisibility(v -> !this.antiWeakness.getValue() && this.p.getValue().equals(Page.TWO)).build());
        this.players = this.register(Settings.booleanBuilder("Players").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.mobs = this.register(Settings.booleanBuilder("Mobs").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.animals = this.register(Settings.booleanBuilder("Animals").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.tracer = this.register(Settings.booleanBuilder("Tracer").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.customColours = this.register(Settings.booleanBuilder("Custom Colours").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.aBlock = this.register(Settings.integerBuilder("Block Transparency").withMinimum(0).withValue(44).withMaximum(255).withVisibility(v -> this.p.getValue().equals(Page.TWO) && this.customColours.getValue()).build());
        this.aTracer = this.register(Settings.integerBuilder("Tracer Transparency").withMinimum(0).withValue(200).withMaximum(255).withVisibility(v -> this.p.getValue().equals(Page.TWO) && this.customColours.getValue()).build());
        this.r = this.register(Settings.integerBuilder("Red").withMinimum(0).withValue(155).withMaximum(255).withVisibility(v -> this.p.getValue().equals(Page.TWO) && this.customColours.getValue()).build());
        this.g = this.register(Settings.integerBuilder("Green").withMinimum(0).withValue(144).withMaximum(255).withVisibility(v -> this.p.getValue().equals(Page.TWO) && this.customColours.getValue()).build());
        this.b = this.register(Settings.integerBuilder("Blue").withMinimum(0).withValue(255).withMaximum(255).withVisibility(v -> this.p.getValue().equals(Page.TWO) && this.customColours.getValue()).build());
        this.statusMessages = this.register(Settings.booleanBuilder("Status Messages").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.systemTime = -1L;
        this.switchCoolDown = false;
        this.isAttacking = false;
        this.oldSlot = -1;
        final Packet packet;
        this.cPacketListener = new Listener<PacketEvent.Send>(event -> {
            packet = event.getPacket();
            if (packet instanceof CPacketPlayer && CrystalAura.isSpoofingAngles) {
                ((CPacketPlayer)packet).yaw = (float)CrystalAura.yaw;
                ((CPacketPlayer)packet).pitch = (float)CrystalAura.pitch;
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.defaultSetting.getValue()) {
            this.explodeBehavior.setValue(ExplodeBehavior.ALWAYS);
            this.placeBehavior.setValue(PlaceBehavior.TRADITIONAL);
            this.autoSwitch.setValue(true);
            this.place.setValue(false);
            this.explode.setValue(false);
            this.checkAbsorption.setValue(true);
            this.range.setValue(4.0);
            this.delay.setValue(5.0);
            this.hitAttempts.setValue(-1);
            this.minDmg.setValue(0.0);
            this.sneakEnable.setValue(true);
            this.placePriority.setValue(false);
            this.antiWeakness.setValue(false);
            this.noToolExplode.setValue(true);
            this.players.setValue(true);
            this.mobs.setValue(false);
            this.animals.setValue(false);
            this.tracer.setValue(true);
            this.customColours.setValue(true);
            this.aBlock.setValue(44);
            this.aTracer.setValue(200);
            this.r.setValue(155);
            this.g.setValue(144);
            this.b.setValue(255);
            this.statusMessages.setValue(false);
            this.defaultSetting.setValue(false);
            MessageSendHelper.sendChatMessage(this.getChatName() + "Set to defaults!");
            closeSettings();
        }
        if (CrystalAura.mc.player == null) {
            this.resetHitAttempts();
            return;
        }
        this.resetHitAttempts(this.hitAttempts.getValue() == -1);
        int holeBlocks = 0;
        final Vec3d[] holeOffset = { CrystalAura.mc.player.getPositionVector().add(1.0, 0.0, 0.0), CrystalAura.mc.player.getPositionVector().add(-1.0, 0.0, 0.0), CrystalAura.mc.player.getPositionVector().add(0.0, 0.0, 1.0), CrystalAura.mc.player.getPositionVector().add(0.0, 0.0, -1.0), CrystalAura.mc.player.getPositionVector().add(0.0, -1.0, 0.0) };
        final EntityEnderCrystal crystal = (EntityEnderCrystal)CrystalAura.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).filter(entity -> !this.ignored(entity)).map(entity -> entity).min(Comparator.comparing(c -> CrystalAura.mc.player.getDistance(c))).orElse(null);
        if (this.explode.getValue() && crystal != null && CrystalAura.mc.player.getDistance((Entity)crystal) <= this.range.getValue() && this.passSwordCheck()) {
            if (System.nanoTime() / 1000000.0f - this.systemTime >= 25.0 * this.delay.getValue()) {
                if (this.antiWeakness.getValue() && CrystalAura.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    if (!this.isAttacking) {
                        this.oldSlot = Wrapper.getPlayer().inventory.currentItem;
                        this.isAttacking = true;
                    }
                    int newSlot = -1;
                    for (int i = 0; i < 9; ++i) {
                        final ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
                        if (stack != ItemStack.EMPTY) {
                            if (stack.getItem() instanceof ItemSword) {
                                newSlot = i;
                                break;
                            }
                            if (stack.getItem() instanceof ItemTool) {
                                newSlot = i;
                                break;
                            }
                        }
                    }
                    if (newSlot != -1) {
                        Wrapper.getPlayer().inventory.currentItem = newSlot;
                        this.switchCoolDown = true;
                    }
                }
                if (this.placePriority.getValue()) {
                    final boolean wasPlacing = this.place.getValue();
                    if (CrystalAura.mc.gameSettings.keyBindUseItem.isKeyDown() && this.place.getValue()) {
                        this.place.setValue(false);
                        this.explode(crystal);
                    }
                    if (!this.place.getValue() && wasPlacing) {
                        this.place.setValue(true);
                    }
                }
                if (this.explodeBehavior.getValue() == ExplodeBehavior.ALWAYS) {
                    this.explode(crystal);
                }
                for (final Vec3d vecOffset : holeOffset) {
                    final BlockPos offset = new BlockPos(vecOffset.x, vecOffset.y, vecOffset.z);
                    if (CrystalAura.mc.world.getBlockState(offset).getBlock() == Blocks.OBSIDIAN || CrystalAura.mc.world.getBlockState(offset).getBlock() == Blocks.BEDROCK) {
                        ++holeBlocks;
                    }
                }
                if (this.explodeBehavior.getValue() == ExplodeBehavior.HOLE_ONLY && holeBlocks == 5) {
                    this.explode(crystal);
                }
                if (this.explodeBehavior.getValue() == ExplodeBehavior.PREVENT_SUICIDE && ((CrystalAura.mc.player.getPositionVector().distanceTo(crystal.getPositionVector()) <= 0.5 && CrystalAura.mc.player.getPosition().getY() == crystal.getPosition().getY()) || (CrystalAura.mc.player.getPositionVector().distanceTo(crystal.getPositionVector()) >= 2.3 && CrystalAura.mc.player.getPosition().getY() == crystal.getPosition().getY()) || (CrystalAura.mc.player.getPositionVector().distanceTo(crystal.getPositionVector()) >= 0.5 && CrystalAura.mc.player.getPosition().getY() != crystal.getPosition().getY()))) {
                    this.explode(crystal);
                }
                if (this.explodeBehavior.getValue() == ExplodeBehavior.LEFT_CLICK_ONLY && CrystalAura.mc.gameSettings.keyBindAttack.isKeyDown()) {
                    this.explode(crystal);
                }
                if (this.sneakEnable.getValue() && CrystalAura.mc.player.isSneaking() && holeBlocks != 5) {
                    KamiMod.MODULE_MANAGER.getModule(Surround.class).enable();
                }
                return;
            }
        }
        else {
            resetRotation();
            if (this.oldSlot != -1) {
                Wrapper.getPlayer().inventory.currentItem = this.oldSlot;
                this.oldSlot = -1;
            }
            this.isAttacking = false;
        }
        int crystalSlot = (CrystalAura.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? CrystalAura.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (CrystalAura.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (CrystalAura.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        }
        else if (crystalSlot == -1) {
            return;
        }
        final List<BlockPos> blocks = this.findCrystalBlocks();
        final List<Entity> entities = new ArrayList<Entity>();
        if (this.players.getValue()) {
            entities.addAll((Collection<? extends Entity>)CrystalAura.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        }
        final boolean b2;
        entities.addAll((Collection<? extends Entity>)CrystalAura.mc.world.loadedEntityList.stream().filter(entity -> {
            if (EntityUtil.isLiving(entity)) {
                if (EntityUtil.isPassive(entity) ? this.animals.getValue() : this.mobs.getValue()) {
                    return b2;
                }
            }
            return b2;
        }).collect(Collectors.toList()));
        BlockPos q = null;
        double damage = 0.5;
        for (final Entity entity2 : entities) {
            if (entity2 != CrystalAura.mc.player && ((EntityLivingBase)entity2).getHealth() > 0.0f) {
                if (this.placeBehavior.getValue() == PlaceBehavior.MULTI) {
                    continue;
                }
                for (final BlockPos blockPos : blocks) {
                    final double b = entity2.getDistanceSq(blockPos);
                    if (b >= 169.0) {
                        continue;
                    }
                    final double d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2);
                    if (d <= damage) {
                        continue;
                    }
                    final double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)CrystalAura.mc.player);
                    float enemyAbsorption = 0.0f;
                    float playerAbsorption = 0.0f;
                    if (this.checkAbsorption.getValue()) {
                        enemyAbsorption = ((EntityLivingBase)entity2).getAbsorptionAmount();
                        playerAbsorption = CrystalAura.mc.player.getAbsorptionAmount();
                    }
                    if (self > d && d >= ((EntityLivingBase)entity2).getHealth() + enemyAbsorption) {
                        continue;
                    }
                    if (self - 0.5 > CrystalAura.mc.player.getHealth() + playerAbsorption) {
                        continue;
                    }
                    damage = d;
                    q = blockPos;
                    this.renderEnt = entity2;
                }
            }
        }
        if (this.place.getValue() && this.placeBehavior.getValue() == PlaceBehavior.MULTI) {
            for (final Entity entity2 : entities) {
                if (entity2 != CrystalAura.mc.player) {
                    if (((EntityLivingBase)entity2).getHealth() <= 0.0f) {
                        continue;
                    }
                    for (final BlockPos blockPos : blocks) {
                        final double b = entity2.getDistanceSq(blockPos);
                        if (b > 75.0) {
                            continue;
                        }
                        final double d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2);
                        final double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)CrystalAura.mc.player);
                        if (self >= CrystalAura.mc.player.getHealth() + CrystalAura.mc.player.getAbsorptionAmount()) {
                            continue;
                        }
                        if (self > d) {
                            continue;
                        }
                        if ((b >= 10.0 || d < 15.0) && d < ((EntityLivingBase)entity2).getHealth() + ((EntityLivingBase)entity2).getAbsorptionAmount() && (6.0f < ((EntityLivingBase)entity2).getHealth() + ((EntityLivingBase)entity2).getAbsorptionAmount() || b >= 4.0) && (b >= 9.0 || d < this.minDmg.getValue() || this.minDmg.getValue() <= 0.0)) {
                            continue;
                        }
                        q = blockPos;
                        damage = d;
                        this.renderEnt = entity2;
                    }
                }
            }
        }
        if (damage == 0.5) {
            this.render = null;
            this.renderEnt = null;
            resetRotation();
            return;
        }
        if (this.place.getValue()) {
            this.render = q;
            if (!offhand && CrystalAura.mc.player.inventory.currentItem != crystalSlot) {
                if (this.autoSwitch.getValue()) {
                    CrystalAura.mc.player.inventory.currentItem = crystalSlot;
                    resetRotation();
                    this.switchCoolDown = true;
                }
                return;
            }
            this.lookAtPacket(q.x + 0.5, q.y - 0.5, q.z + 0.5, (EntityPlayer)CrystalAura.mc.player);
            final RayTraceResult result = CrystalAura.mc.world.rayTraceBlocks(new Vec3d(CrystalAura.mc.player.posX, CrystalAura.mc.player.posY + CrystalAura.mc.player.getEyeHeight(), CrystalAura.mc.player.posZ), new Vec3d(q.x + 0.5, q.y - 0.5, q.z + 0.5));
            EnumFacing f;
            if (result == null || result.sideHit == null) {
                f = EnumFacing.UP;
            }
            else {
                f = result.sideHit;
            }
            if (this.switchCoolDown) {
                this.switchCoolDown = false;
                return;
            }
            CrystalAura.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        }
        if (CrystalAura.isSpoofingAngles) {
            if (CrystalAura.togglePitch) {
                final EntityPlayerSP player = CrystalAura.mc.player;
                player.rotationPitch += (float)4.0E-4;
                CrystalAura.togglePitch = false;
            }
            else {
                final EntityPlayerSP player2 = CrystalAura.mc.player;
                player2.rotationPitch -= (float)4.0E-4;
                CrystalAura.togglePitch = true;
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null) {
            KamiTessellator.prepare(7);
            int colour = 1157627903;
            if (this.customColours.getValue()) {
                colour = ColourConverter.rgbToInt(this.r.getValue(), this.g.getValue(), this.b.getValue(), this.aBlock.getValue());
            }
            KamiTessellator.drawBox(this.render, colour, 63);
            KamiTessellator.release();
            if (this.renderEnt != null && this.tracer.getValue()) {
                final Vec3d p = EntityUtil.getInterpolatedRenderPos(this.renderEnt, CrystalAura.mc.getRenderPartialTicks());
                float rL = 1.0f;
                float gL = 1.0f;
                float bL = 1.0f;
                float aL = 1.0f;
                if (this.customColours.getValue()) {
                    rL = ColourConverter.toF(this.r.getValue());
                    gL = ColourConverter.toF(this.g.getValue());
                    bL = ColourConverter.toF(this.b.getValue());
                    aL = ColourConverter.toF(this.aTracer.getValue());
                }
                Tracers.drawLineFromPosToPos(this.render.x - CrystalAura.mc.getRenderManager().renderPosX + 0.5, this.render.y - CrystalAura.mc.getRenderManager().renderPosY + 1.0, this.render.z - CrystalAura.mc.getRenderManager().renderPosZ + 0.5, p.x, p.y, p.z, this.renderEnt.getEyeHeight(), rL, gL, bL, aL);
            }
        }
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1] + 1.0f);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (CrystalAura.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || CrystalAura.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && CrystalAura.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && CrystalAura.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && CrystalAura.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && CrystalAura.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(CrystalAura.mc.player.posX), Math.floor(CrystalAura.mc.player.posY), Math.floor(CrystalAura.mc.player.posZ));
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    public List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
    
    public static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedSize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedSize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finalD = 1.0;
        if (entity instanceof EntityLivingBase) {
            finalD = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)CrystalAura.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finalD;
    }
    
    public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            final int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionById(11)))) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage - ep.getAbsorptionAmount(), 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }
    
    private static float getDamageMultiplied(final float damage) {
        final int diff = CrystalAura.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        CrystalAura.yaw = yaw1;
        CrystalAura.pitch = pitch1;
        CrystalAura.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (CrystalAura.isSpoofingAngles) {
            CrystalAura.yaw = CrystalAura.mc.player.rotationYaw;
            CrystalAura.pitch = CrystalAura.mc.player.rotationPitch;
            CrystalAura.isSpoofingAngles = false;
        }
    }
    
    public void onEnable() {
        this.sendMessage("&aENABLED&r");
    }
    
    public void onDisable() {
        this.sendMessage("&cDISABLED&r");
        this.render = null;
        this.renderEnt = null;
        resetRotation();
    }
    
    public void explode(final EntityEnderCrystal crystal) {
        if (crystal == null) {
            return;
        }
        if (CrystalAura.lastCrystal == crystal) {
            ++CrystalAura.hitTries;
        }
        else {
            CrystalAura.lastCrystal = crystal;
            CrystalAura.hitTries = 0;
        }
        try {
            if (this.hitAttempts.getValue() != -1 && CrystalAura.hitTries > this.hitAttempts.getValue()) {
                CrystalAura.ignoredCrystals.add(crystal);
                CrystalAura.hitTries = 0;
            }
            else {
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)CrystalAura.mc.player);
                CrystalAura.mc.playerController.attackEntity((EntityPlayer)CrystalAura.mc.player, (Entity)crystal);
                CrystalAura.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
        catch (Throwable t) {}
        this.systemTime = System.nanoTime() / 1000000L;
    }
    
    private boolean passSwordCheck() {
        return !this.noToolExplode.getValue() || this.antiWeakness.getValue() || !this.noToolExplode.getValue() || (!(CrystalAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemTool) && !(CrystalAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword));
    }
    
    @Override
    public String getHudInfo() {
        return String.valueOf(InfoOverlay.getItems(Items.END_CRYSTAL));
    }
    
    private boolean ignored(final Entity e) {
        if (CrystalAura.ignoredCrystals == null) {
            return false;
        }
        for (final EntityEnderCrystal c : CrystalAura.ignoredCrystals) {
            if (e != null && e == c) {
                return true;
            }
        }
        return false;
    }
    
    private void resetHitAttempts() {
        this.sendMessage("&l&9Reset&r&9 hit attempts crystals");
        CrystalAura.lastCrystal = null;
        CrystalAura.ignoredCrystals = null;
        CrystalAura.hitTries = 0;
    }
    
    private void resetHitAttempts(final boolean should) {
        if (should) {
            CrystalAura.lastCrystal = null;
            CrystalAura.ignoredCrystals = null;
            CrystalAura.hitTries = 0;
        }
        else if (this.resetTime()) {
            this.sendMessage("&l&9Reset&r&9 hit attempts crystals");
            CrystalAura.lastCrystal = null;
            CrystalAura.ignoredCrystals = null;
            CrystalAura.hitTries = 0;
        }
    }
    
    private boolean resetTime() {
        if (CrystalAura.startTime == 0L) {
            CrystalAura.startTime = System.currentTimeMillis();
        }
        if (CrystalAura.startTime + 900000L <= System.currentTimeMillis()) {
            CrystalAura.startTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
    
    private void sendMessage(final String message) {
        if (this.statusMessages.getValue()) {
            MessageSendHelper.sendChatMessage(this.getChatName() + message);
        }
    }
    
    static {
        CrystalAura.togglePitch = false;
        CrystalAura.ignoredCrystals = new ArrayList<EntityEnderCrystal>();
        CrystalAura.hitTries = 0;
        CrystalAura.startTime = 0L;
    }
    
    private enum ExplodeBehavior
    {
        HOLE_ONLY, 
        PREVENT_SUICIDE, 
        LEFT_CLICK_ONLY, 
        ALWAYS;
    }
    
    private enum PlaceBehavior
    {
        MULTI, 
        TRADITIONAL;
    }
    
    private enum Page
    {
        ONE, 
        TWO;
    }
}

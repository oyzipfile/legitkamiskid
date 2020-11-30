// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.render;

import java.awt.Color;
import me.xiam.creativehack.util.KamiTessellator;
import me.xiam.creativehack.event.events.RenderEvent;
import net.minecraft.block.Block;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.Vec3i;
import net.minecraft.init.Blocks;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.module.modules.combat.CrystalAura;
import me.xiam.creativehack.setting.Settings;
import java.util.concurrent.ConcurrentHashMap;
import me.xiam.creativehack.setting.Setting;
import net.minecraft.util.math.BlockPos;
import me.xiam.creativehack.module.Module;

@Info(name = "HoleESP", category = Category.RENDER, description = "Show safe holes for crystal pvp")
public class HoleESP extends Module
{
    private final BlockPos[] surroundOffset;
    private Setting<Double> renderDistance;
    private Setting<Integer> a0;
    private Setting<Integer> r1;
    private Setting<Integer> g1;
    private Setting<Integer> b1;
    private Setting<Integer> r2;
    private Setting<Integer> g2;
    private Setting<Integer> b2;
    private Setting<RenderMode> renderModeSetting;
    private Setting<RenderBlocks> renderBlocksSetting;
    private ConcurrentHashMap<BlockPos, Boolean> safeHoles;
    
    public HoleESP() {
        this.surroundOffset = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
        this.renderDistance = this.register(Settings.d("Render Distance", 8.0));
        this.a0 = this.register((Setting<Integer>)Settings.integerBuilder("Transparency").withMinimum(0).withValue(32).withMaximum(255).build());
        this.r1 = this.register(Settings.integerBuilder("Red (Obby)").withMinimum(0).withValue(208).withMaximum(255).withVisibility(v -> this.obbySettings()).build());
        this.g1 = this.register(Settings.integerBuilder("Green (Obby)").withMinimum(0).withValue(144).withMaximum(255).withVisibility(v -> this.obbySettings()).build());
        this.b1 = this.register(Settings.integerBuilder("Blue (Obby)").withMinimum(0).withValue(255).withMaximum(255).withVisibility(v -> this.obbySettings()).build());
        this.r2 = this.register(Settings.integerBuilder("Red (Bedrock)").withMinimum(0).withValue(144).withMaximum(255).withVisibility(v -> this.bedrockSettings()).build());
        this.g2 = this.register(Settings.integerBuilder("Green (Bedrock)").withMinimum(0).withValue(144).withMaximum(255).withVisibility(v -> this.bedrockSettings()).build());
        this.b2 = this.register(Settings.integerBuilder("Blue (Bedrock)").withMinimum(0).withValue(255).withMaximum(255).withVisibility(v -> this.bedrockSettings()).build());
        this.renderModeSetting = this.register(Settings.e("Render Mode", RenderMode.BLOCK));
        this.renderBlocksSetting = this.register(Settings.e("Render", RenderBlocks.BOTH));
    }
    
    private boolean obbySettings() {
        return this.renderBlocksSetting.getValue().equals(RenderBlocks.OBBY) || this.renderBlocksSetting.getValue().equals(RenderBlocks.BOTH);
    }
    
    private boolean bedrockSettings() {
        return this.renderBlocksSetting.getValue().equals(RenderBlocks.BEDROCK) || this.renderBlocksSetting.getValue().equals(RenderBlocks.BOTH);
    }
    
    @Override
    public void onUpdate() {
        if (this.safeHoles == null) {
            this.safeHoles = new ConcurrentHashMap<BlockPos, Boolean>();
        }
        else {
            this.safeHoles.clear();
        }
        final int range = (int)Math.ceil(this.renderDistance.getValue());
        final CrystalAura crystalAura = KamiMod.MODULE_MANAGER.getModuleT(CrystalAura.class);
        final List<BlockPos> blockPosList = crystalAura.getSphere(CrystalAura.getPlayerPos(), (float)range, range, false, true, 0);
        for (final BlockPos pos : blockPosList) {
            if (!HoleESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            boolean isSafe = true;
            boolean isBedrock = true;
            for (final BlockPos offset : this.surroundOffset) {
                final Block block = HoleESP.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
                if (block != Blocks.BEDROCK) {
                    isBedrock = false;
                }
                if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                    isSafe = false;
                    break;
                }
            }
            if (!isSafe) {
                continue;
            }
            this.safeHoles.put(pos, isBedrock);
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (HoleESP.mc.player == null || this.safeHoles == null) {
            return;
        }
        if (this.safeHoles.isEmpty()) {
            return;
        }
        KamiTessellator.prepare(7);
        this.safeHoles.forEach((blockPos, isBedrock) -> {
            switch (this.renderBlocksSetting.getValue()) {
                case BOTH: {
                    if (isBedrock) {
                        this.drawBox(blockPos, this.r2.getValue(), this.g2.getValue(), this.b2.getValue());
                        break;
                    }
                    else {
                        this.drawBox(blockPos, this.r1.getValue(), this.g1.getValue(), this.b1.getValue());
                        break;
                    }
                    break;
                }
                case OBBY: {
                    if (!isBedrock) {
                        this.drawBox(blockPos, this.r1.getValue(), this.g1.getValue(), this.b1.getValue());
                        break;
                    }
                    else {
                        break;
                    }
                    break;
                }
                case BEDROCK: {
                    if (isBedrock) {
                        this.drawBox(blockPos, this.r2.getValue(), this.g2.getValue(), this.b2.getValue());
                        break;
                    }
                    else {
                        break;
                    }
                    break;
                }
            }
            return;
        });
        KamiTessellator.release();
    }
    
    private void drawBox(final BlockPos blockPos, final int r, final int g, final int b) {
        final Color color = new Color(r, g, b, this.a0.getValue());
        if (this.renderModeSetting.getValue().equals(RenderMode.DOWN)) {
            KamiTessellator.drawBox(blockPos, color.getRGB(), 1);
        }
        else if (this.renderModeSetting.getValue().equals(RenderMode.BLOCK)) {
            KamiTessellator.drawBox(blockPos, color.getRGB(), 63);
        }
    }
    
    private enum RenderMode
    {
        DOWN, 
        BLOCK;
    }
    
    private enum RenderBlocks
    {
        OBBY, 
        BEDROCK, 
        BOTH;
    }
}

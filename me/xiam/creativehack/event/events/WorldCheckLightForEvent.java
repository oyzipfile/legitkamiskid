// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.event.events;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class WorldCheckLightForEvent extends Event
{
    private final EnumSkyBlock enumSkyBlock;
    private final BlockPos pos;
    
    public WorldCheckLightForEvent(final EnumSkyBlock enumSkyBlock, final BlockPos pos) {
        this.enumSkyBlock = enumSkyBlock;
        this.pos = pos;
    }
    
    public EnumSkyBlock getEnumSkyBlock() {
        return this.enumSkyBlock;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
}

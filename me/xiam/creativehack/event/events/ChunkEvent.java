// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.event.events;

import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.world.chunk.Chunk;
import me.xiam.creativehack.event.KamiEvent;

public class ChunkEvent extends KamiEvent
{
    private Chunk chunk;
    private SPacketChunkData packet;
    
    public ChunkEvent(final Chunk chunk, final SPacketChunkData packet) {
        this.chunk = chunk;
        this.packet = packet;
    }
    
    public Chunk getChunk() {
        return this.chunk;
    }
    
    public SPacketChunkData getPacket() {
        return this.packet;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.syntax.parsers;

import java.util.Map;
import java.util.TreeMap;
import me.xiam.creativehack.command.syntax.SyntaxChunk;
import java.util.Iterator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.block.Block;
import java.util.HashMap;

public class BlockParser extends AbstractParser
{
    private static HashMap<String, Block> blockNames;
    
    public BlockParser() {
        if (!BlockParser.blockNames.isEmpty()) {
            return;
        }
        for (final ResourceLocation resourceLocation : Block.REGISTRY.getKeys()) {
            BlockParser.blockNames.put(resourceLocation.toString().replace("minecraft:", "").replace("_", ""), (Block)Block.REGISTRY.getObject((Object)resourceLocation));
        }
    }
    
    @Override
    public String getChunk(final SyntaxChunk[] chunks, final SyntaxChunk thisChunk, final String[] values, final String chunkValue) {
        try {
            if (chunkValue == null) {
                return (thisChunk.isHeadless() ? "" : thisChunk.getHead()) + (thisChunk.isNecessary() ? "<" : "[") + thisChunk.getType() + (thisChunk.isNecessary() ? ">" : "]");
            }
            final HashMap<String, Block> possibilities = new HashMap<String, Block>();
            for (final String s : BlockParser.blockNames.keySet()) {
                if (s.toLowerCase().startsWith(chunkValue.toLowerCase().replace("minecraft:", "").replace("_", ""))) {
                    possibilities.put(s, BlockParser.blockNames.get(s));
                }
            }
            if (possibilities.isEmpty()) {
                return "";
            }
            final TreeMap<String, Block> p = new TreeMap<String, Block>(possibilities);
            final Map.Entry<String, Block> e = p.firstEntry();
            return e.getKey().substring(chunkValue.length());
        }
        catch (Exception e2) {
            return "";
        }
    }
    
    static {
        BlockParser.blockNames = new HashMap<String, Block>();
    }
}

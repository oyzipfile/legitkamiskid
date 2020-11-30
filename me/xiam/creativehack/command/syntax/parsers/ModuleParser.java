// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.syntax.parsers;

import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.command.syntax.SyntaxChunk;

public class ModuleParser extends AbstractParser
{
    @Override
    public String getChunk(final SyntaxChunk[] chunks, final SyntaxChunk thisChunk, final String[] values, final String chunkValue) {
        if (chunkValue == null) {
            return this.getDefaultChunk(thisChunk);
        }
        final Module chosen = KamiMod.MODULE_MANAGER.getModules().stream().filter(module -> module.getName().toLowerCase().startsWith(chunkValue.toLowerCase())).filter(Module::isProduction).findFirst().orElse(null);
        if (chosen == null) {
            return null;
        }
        return chosen.getName().substring(chunkValue.length());
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.syntax.parsers;

import java.util.Iterator;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.ModuleManager;
import java.util.Map;
import java.util.TreeMap;
import me.xiam.creativehack.setting.Setting;
import java.util.HashMap;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.command.syntax.SyntaxChunk;

public class ValueParser extends AbstractParser
{
    private int moduleIndex;
    
    public ValueParser(final int moduleIndex) {
        this.moduleIndex = moduleIndex;
    }
    
    @Override
    public String getChunk(final SyntaxChunk[] chunks, final SyntaxChunk thisChunk, final String[] values, final String chunkValue) {
        if (this.moduleIndex > values.length - 1 || chunkValue == null) {
            return this.getDefaultChunk(thisChunk);
        }
        final String module = values[this.moduleIndex];
        try {
            final Module m = KamiMod.MODULE_MANAGER.getModule(module);
            final HashMap<String, Setting<?>> possibilities = new HashMap<String, Setting<?>>();
            for (final Setting<?> v : m.settingList) {
                if (v.getName().toLowerCase().startsWith(chunkValue.toLowerCase())) {
                    possibilities.put(v.getName(), v);
                }
            }
            if (possibilities.isEmpty()) {
                return "";
            }
            final TreeMap<String, Setting<?>> p = new TreeMap<String, Setting<?>>(possibilities);
            final Setting<?> aV = p.firstEntry().getValue();
            return aV.getName().substring(chunkValue.length());
        }
        catch (ModuleManager.ModuleNotFoundException x) {
            return "";
        }
    }
}

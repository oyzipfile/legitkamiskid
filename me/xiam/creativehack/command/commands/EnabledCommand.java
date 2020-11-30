// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import java.util.List;
import me.xiam.creativehack.util.MessageSendHelper;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;
import java.util.regex.Pattern;
import java.util.Collection;
import me.xiam.creativehack.module.Module;
import java.util.ArrayList;
import me.xiam.creativehack.KamiMod;
import java.util.concurrent.atomic.AtomicReference;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import me.xiam.creativehack.command.Command;

public class EnabledCommand extends Command
{
    public EnabledCommand() {
        super("enabled", new ChunkBuilder().append("filter").build(), new String[0]);
        this.setDescription("Prints enabled modules");
    }
    
    @Override
    public void call(final String[] args) {
        final AtomicReference<String> enabled = new AtomicReference<String>("");
        final List<Module> mods = new ArrayList<Module>(KamiMod.MODULE_MANAGER.getModules());
        String f = "";
        if (args[0] != null) {
            f = "(filter: " + args[0] + ")";
        }
        final AtomicReference<String> atomicReference;
        mods.forEach(module -> {
            if (args[0] == null) {
                if (module.isEnabled()) {
                    atomicReference.set(atomicReference + module.getName() + ", ");
                }
            }
            else if (module.isEnabled() && Pattern.compile(args[0], 2).matcher(module.getName()).find()) {
                atomicReference.set(atomicReference + module.getName() + ", ");
            }
            return;
        });
        enabled.set(StringUtils.chop(StringUtils.chop(String.valueOf(enabled))));
        MessageSendHelper.sendChatMessage("Enabled modules: " + f + "\n" + TextFormatting.GRAY + enabled);
    }
}

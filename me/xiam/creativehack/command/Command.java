// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command;

import me.xiam.creativehack.setting.Settings;
import java.util.Arrays;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.command.syntax.SyntaxChunk;
import net.minecraft.client.Minecraft;
import java.util.List;

public abstract class Command
{
    protected String label;
    protected String syntax;
    protected String description;
    protected List<String> aliases;
    public final Minecraft mc;
    protected SyntaxChunk[] syntaxChunks;
    public static Setting<String> commandPrefix;
    
    public Command(final String label, final SyntaxChunk[] syntaxChunks, final String... aliases) {
        this.mc = Minecraft.getMinecraft();
        this.label = label;
        this.syntaxChunks = syntaxChunks;
        this.description = "Descriptionless";
        this.aliases = Arrays.asList(aliases);
    }
    
    protected void setDescription(final String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public static String getCommandPrefix() {
        return Command.commandPrefix.getValue();
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public String getChatLabel() {
        return "[" + this.label + "] ";
    }
    
    public abstract void call(final String[] p0);
    
    public SyntaxChunk[] getSyntaxChunks() {
        return this.syntaxChunks;
    }
    
    public List<String> getAliases() {
        return this.aliases;
    }
    
    static {
        Command.commandPrefix = Settings.s("commandPrefix", ";");
    }
}

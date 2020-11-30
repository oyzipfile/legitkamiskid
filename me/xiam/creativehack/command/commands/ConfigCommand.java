// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import me.xiam.creativehack.gui.kami.KamiGUI;
import java.io.BufferedWriter;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.io.IOException;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.command.syntax.parsers.DependantParser;
import me.xiam.creativehack.command.syntax.SyntaxParser;
import me.xiam.creativehack.command.syntax.parsers.EnumParser;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import me.xiam.creativehack.command.Command;

public class ConfigCommand extends Command
{
    public ConfigCommand() {
        super("config", new ChunkBuilder().append("mode", true, new EnumParser(new String[] { "reload", "save", "path" })).append("path", true, new DependantParser(0, new DependantParser.Dependency(new String[][] { { "path", "path" } }, ""))).build(), new String[] { "cfg" });
        this.setDescription("Change where your config is saved or manually save and reload your config");
    }
    
    @Override
    public void call(final String[] args) {
        if (args[0] == null) {
            MessageSendHelper.sendChatMessage("Missing argument &bmode&r: Choose from reload, save or path");
            return;
        }
        final String lowerCase = args[0].toLowerCase();
        switch (lowerCase) {
            case "reload": {
                this.reload();
                return;
            }
            case "save": {
                try {
                    KamiMod.saveConfigurationUnsafe();
                    MessageSendHelper.sendChatMessage("Saved configuration!");
                }
                catch (IOException e) {
                    e.printStackTrace();
                    MessageSendHelper.sendChatMessage("Failed to save! " + e.getMessage());
                }
                return;
            }
            case "path": {
                if (args[1] == null) {
                    final Path file = Paths.get(KamiMod.getConfigName(), new String[0]);
                    MessageSendHelper.sendChatMessage("Path to configuration: &b" + file.toAbsolutePath().toString());
                    return;
                }
                final String newPath = args[1];
                if (!KamiMod.isFilenameValid(newPath)) {
                    MessageSendHelper.sendChatMessage("&b" + newPath + "&r is not a valid path");
                    return;
                }
                try (final BufferedWriter writer = Files.newBufferedWriter(Paths.get("KAMILastConfig.txt", new String[0]), new OpenOption[0])) {
                    writer.write(newPath);
                    this.reload();
                    MessageSendHelper.sendChatMessage("Configuration path set to &b" + newPath + "&r!");
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                    MessageSendHelper.sendChatMessage("Couldn't set path: " + e2.getMessage());
                    return;
                }
                break;
            }
        }
        MessageSendHelper.sendChatMessage("Incorrect mode, please choose from: reload, save or path");
    }
    
    private void reload() {
        (KamiMod.getInstance().guiManager = new KamiGUI()).initializeGUI();
        KamiMod.loadConfiguration();
        MessageSendHelper.sendChatMessage("Configuration reloaded!");
    }
}

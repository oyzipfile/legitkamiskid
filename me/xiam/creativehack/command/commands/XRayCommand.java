// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import net.minecraft.block.Block;
import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.module.modules.render.XRay;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import me.xiam.creativehack.command.Command;

public class XRayCommand extends Command
{
    public XRayCommand() {
        super("xray", new ChunkBuilder().append("help").append("+block|-block|=block").append("list|defaults|clear|invert").build(), new String[0]);
        this.setDescription("Allows you to add or remove blocks from the &fxray &7module");
    }
    
    @Override
    public void call(final String[] args) {
        final XRay xr = KamiMod.MODULE_MANAGER.getModuleT(XRay.class);
        if (xr == null) {
            MessageSendHelper.sendErrorMessage("&cThe module is not available for some reason. Make sure the name you're calling is correct and that you have the module installed!!");
            return;
        }
        if (!xr.isEnabled()) {
            MessageSendHelper.sendWarningMessage("&6Warning: The " + xr.getName() + " module is not enabled!");
            MessageSendHelper.sendWarningMessage("These commands will still have effect, but will not visibly do anything.");
        }
        for (final String s : args) {
            if (s != null) {
                if (s.equalsIgnoreCase("help")) {
                    MessageSendHelper.sendChatMessage("The " + xr.getName() + " module has a list of blocks");
                    MessageSendHelper.sendChatMessage("Normally, the " + xr.getName() + " module hides these blocks");
                    MessageSendHelper.sendChatMessage("When the Invert setting is on, the " + xr.getName() + " only shows these blocks");
                    MessageSendHelper.sendChatMessage("This command is a convenient way to quickly edit the list");
                    MessageSendHelper.sendChatMessage("Available options: \n+block: Adds a block to the list\n-block: Removes a block from the list\n=block: Changes the list to only that block\nlist: Prints the list of selected blocks\ndefaults: Resets the list to the default list\nclear: Removes all blocks from the " + xr.getName() + " block list\ninvert: Quickly toggles the invert setting");
                }
                else if (s.equalsIgnoreCase("clear")) {
                    xr.extClear();
                    MessageSendHelper.sendWarningMessage("Cleared the " + xr.getName() + " block list");
                }
                else if (s.equalsIgnoreCase("defaults")) {
                    xr.extDefaults();
                    MessageSendHelper.sendChatMessage("Reset the " + xr.getName() + " block list to default");
                }
                else if (s.equalsIgnoreCase("list")) {
                    MessageSendHelper.sendChatMessage("\n" + xr.extGet());
                }
                else if (s.equalsIgnoreCase("invert")) {
                    if (xr.invert.getValue()) {
                        xr.invert.setValue(false);
                        MessageSendHelper.sendChatMessage("Disabled " + xr.getName() + " Invert");
                    }
                    else {
                        xr.invert.setValue(true);
                        MessageSendHelper.sendChatMessage("Enabled " + xr.getName() + " Invert");
                    }
                }
                else if (s.startsWith("=")) {
                    final String sT = s.replace("=", "");
                    xr.extSet(sT);
                    MessageSendHelper.sendChatMessage("Set the " + xr.getName() + " block list to " + sT);
                }
                else if (s.startsWith("+") || s.startsWith("-")) {
                    final String name = s.substring(1);
                    final Block b = Block.getBlockFromName(name);
                    if (b == null) {
                        MessageSendHelper.sendChatMessage("&cInvalid block name <" + name + ">");
                    }
                    else if (s.startsWith("+")) {
                        MessageSendHelper.sendChatMessage("Added <" + name + "> to the " + xr.getName() + " block list");
                        xr.extAdd(name);
                    }
                    else {
                        MessageSendHelper.sendChatMessage("Removed <" + name + "> from the " + xr.getName() + " block list");
                        xr.extRemove(name);
                    }
                }
                else {
                    MessageSendHelper.sendChatMessage("&cInvalid subcommand <" + s + ">");
                }
            }
        }
    }
}

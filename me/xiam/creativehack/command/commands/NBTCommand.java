// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import me.xiam.creativehack.util.MessageSendHelper;
import java.awt.Toolkit;
import me.xiam.creativehack.command.syntax.SyntaxParser;
import me.xiam.creativehack.command.syntax.parsers.EnumParser;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import me.xiam.creativehack.command.Command;

public class NBTCommand extends Command
{
    private final Clipboard clipboard;
    private StringSelection nbt;
    
    public NBTCommand() {
        super("nbt", new ChunkBuilder().append("action", true, new EnumParser(new String[] { "get", "copy", "wipe" })).build(), new String[0]);
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        this.setDescription("Does NBT related stuff (&fget&7, &fcopy&7, &fset&7)");
    }
    
    @Override
    public void call(final String[] args) {
        if (args[0].isEmpty()) {
            MessageSendHelper.sendErrorMessage("Invalid Syntax!");
            return;
        }
        final ItemStack item = this.mc.player.inventory.getCurrentItem();
        if (args[0].equalsIgnoreCase("get")) {
            if (item.getTagCompound() != null) {
                MessageSendHelper.sendChatMessage("&6&lNBT:\n" + item.getTagCompound() + "");
            }
            else {
                MessageSendHelper.sendErrorMessage("No NBT on " + item.getDisplayName());
            }
        }
        else if (args[0].equalsIgnoreCase("copy")) {
            if (item.getTagCompound() != null) {
                this.nbt = new StringSelection(item.getTagCompound() + "");
                this.clipboard.setContents(this.nbt, this.nbt);
                MessageSendHelper.sendChatMessage("&6Copied\n&f" + item.getTagCompound() + "\n" + "&6to clipboard.");
            }
            else {
                MessageSendHelper.sendErrorMessage("No NBT on " + item.getDisplayName());
            }
        }
        else if (args[0].equalsIgnoreCase("wipe")) {
            MessageSendHelper.sendChatMessage("&6Wiped\n&f" + item.getTagCompound() + "\n" + "&6from " + item.getDisplayName() + ".");
            item.setTagCompound(new NBTTagCompound());
        }
    }
}

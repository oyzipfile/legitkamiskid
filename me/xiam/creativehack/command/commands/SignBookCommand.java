// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.item.ItemWritableBook;
import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.util.Wrapper;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import me.xiam.creativehack.command.Command;

public class SignBookCommand extends Command
{
    public SignBookCommand() {
        super("signbook", new ChunkBuilder().append("name").build(), new String[] { "sign" });
        this.setDescription("Colored book names. &f#n&7 for a new line and &f&&7 for colour codes");
    }
    
    @Override
    public void call(final String[] args) {
        final ItemStack is = Wrapper.getPlayer().inventory.getCurrentItem();
        final int c = 167;
        if (args.length == 1) {
            MessageSendHelper.sendChatMessage("Please specify a title.");
            return;
        }
        if (is.getItem() instanceof ItemWritableBook) {
            final ArrayList<String> toAdd = new ArrayList<String>(Arrays.asList(args));
            String futureTitle = String.join(" ", toAdd);
            futureTitle = futureTitle.replaceAll("&", Character.toString((char)c));
            futureTitle = futureTitle.replaceAll("#n", "\n");
            futureTitle = futureTitle.replaceAll("null", "");
            if (futureTitle.length() > 31) {
                MessageSendHelper.sendChatMessage("Title cannot be over 31 characters.");
                return;
            }
            final NBTTagList pages = new NBTTagList();
            final String pageText = "";
            pages.appendTag((NBTBase)new NBTTagString(pageText));
            final NBTTagCompound bookData = is.getTagCompound();
            if (is.hasTagCompound()) {
                if (bookData != null) {
                    is.setTagCompound(bookData);
                }
                is.getTagCompound().setTag("title", (NBTBase)new NBTTagString(futureTitle));
                is.getTagCompound().setTag("author", (NBTBase)new NBTTagString(Wrapper.getPlayer().getName()));
            }
            else {
                is.setTagInfo("pages", (NBTBase)pages);
                is.setTagInfo("title", (NBTBase)new NBTTagString(futureTitle));
                is.setTagInfo("author", (NBTBase)new NBTTagString(Wrapper.getPlayer().getName()));
            }
            final PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
            buf.writeItemStack(is);
            Wrapper.getPlayer().connection.sendPacket((Packet)new CPacketCustomPayload("MC|BSign", buf));
            MessageSendHelper.sendChatMessage("Signed book with title: " + futureTitle + "&r");
        }
        else {
            MessageSendHelper.sendChatMessage("You must be holding a writable book.");
        }
    }
}

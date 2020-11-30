// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.player;

import java.util.function.Predicate;
import net.minecraft.entity.player.EntityPlayer;
import me.xiam.creativehack.util.MessageSendHelper;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.network.play.client.CPacketClickWindow;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.module.Module;

@Info(name = "AntiBookKick", category = Category.MISC, description = "Prevents being kicked by clicking on books", showOnArray = ShowOnArray.OFF)
public class AntiBookKick extends Module
{
    @EventHandler
    public Listener<PacketEvent.Receive> listener;
    
    public AntiBookKick() {
        CPacketClickWindow packet;
        this.listener = new Listener<PacketEvent.Receive>(event -> {
            if (!(!(event.getPacket() instanceof CPacketClickWindow))) {
                packet = (CPacketClickWindow)event.getPacket();
                if (!(!(packet.getClickedItem().getItem() instanceof ItemWrittenBook))) {
                    event.cancel();
                    MessageSendHelper.sendWarningMessage(this.getChatName() + "Don't click the book \"" + packet.getClickedItem().getDisplayName() + "\", shift click it instead!");
                    AntiBookKick.mc.player.openContainer.slotClick(packet.getSlotId(), packet.getUsedButton(), packet.getClickType(), (EntityPlayer)AntiBookKick.mc.player);
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
}

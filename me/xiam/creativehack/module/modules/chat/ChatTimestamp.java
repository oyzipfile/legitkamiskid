// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.chat;

import me.xiam.creativehack.util.MessageSendHelper;
import net.minecraft.util.text.TextFormatting;
import java.util.function.Predicate;
import net.minecraft.network.play.server.SPacketChat;
import me.xiam.creativehack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.util.TimeUtil;
import me.xiam.creativehack.util.ColourTextFormatting;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "ChatTimestamp", category = Category.CHAT, description = "Shows the time a message was sent beside the message", showOnArray = ShowOnArray.OFF)
public class ChatTimestamp extends Module
{
    private Setting<ColourTextFormatting.ColourCode> firstColour;
    private Setting<ColourTextFormatting.ColourCode> secondColour;
    private Setting<TimeUtil.TimeType> timeTypeSetting;
    private Setting<TimeUtil.TimeUnit> timeUnitSetting;
    private Setting<Boolean> doLocale;
    @EventHandler
    public Listener<PacketEvent.Receive> listener;
    
    public ChatTimestamp() {
        this.firstColour = this.register(Settings.e("First Colour", ColourTextFormatting.ColourCode.GRAY));
        this.secondColour = this.register(Settings.e("Second Colour", ColourTextFormatting.ColourCode.WHITE));
        this.timeTypeSetting = this.register(Settings.e("Time Format", TimeUtil.TimeType.HHMM));
        this.timeUnitSetting = this.register(Settings.e("Time Unit", TimeUtil.TimeUnit.H12));
        this.doLocale = this.register(Settings.b("Show AMPM", true));
        SPacketChat sPacketChat;
        this.listener = new Listener<PacketEvent.Receive>(event -> {
            if (ChatTimestamp.mc.player != null && !this.isDisabled()) {
                if (!(!(event.getPacket() instanceof SPacketChat))) {
                    sPacketChat = (SPacketChat)event.getPacket();
                    if (this.addTime(sPacketChat.getChatComponent().getFormattedText())) {
                        event.cancel();
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    private boolean addTime(final String message) {
        MessageSendHelper.sendRawChatMessage("<" + TimeUtil.getFinalTime(this.setToText(this.secondColour.getValue()), this.setToText(this.firstColour.getValue()), this.timeUnitSetting.getValue(), this.timeTypeSetting.getValue(), this.doLocale.getValue()) + TextFormatting.RESET + "> " + message);
        return true;
    }
    
    public String returnFormatted() {
        return "<" + TimeUtil.getFinalTime(this.timeUnitSetting.getValue(), this.timeTypeSetting.getValue(), this.doLocale.getValue()) + "> ";
    }
    
    private TextFormatting setToText(final ColourTextFormatting.ColourCode colourCode) {
        return ColourTextFormatting.toTextMap.get(colourCode);
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.combat;

import me.xiam.creativehack.util.MessageSendHelper;
import net.minecraft.util.text.TextFormatting;
import me.xiam.creativehack.util.Friends;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import me.xiam.creativehack.KamiMod;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import java.util.function.Predicate;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.EntityUseTotemEvent;
import me.zero.alpine.listener.Listener;
import java.util.HashMap;
import me.xiam.creativehack.util.ColourTextFormatting;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "TotemPopCounter", description = "Counts how many times players pop", category = Category.COMBAT)
public class TotemPopCounter extends Module
{
    private Setting<Boolean> countFriends;
    private Setting<Boolean> countSelf;
    private Setting<Boolean> resetDeaths;
    private Setting<Boolean> resetSelfDeaths;
    private Setting<Announce> announceSetting;
    private Setting<Boolean> thanksTo;
    private Setting<ColourTextFormatting.ColourCode> colourCode;
    private Setting<ColourTextFormatting.ColourCode> colourCode1;
    private HashMap<String, Integer> playerList;
    private boolean isDead;
    @EventHandler
    public Listener<EntityUseTotemEvent> listListener;
    @EventHandler
    public Listener<PacketEvent.Receive> popListener;
    
    public TotemPopCounter() {
        this.countFriends = this.register(Settings.b("Count Friends", true));
        this.countSelf = this.register(Settings.b("Count Self", false));
        this.resetDeaths = this.register(Settings.b("Reset On Death", true));
        this.resetSelfDeaths = this.register(Settings.b("Reset Self Death", true));
        this.announceSetting = this.register(Settings.e("Announce", Announce.CLIENT));
        this.thanksTo = this.register(Settings.b("Thanks to", false));
        this.colourCode = this.register(Settings.e("Color Name", ColourTextFormatting.ColourCode.DARK_PURPLE));
        this.colourCode1 = this.register(Settings.e("Color Number", ColourTextFormatting.ColourCode.LIGHT_PURPLE));
        this.playerList = new HashMap<String, Integer>();
        this.isDead = false;
        int popCounter;
        this.listListener = new Listener<EntityUseTotemEvent>(event -> {
            if (this.playerList == null) {
                this.playerList = new HashMap<String, Integer>();
            }
            if (this.playerList.get(event.getEntity().getName()) == null) {
                this.playerList.put(event.getEntity().getName(), 1);
                this.sendMessage(this.formatName(event.getEntity().getName()) + " popped " + this.formatNumber(1) + " totem" + this.ending());
            }
            else if (this.playerList.get(event.getEntity().getName()) != null) {
                popCounter = this.playerList.get(event.getEntity().getName());
                ++popCounter;
                this.playerList.put(event.getEntity().getName(), popCounter);
                this.sendMessage(this.formatName(event.getEntity().getName()) + " popped " + this.formatNumber(popCounter) + " totems" + this.ending());
            }
            return;
        }, (Predicate<EntityUseTotemEvent>[])new Predicate[0]);
        SPacketEntityStatus packet;
        Entity entity;
        this.popListener = new Listener<PacketEvent.Receive>(event -> {
            if (TotemPopCounter.mc.player != null) {
                if (event.getPacket() instanceof SPacketEntityStatus) {
                    packet = (SPacketEntityStatus)event.getPacket();
                    if (packet.getOpCode() == 35) {
                        entity = packet.getEntity((World)TotemPopCounter.mc.world);
                        if (this.friendCheck(entity.getName()) || this.selfCheck(entity.getName())) {
                            KamiMod.EVENT_BUS.post(new EntityUseTotemEvent(entity));
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (!this.isDead && this.resetSelfDeaths.getValue() && 0.0f >= TotemPopCounter.mc.player.getHealth()) {
            this.sendMessage(this.formatName(TotemPopCounter.mc.player.getName()) + " died and " + this.grammar(TotemPopCounter.mc.player.getName()) + " pop list was reset!");
            this.isDead = true;
            this.playerList.clear();
            return;
        }
        if (this.isDead && 0.0f < TotemPopCounter.mc.player.getHealth()) {
            this.isDead = false;
        }
        for (final EntityPlayer player : TotemPopCounter.mc.world.playerEntities) {
            if (this.resetDeaths.getValue() && 0.0f >= player.getHealth() && this.friendCheck(player.getName()) && this.selfCheck(player.getName()) && this.playerList.containsKey(player.getName())) {
                this.sendMessage(this.formatName(player.getName()) + " died after popping " + this.formatNumber(this.playerList.get(player.getName())) + " totems" + this.ending());
                this.playerList.remove(player.getName(), this.playerList.get(player.getName()));
            }
        }
    }
    
    private boolean friendCheck(final String name) {
        if (this.isDead) {
            return false;
        }
        for (final Friends.Friend names : Friends.friends.getValue()) {
            if (names.getUsername().equalsIgnoreCase(name)) {
                return this.countFriends.getValue();
            }
        }
        return true;
    }
    
    private boolean selfCheck(final String name) {
        return !this.isDead && ((this.countSelf.getValue() && name.equalsIgnoreCase(TotemPopCounter.mc.player.getName())) || this.countSelf.getValue() || !name.equalsIgnoreCase(TotemPopCounter.mc.player.getName()));
    }
    
    private boolean isSelf(final String name) {
        return name.equalsIgnoreCase(TotemPopCounter.mc.player.getName());
    }
    
    private boolean isFriend(final String name) {
        for (final Friends.Friend names : Friends.friends.getValue()) {
            if (names.getUsername().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    
    private String formatName(String name) {
        String extraText = "";
        if (this.isFriend(name) && !this.isPublic()) {
            extraText = "Your friend, ";
        }
        else if (this.isFriend(name) && this.isPublic()) {
            extraText = "My friend, ";
        }
        if (this.isSelf(name)) {
            extraText = "";
            name = "I";
        }
        if (this.announceSetting.getValue().equals(Announce.EVERYONE)) {
            return extraText + name;
        }
        return extraText + this.setToText(this.colourCode.getValue()) + name + TextFormatting.RESET;
    }
    
    private String grammar(final String name) {
        if (this.isSelf(name)) {
            return "my";
        }
        return "their";
    }
    
    private String ending() {
        if (this.thanksTo.getValue()) {
            return " thanks to KAMI Blue!";
        }
        return "!";
    }
    
    private boolean isPublic() {
        return this.announceSetting.getValue().equals(Announce.EVERYONE);
    }
    
    private String formatNumber(final int message) {
        if (this.announceSetting.getValue().equals(Announce.EVERYONE)) {
            return "" + message;
        }
        return this.setToText(this.colourCode1.getValue()) + "" + message + TextFormatting.RESET;
    }
    
    private void sendMessage(final String message) {
        switch (this.announceSetting.getValue()) {
            case CLIENT: {
                MessageSendHelper.sendRawChatMessage(message);
            }
            case EVERYONE: {
                MessageSendHelper.sendServerMessage(message);
            }
            default: {}
        }
    }
    
    private TextFormatting setToText(final ColourTextFormatting.ColourCode colourCode) {
        return ColourTextFormatting.toTextMap.get(colourCode);
    }
    
    private enum Announce
    {
        CLIENT, 
        EVERYONE;
    }
}

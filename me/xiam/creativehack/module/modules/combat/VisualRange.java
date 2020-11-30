// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.combat;

import java.util.Iterator;
import me.xiam.creativehack.util.MessageSendHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.xiam.creativehack.util.Friends;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import java.util.ArrayList;
import me.xiam.creativehack.setting.Settings;
import java.util.List;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "VisualRange", description = "Shows players who enter and leave range in chat", category = Category.COMBAT)
public class VisualRange extends Module
{
    private Setting<Boolean> leaving;
    private Setting<Boolean> uwuAura;
    private List<String> knownPlayers;
    
    public VisualRange() {
        this.leaving = this.register(Settings.b("Leaving", false));
        this.uwuAura = this.register(Settings.b("UwU Aura", false));
    }
    
    @Override
    public void onUpdate() {
        if (VisualRange.mc.player == null) {
            return;
        }
        final List<String> tickPlayerList = new ArrayList<String>();
        for (final Entity entity : VisualRange.mc.world.getLoadedEntityList()) {
            if (entity instanceof EntityPlayer) {
                tickPlayerList.add(entity.getName());
            }
        }
        if (tickPlayerList.size() > 0) {
            for (final String playerName : tickPlayerList) {
                if (playerName.equals(VisualRange.mc.player.getName())) {
                    continue;
                }
                if (!this.knownPlayers.contains(playerName)) {
                    this.knownPlayers.add(playerName);
                    if (Friends.isFriend(playerName)) {
                        this.sendNotification(ChatFormatting.GREEN.toString() + playerName + ChatFormatting.RESET.toString() + " entered the Battlefield!");
                    }
                    else {
                        this.sendNotification(ChatFormatting.RED.toString() + playerName + ChatFormatting.RESET.toString() + " entered the Battlefield!");
                    }
                    if (this.uwuAura.getValue()) {
                        MessageSendHelper.sendServerMessage("/w " + playerName + " hi uwu");
                    }
                    return;
                }
            }
        }
        if (this.knownPlayers.size() > 0) {
            for (final String playerName : this.knownPlayers) {
                if (!tickPlayerList.contains(playerName)) {
                    this.knownPlayers.remove(playerName);
                    if (this.leaving.getValue()) {
                        if (Friends.isFriend(playerName)) {
                            this.sendNotification(ChatFormatting.GREEN.toString() + playerName + ChatFormatting.RESET.toString() + " left the Battlefield!");
                        }
                        else {
                            this.sendNotification(ChatFormatting.RED.toString() + playerName + ChatFormatting.RESET.toString() + " left the Battlefield!");
                        }
                        if (this.uwuAura.getValue()) {
                            MessageSendHelper.sendServerMessage("/w " + playerName + " bye uwu");
                        }
                    }
                }
            }
        }
    }
    
    private void sendNotification(final String s) {
        MessageSendHelper.sendChatMessage(s);
    }
    
    public void onEnable() {
        this.knownPlayers = new ArrayList<String>();
    }
}

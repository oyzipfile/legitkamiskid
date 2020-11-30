// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.render;

import me.xiam.creativehack.util.Friends;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.network.NetworkPlayerInfo;
import me.xiam.creativehack.module.Module;

@Info(name = "TabFriends", description = "Highlights friends in the tab menu", category = Category.RENDER, showOnArray = ShowOnArray.OFF)
public class TabFriends extends Module
{
    public static TabFriends INSTANCE;
    
    public TabFriends() {
        TabFriends.INSTANCE = this;
    }
    
    public static String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        final String dname = (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        if (Friends.isFriend(dname)) {
            return String.format("%sa%s", '§', dname);
        }
        return dname;
    }
}

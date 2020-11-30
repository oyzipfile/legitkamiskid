// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.misc;

import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.DiscordPresence;
import me.xiam.creativehack.util.InfoCalculator;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "DiscordRPC", category = Category.MISC, description = "Discord Rich Presence")
public class DiscordRPC extends Module
{
    private Setting<Boolean> coordsConfirm;
    public Setting<LineInfo> line1Setting;
    public Setting<LineInfo> line3Setting;
    public Setting<LineInfo> line2Setting;
    public Setting<LineInfo> line4Setting;
    private static long startTime;
    
    public DiscordRPC() {
        this.coordsConfirm = this.register(Settings.b("Coords Confirm", false));
        this.line1Setting = this.register(Settings.e("Line 1 Left", LineInfo.VERSION));
        this.line3Setting = this.register(Settings.e("Line 1 Right", LineInfo.USERNAME));
        this.line2Setting = this.register(Settings.e("Line 2 Left", LineInfo.SERVER_IP));
        this.line4Setting = this.register(Settings.e("Line 2 Right", LineInfo.HEALTH));
    }
    
    public String getLine(final LineInfo line) {
        switch (line) {
            case VERSION: {
                return "1.0.0";
            }
            case WORLD: {
                if (DiscordRPC.mc.isIntegratedServerRunning()) {
                    return "by Xiamgodlol123xd";
                }
                if (DiscordRPC.mc.getCurrentServerData() != null) {
                    return "by Xiamgodlol123xd";
                }
                return "by Xiamgodlol123xd";
            }
            case DIMENSION: {
                return InfoCalculator.playerDimension(DiscordRPC.mc);
            }
            case USERNAME: {
                if (DiscordRPC.mc.player != null) {
                    return "Cr4tiv3h4ck";
                }
                return "Cr4tiv3h4ck";
            }
            case HEALTH: {
                if (DiscordRPC.mc.player != null) {
                    return "ez";
                }
                return "ez";
            }
            case SERVER_IP: {
                if (DiscordRPC.mc.getCurrentServerData() != null) {
                    return "by Xiamgodlol123xd";
                }
                if (DiscordRPC.mc.isIntegratedServerRunning()) {
                    return "by Xiamgodlol123xd";
                }
                return "by Xiamgodlol123xd";
            }
            case COORDS: {
                if (DiscordRPC.mc.player != null && this.coordsConfirm.getValue()) {
                    return "(" + (int)DiscordRPC.mc.player.posX + " " + (int)DiscordRPC.mc.player.posY + " " + (int)DiscordRPC.mc.player.posZ + ")";
                }
                return "No coords";
            }
            default: {
                return "";
            }
        }
    }
    
    public void onEnable() {
        DiscordPresence.start();
    }
    
    @Override
    public void onUpdate() {
        if (DiscordRPC.startTime == 0L) {
            DiscordRPC.startTime = System.currentTimeMillis();
        }
        if (DiscordRPC.startTime + 10000L <= System.currentTimeMillis()) {
            if ((this.line1Setting.getValue().equals(LineInfo.COORDS) || this.line2Setting.getValue().equals(LineInfo.COORDS) || this.line3Setting.getValue().equals(LineInfo.COORDS) || this.line4Setting.getValue().equals(LineInfo.COORDS)) && !this.coordsConfirm.getValue() && DiscordRPC.mc.player != null) {
                MessageSendHelper.sendWarningMessage(this.getChatName() + " Warning: In order to use the coords option please enable the coords confirmation option. This will display your coords on the discord rpc. Do NOT use this if you do not want your coords displayed");
            }
            DiscordRPC.startTime = System.currentTimeMillis();
        }
    }
    
    @Override
    protected void onDisable() {
        DiscordPresence.end();
    }
    
    static {
        DiscordRPC.startTime = 0L;
    }
    
    public enum LineInfo
    {
        VERSION, 
        WORLD, 
        DIMENSION, 
        USERNAME, 
        HEALTH, 
        SERVER_IP, 
        COORDS, 
        NONE;
    }
}

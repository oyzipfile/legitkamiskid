// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.player;

import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "NoEntityTrace", category = Category.PLAYER, description = "Blocks entities from stopping you from mining")
public class NoEntityTrace extends Module
{
    private Setting<TraceMode> mode;
    private static NoEntityTrace INSTANCE;
    
    public NoEntityTrace() {
        this.mode = this.register(Settings.e("Mode", TraceMode.DYNAMIC));
        NoEntityTrace.INSTANCE = this;
    }
    
    public static boolean shouldBlock() {
        return NoEntityTrace.INSTANCE.isEnabled() && (NoEntityTrace.INSTANCE.mode.getValue() == TraceMode.STATIC || NoEntityTrace.mc.playerController.isHittingBlock);
    }
    
    private enum TraceMode
    {
        STATIC, 
        DYNAMIC;
    }
}

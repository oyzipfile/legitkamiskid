// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.render;

import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AntiFog", description = "Disables or reduces fog", category = Category.RENDER)
public class AntiFog extends Module
{
    public static Setting<VisionMode> mode;
    private static AntiFog INSTANCE;
    
    public AntiFog() {
        (AntiFog.INSTANCE = this).register(AntiFog.mode);
    }
    
    public static boolean enabled() {
        return AntiFog.INSTANCE.isEnabled();
    }
    
    static {
        AntiFog.mode = Settings.e("Mode", VisionMode.NOFOG);
        AntiFog.INSTANCE = new AntiFog();
    }
    
    public enum VisionMode
    {
        NOFOG, 
        AIR;
    }
}

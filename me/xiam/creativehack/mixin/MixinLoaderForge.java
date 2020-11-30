// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin;

import org.apache.logging.log4j.LogManager;
import java.util.Map;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class MixinLoaderForge implements IFMLLoadingPlugin
{
    public static final Logger log;
    private static boolean isObfuscatedEnvironment;
    
    public MixinLoaderForge() {
        MixinLoaderForge.log.info("KAMI mixins initialized");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.kami.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        MixinLoaderForge.log.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }
    
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
        MixinLoaderForge.isObfuscatedEnvironment = data.get("runtimeDeobfuscationEnabled");
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
    
    static {
        log = LogManager.getLogger("KAMI Blue");
        MixinLoaderForge.isObfuscatedEnvironment = false;
    }
}

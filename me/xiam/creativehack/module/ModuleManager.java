// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module;

import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.renderer.Tessellator;
import me.xiam.creativehack.event.events.RenderEvent;
import me.xiam.creativehack.util.KamiTessellator;
import me.xiam.creativehack.util.EntityUtil;
import java.util.Objects;
import me.xiam.creativehack.util.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import java.util.Set;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.util.Comparator;
import me.xiam.creativehack.util.ClassFinder;
import me.xiam.creativehack.module.modules.ClickGUI;
import me.xiam.creativehack.KamiMod;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModuleManager
{
    private Map<Class<? extends Module>, Module> modules;
    
    public ModuleManager() {
        this.modules = new LinkedHashMap<Class<? extends Module>, Module>();
    }
    
    public void register() {
        KamiMod.log.info("Registering modules...");
        final Set<Class> classList = ClassFinder.findClasses(ClickGUI.class.getPackage().getName(), Module.class);
        Module module;
        classList.stream().sorted(Comparator.comparing((Function<? super Object, ? extends Comparable>)Class::getSimpleName)).forEach(aClass -> {
            try {
                module = aClass.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
                this.modules.put(module.getClass(), module);
            }
            catch (InvocationTargetException e) {
                e.getCause().printStackTrace();
                System.err.println("Couldn't initiate module " + aClass.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
            }
            catch (Exception e2) {
                e2.printStackTrace();
                System.err.println("Couldn't initiate module " + aClass.getSimpleName() + "! Err: " + e2.getClass().getSimpleName() + ", message: " + e2.getMessage());
            }
            return;
        });
        KamiMod.log.info("Modules registered");
    }
    
    public void onUpdate() {
        this.modules.forEach((clazz, mod) -> {
            if (mod.alwaysListening || mod.isEnabled()) {
                mod.onUpdate();
            }
        });
    }
    
    public void onRender() {
        this.modules.forEach((clazz, mod) -> {
            if (mod.alwaysListening || mod.isEnabled()) {
                mod.onRender();
            }
        });
    }
    
    public void onWorldRender(final RenderWorldLastEvent event) {
        Minecraft.getMinecraft().profiler.startSection("kami");
        Minecraft.getMinecraft().profiler.startSection("setup");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1.0f);
        final Vec3d renderPos = EntityUtil.getInterpolatedPos(Objects.requireNonNull(Wrapper.getMinecraft().getRenderViewEntity()), event.getPartialTicks());
        final RenderEvent e = new RenderEvent(KamiTessellator.INSTANCE, renderPos);
        e.resetTranslation();
        Minecraft.getMinecraft().profiler.endSection();
        final RenderEvent event2;
        this.modules.forEach((clazz, mod) -> {
            if (mod.alwaysListening || mod.isEnabled()) {
                Minecraft.getMinecraft().profiler.startSection(mod.getOriginalName());
                mod.onWorldRender(event2);
                Minecraft.getMinecraft().profiler.endSection();
            }
            return;
        });
        Minecraft.getMinecraft().profiler.startSection("release");
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        KamiTessellator.releaseGL();
        Minecraft.getMinecraft().profiler.endSection();
    }
    
    public void onBind(final int eventKey) {
        if (eventKey == 0) {
            return;
        }
        this.modules.forEach((clazz, module) -> {
            if (module.getBind().isDown(eventKey)) {
                module.toggle();
            }
        });
    }
    
    public Collection<Module> getModules() {
        return Collections.unmodifiableCollection((Collection<? extends Module>)this.modules.values());
    }
    
    public Module getModule(final Class<? extends Module> clazz) {
        return this.modules.get(clazz);
    }
    
    public <T extends Module> T getModuleT(final Class<T> clazz) {
        return (T)this.modules.get(clazz);
    }
    
    @Deprecated
    public Module getModule(final String name) {
        for (final Map.Entry<Class<? extends Module>, Module> module : this.modules.entrySet()) {
            if (module.getClass().getSimpleName().equalsIgnoreCase(name) || module.getValue().getOriginalName().equalsIgnoreCase(name)) {
                return module.getValue();
            }
        }
        throw new ModuleNotFoundException("Error: Module not found. Check the spelling of the module. (getModuleByName(String) failed)");
    }
    
    public boolean isModuleEnabled(final Class<? extends Module> clazz) {
        return this.getModule(clazz).isEnabled();
    }
    
    @Deprecated
    public boolean isModuleEnabled(final String moduleName) {
        return this.getModule(moduleName).isEnabled();
    }
    
    public static class ModuleNotFoundException extends IllegalArgumentException
    {
        public ModuleNotFoundException(final String s) {
            super(s);
        }
    }
}

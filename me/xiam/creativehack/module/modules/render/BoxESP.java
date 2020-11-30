// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.render;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.item.EntityXPOrb;
import java.util.Iterator;
import me.xiam.creativehack.util.ColourConverter;
import me.xiam.creativehack.util.KamiTessellator;
import net.minecraft.entity.Entity;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import java.util.List;
import me.xiam.creativehack.event.events.RenderEvent;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "BoxESP", description = "Draws a box around small entities", category = Category.RENDER)
public class BoxESP extends Module
{
    private Setting<Boolean> experience;
    private Setting<Boolean> arrows;
    private Setting<Boolean> throwable;
    private Setting<Boolean> items;
    private Setting<Integer> alpha;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    
    public BoxESP() {
        this.experience = this.register(Settings.b("Experience", true));
        this.arrows = this.register(Settings.b("Arrows", true));
        this.throwable = this.register(Settings.b("Throwable", true));
        this.items = this.register(Settings.b("Items", false));
        this.alpha = this.register((Setting<Integer>)Settings.integerBuilder("Alpha").withMinimum(1).withMaximum(255).withValue(100).build());
        this.red = this.register((Setting<Integer>)Settings.integerBuilder("Red").withMinimum(1).withMaximum(255).withValue(155).build());
        this.green = this.register((Setting<Integer>)Settings.integerBuilder("Green").withMinimum(1).withMaximum(255).withValue(144).build());
        this.blue = this.register((Setting<Integer>)Settings.integerBuilder("Blue").withMinimum(1).withMaximum(255).withValue(255).build());
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final List<Entity> entities = (List<Entity>)BoxESP.mc.world.loadedEntityList.stream().filter(this::getEntity).collect(Collectors.toList());
        for (final Entity e : entities) {
            KamiTessellator.prepare(7);
            final int colour = ColourConverter.rgbToInt(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
            KamiTessellator.drawBoxSmall((float)e.getPositionVector().x - 0.25f, (float)e.getPositionVector().y, (float)e.getPositionVector().z - 0.25f, colour, 63);
            KamiTessellator.release();
        }
    }
    
    private boolean getEntity(final Entity entity) {
        return (entity instanceof EntityXPOrb && this.experience.getValue()) || (entity instanceof EntityArrow && this.arrows.getValue()) || (entity instanceof EntityThrowable && this.throwable.getValue()) || (entity instanceof EntityItem && this.items.getValue());
    }
}

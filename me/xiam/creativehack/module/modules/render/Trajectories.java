// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.render;

import net.minecraft.entity.Entity;
import java.util.Iterator;
import net.minecraft.util.math.BlockPos;
import me.xiam.creativehack.util.GeometryMasks;
import me.xiam.creativehack.util.KamiTessellator;
import org.lwjgl.opengl.GL11;
import me.xiam.creativehack.util.TrajectoryCalculator;
import net.minecraft.entity.EntityLivingBase;
import me.xiam.creativehack.event.events.RenderEvent;
import me.xiam.creativehack.util.HueCycler;
import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import me.xiam.creativehack.module.Module;

@Info(name = "Trajectories", category = Category.RENDER, description = "Draws lines to where trajectories are going to fall")
public class Trajectories extends Module
{
    ArrayList<Vec3d> positions;
    HueCycler cycler;
    
    public Trajectories() {
        this.positions = new ArrayList<Vec3d>();
        this.cycler = new HueCycler(100);
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        try {
            final TrajectoryCalculator.ThrowingType tt;
            TrajectoryCalculator.FlightPath flightPath;
            BlockPos hit;
            Vec3d a;
            final Iterator<Vec3d> iterator;
            Vec3d v;
            Trajectories.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityLivingBase).map(entity -> entity).forEach(entity -> {
                this.positions.clear();
                tt = TrajectoryCalculator.getThrowType(entity);
                if (tt != TrajectoryCalculator.ThrowingType.NONE) {
                    flightPath = new TrajectoryCalculator.FlightPath(entity, tt);
                    while (!flightPath.isCollided()) {
                        flightPath.onUpdate();
                        this.positions.add(flightPath.position);
                    }
                    hit = null;
                    if (flightPath.getCollidingTarget() != null) {
                        hit = flightPath.getCollidingTarget().getBlockPos();
                    }
                    GL11.glEnable(3042);
                    GL11.glDisable(3553);
                    GL11.glDisable(2896);
                    GL11.glDisable(2929);
                    if (hit != null) {
                        KamiTessellator.prepare(7);
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);
                        KamiTessellator.drawBox(hit, 872415231, GeometryMasks.FACEMAP.get(flightPath.getCollidingTarget().sideHit));
                        KamiTessellator.release();
                    }
                    if (!this.positions.isEmpty()) {
                        GL11.glDisable(3042);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glLineWidth(2.0f);
                        if (hit != null) {
                            GL11.glColor3f(1.0f, 1.0f, 1.0f);
                        }
                        else {
                            this.cycler.setNext();
                        }
                        GL11.glBegin(1);
                        a = this.positions.get(0);
                        GL11.glVertex3d(a.x - Trajectories.mc.getRenderManager().renderPosX, a.y - Trajectories.mc.getRenderManager().renderPosY, a.z - Trajectories.mc.getRenderManager().renderPosZ);
                        this.positions.iterator();
                        while (iterator.hasNext()) {
                            v = iterator.next();
                            GL11.glVertex3d(v.x - Trajectories.mc.getRenderManager().renderPosX, v.y - Trajectories.mc.getRenderManager().renderPosY, v.z - Trajectories.mc.getRenderManager().renderPosZ);
                            GL11.glVertex3d(v.x - Trajectories.mc.getRenderManager().renderPosX, v.y - Trajectories.mc.getRenderManager().renderPosY, v.z - Trajectories.mc.getRenderManager().renderPosZ);
                            if (hit == null) {
                                this.cycler.setNext();
                            }
                        }
                        GL11.glEnd();
                        GL11.glEnable(3042);
                        GL11.glEnable(3553);
                        this.cycler.reset();
                    }
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

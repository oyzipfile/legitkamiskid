// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.util;

import net.minecraft.util.math.BlockPos;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.util.math.Vec3d;

public class VectorUtil
{
    public static double getDistance(final Vec3d vecA, final Vec3d vecB) {
        return Math.sqrt(Math.pow(vecA.x - vecB.x, 2.0) + Math.pow(vecA.y - vecB.y, 2.0) + Math.pow(vecA.z - vecB.z, 2.0));
    }
    
    public static ArrayList<Vec3d> extendVec(final Vec3d startVec, final Vec3d destinationVec, final int steps) {
        final ArrayList<Vec3d> returnList = new ArrayList<Vec3d>(steps + 1);
        final double stepDistance = getDistance(startVec, destinationVec) / steps;
        for (int i = 0; i < Math.max(steps, 1) + 1; ++i) {
            returnList.add(advanceVec(startVec, destinationVec, stepDistance * i));
        }
        return returnList;
    }
    
    public static Vec3d advanceVec(final Vec3d startVec, final Vec3d destinationVec, final double distance) {
        final Vec3d advanceDirection = destinationVec.subtract(startVec).normalize();
        if (destinationVec.distanceTo(startVec) < distance) {
            return destinationVec;
        }
        return advanceDirection.scale(distance);
    }
    
    public static List<BlockPos> getBlockPositionsInArea(final Vec3d pos1, final Vec3d pos2) {
        final int minX = (int)Math.round(Math.min(pos1.x, pos2.x));
        final int maxX = (int)Math.round(Math.max(pos1.x, pos2.x));
        final int minY = (int)Math.round(Math.min(pos1.y, pos2.y));
        final int maxY = (int)Math.round(Math.max(pos1.y, pos2.y));
        final int minZ = (int)Math.round(Math.min(pos1.z, pos2.z));
        final int maxZ = (int)Math.round(Math.max(pos1.z, pos2.z));
        return getBlockPos(minX, maxX, minY, maxY, minZ, maxZ);
    }
    
    public static List<BlockPos> getBlockPositionsInArea(final BlockPos pos1, final BlockPos pos2) {
        final int minX = Math.min(pos1.x, pos2.x);
        final int maxX = Math.max(pos1.x, pos2.x);
        final int minY = Math.min(pos1.y, pos2.y);
        final int maxY = Math.max(pos1.y, pos2.y);
        final int minZ = Math.min(pos1.z, pos2.z);
        final int maxZ = Math.max(pos1.z, pos2.z);
        return getBlockPos(minX, maxX, minY, maxY, minZ, maxZ);
    }
    
    private static List<BlockPos> getBlockPos(final int minX, final int maxX, final int minY, final int maxY, final int minZ, final int maxZ) {
        final ArrayList<BlockPos> returnList = new ArrayList<BlockPos>((maxX - minX) * (maxY - minY) * (maxZ - minZ));
        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    returnList.add(new BlockPos(x, y, z));
                }
            }
        }
        return returnList;
    }
}

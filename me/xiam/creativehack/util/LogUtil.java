// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.util;

import java.io.IOException;
import java.io.FileWriter;
import net.minecraft.client.Minecraft;

public class LogUtil
{
    public static int[] getCurrentCoord(final boolean chunk) {
        final Minecraft mc = Minecraft.getMinecraft();
        final int[] currentCoords = { (int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ };
        if (chunk) {
            final int[] chunkCoords = { currentCoords[0] / 16, currentCoords[1] / 16, currentCoords[2] / 16 };
            return chunkCoords;
        }
        return currentCoords;
    }
    
    public static void writePlayerCoords(final String locationName, final boolean chunk) {
        writeCoords(getCurrentCoord(chunk), "chunk: " + chunk + ", " + locationName);
    }
    
    public static void writeCoords(final int[] xyz, final String locationName) {
        try {
            final FileWriter fW = new FileWriter("KAMIBlueCoords.txt", true);
            fW.write(formatter(xyz[0], xyz[1], xyz[2], locationName));
            fW.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String formatter(final int x, final int y, final int z, final String locationName) {
        return x + ", " + y + ", " + z + ", " + locationName + "\n";
    }
}

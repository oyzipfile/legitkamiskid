// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.hidden;

import java.util.Date;
import java.text.SimpleDateFormat;
import me.xiam.creativehack.util.LogUtil;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "CoordsLog", description = "Automatically writes the coordinates of the player to a file with a user defined delay between logs.", category = Category.HIDDEN, showOnArray = ShowOnArray.OFF)
public class CoordsLog extends Module
{
    private Setting<Double> delay;
    private Setting<Boolean> checkDuplicates;
    private Setting<Boolean> useChunkCoord;
    private int previousCoord;
    private static boolean playerIsDead;
    private static long startTime;
    
    public CoordsLog() {
        this.delay = this.register((Setting<Double>)Settings.doubleBuilder("Time between logs").withMinimum(1.0).withValue(15.0).withMaximum(60.0).build());
        this.checkDuplicates = this.register(Settings.b("Don't log same coord 2 times in a row", true));
        this.useChunkCoord = this.register(Settings.b("Use chunk coordinate", true));
    }
    
    @Override
    public void onUpdate() {
        if (CoordsLog.mc.player == null) {
            return;
        }
        this.timeout();
        if (!CoordsLog.playerIsDead && 0.0f >= CoordsLog.mc.player.getHealth()) {
            this.logCoordinates();
            CoordsLog.playerIsDead = true;
        }
    }
    
    private void timeout() {
        if (CoordsLog.startTime == 0L) {
            CoordsLog.startTime = System.currentTimeMillis();
        }
        if (CoordsLog.startTime + this.delay.getValue() * 1000.0 <= System.currentTimeMillis()) {
            CoordsLog.startTime = System.currentTimeMillis();
            final int[] cCArray = LogUtil.getCurrentCoord(this.useChunkCoord.getValue());
            final int currentCoord = cCArray[0] * 3 + cCArray[1] * 32 + cCArray[2] / 2;
            if (!this.checkDuplicates.getValue()) {
                this.logCoordinates();
                this.previousCoord = currentCoord;
                return;
            }
            if (currentCoord != this.previousCoord) {
                this.logCoordinates();
                this.previousCoord = currentCoord;
            }
        }
    }
    
    private void logCoordinates() {
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        final String time = sdf.format(new Date());
        LogUtil.writePlayerCoords(time, this.useChunkCoord.getValue());
    }
    
    public void onDisable() {
        CoordsLog.startTime = 0L;
    }
    
    static {
        CoordsLog.playerIsDead = false;
        CoordsLog.startTime = 0L;
    }
}

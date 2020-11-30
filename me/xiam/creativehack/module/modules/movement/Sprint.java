// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.movement;

import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.module.Module;

@Info(name = "Sprint", description = "Automatically makes the player sprint", category = Category.MOVEMENT, showOnArray = ShowOnArray.OFF)
public class Sprint extends Module
{
    @Override
    public void onUpdate() {
        if (Sprint.mc.player == null) {
            return;
        }
        if (KamiMod.MODULE_MANAGER.getModule(ElytraFlight.class).isEnabled() && (Sprint.mc.player.isElytraFlying() || Sprint.mc.player.capabilities.isFlying)) {
            return;
        }
        try {
            if (!Sprint.mc.player.collidedHorizontally && Sprint.mc.player.moveForward > 0.0f) {
                Sprint.mc.player.setSprinting(true);
            }
            else {
                Sprint.mc.player.setSprinting(false);
            }
        }
        catch (Exception ex) {}
    }
}

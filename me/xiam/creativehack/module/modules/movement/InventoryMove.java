// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.movement;

import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiChat;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "InventoryMove", description = "Allows you to walk around with GUIs opened", category = Category.MOVEMENT)
public class InventoryMove extends Module
{
    private Setting<Integer> speed;
    public Setting<Boolean> sneak;
    
    public InventoryMove() {
        this.speed = this.register(Settings.i("Look speed", 10));
        this.sneak = this.register(Settings.b("Sneak", false));
    }
    
    @Override
    public void onUpdate() {
        if (InventoryMove.mc.player == null || InventoryMove.mc.currentScreen == null || InventoryMove.mc.currentScreen instanceof GuiChat) {
            return;
        }
        if (Keyboard.isKeyDown(203)) {
            InventoryMove.mc.player.rotationYaw -= this.speed.getValue();
        }
        if (Keyboard.isKeyDown(205)) {
            InventoryMove.mc.player.rotationYaw += this.speed.getValue();
        }
        if (Keyboard.isKeyDown(200)) {
            InventoryMove.mc.player.rotationPitch -= this.speed.getValue();
        }
        if (Keyboard.isKeyDown(208)) {
            InventoryMove.mc.player.rotationPitch += this.speed.getValue();
        }
    }
}

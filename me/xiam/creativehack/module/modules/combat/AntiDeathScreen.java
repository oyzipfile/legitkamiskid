// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.combat;

import java.util.function.Predicate;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiGameOver;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.GuiScreenEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.module.Module;

@Info(name = "AntiDeathScreen", description = "Fixes random death screen glitches", category = Category.COMBAT)
public class AntiDeathScreen extends Module
{
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> listener;
    
    public AntiDeathScreen() {
        this.listener = new Listener<GuiScreenEvent.Displayed>(event -> {
            if (!(!(event.getScreen() instanceof GuiGameOver))) {
                if (AntiDeathScreen.mc.player.getHealth() > 0.0f) {
                    AntiDeathScreen.mc.player.respawnPlayer();
                    AntiDeathScreen.mc.displayGuiScreen((GuiScreen)null);
                }
            }
        }, (Predicate<GuiScreenEvent.Displayed>[])new Predicate[0]);
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.misc;

import java.util.function.Predicate;
import net.minecraft.client.gui.GuiScreen;
import me.xiam.creativehack.util.MessageSendHelper;
import net.minecraft.client.gui.GuiGameOver;
import me.xiam.creativehack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.GuiScreenEvent;
import me.zero.alpine.listener.Listener;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AutoRespawn", description = "Automatically respawn after dying", category = Category.MISC)
public class AutoRespawn extends Module
{
    private Setting<Boolean> respawn;
    private Setting<Boolean> deathCoords;
    private Setting<Boolean> antiGlitchScreen;
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> listener;
    
    public AutoRespawn() {
        this.respawn = this.register(Settings.b("Respawn", true));
        this.deathCoords = this.register(Settings.b("DeathCoords", true));
        this.antiGlitchScreen = this.register(Settings.b("Anti Glitch Screen", true));
        this.listener = new Listener<GuiScreenEvent.Displayed>(event -> {
            if (!(!(event.getScreen() instanceof GuiGameOver))) {
                if (this.deathCoords.getValue() && AutoRespawn.mc.player.getHealth() <= 0.0f) {
                    MessageSendHelper.sendChatMessage(String.format("You died at x %d y %d z %d", (int)AutoRespawn.mc.player.posX, (int)AutoRespawn.mc.player.posY, (int)AutoRespawn.mc.player.posZ));
                }
                if (this.respawn.getValue() || (this.antiGlitchScreen.getValue() && AutoRespawn.mc.player.getHealth() > 0.0f)) {
                    AutoRespawn.mc.player.respawnPlayer();
                    AutoRespawn.mc.displayGuiScreen((GuiScreen)null);
                }
            }
        }, (Predicate<GuiScreenEvent.Displayed>[])new Predicate[0]);
    }
}

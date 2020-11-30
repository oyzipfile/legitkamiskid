// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.combat;

import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.command.Command;
import net.minecraft.client.gui.GuiGameOver;
import java.util.function.Predicate;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.event.events.GuiScreenEvent;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.player.EntityPlayer;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AutoEZ", category = Category.COMBAT, description = "Sends an insult in chat after killing someone")
public class AutoEZ extends Module
{
    public Setting<Mode> mode;
    public Setting<String> customText;
    EntityPlayer focus;
    int hasBeenCombat;
    @EventHandler
    public Listener<AttackEntityEvent> livingDeathEventListener;
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> listener;
    private static long startTime;
    
    public AutoEZ() {
        this.mode = this.register(Settings.e("Mode", Mode.ONTOP));
        this.customText = this.register(Settings.stringBuilder("Custom Text").withValue("unchanged").withConsumer((old, value) -> {}).build());
        this.livingDeathEventListener = new Listener<AttackEntityEvent>(event -> {
            if (event.getTarget() instanceof EntityPlayer) {
                this.focus = (EntityPlayer)event.getTarget();
                if (event.getEntityPlayer().getUniqueID() == AutoEZ.mc.player.getUniqueID()) {
                    if (this.focus.getHealth() <= 0.0 || this.focus.isDead || !AutoEZ.mc.world.playerEntities.contains(this.focus)) {
                        AutoEZ.mc.player.sendChatMessage(this.getText(this.mode.getValue(), event.getTarget().getName()));
                    }
                    else {
                        this.hasBeenCombat = 1000;
                    }
                }
            }
            return;
        }, (Predicate<AttackEntityEvent>[])new Predicate[0]);
        this.listener = new Listener<GuiScreenEvent.Displayed>(event -> {
            if (!(!(event.getScreen() instanceof GuiGameOver))) {
                if (AutoEZ.mc.player.getHealth() > 0.0f) {
                    this.hasBeenCombat = 0;
                }
            }
        }, (Predicate<GuiScreenEvent.Displayed>[])new Predicate[0]);
    }
    
    private String getText(final Mode m, final String playerName) {
        if (m.equals(Mode.CUSTOM)) {
            return this.customText.getValue().replace("$NAME", playerName);
        }
        return m.text.replace("$NAME", playerName);
    }
    
    @Override
    public void onUpdate() {
        if (AutoEZ.mc.player == null) {
            return;
        }
        if (this.hasBeenCombat > 0 && (this.focus.getHealth() <= 0.0f || this.focus.isDead || !AutoEZ.mc.world.playerEntities.contains(this.focus))) {
            AutoEZ.mc.player.sendChatMessage(this.getText(this.mode.getValue(), this.focus.getName()));
            this.hasBeenCombat = 0;
        }
        --this.hasBeenCombat;
        if (AutoEZ.startTime == 0L) {
            AutoEZ.startTime = System.currentTimeMillis();
        }
        if (AutoEZ.startTime + 5000L <= System.currentTimeMillis()) {
            if (this.mode.getValue().equals(Mode.CUSTOM) && this.customText.getValue().equalsIgnoreCase("unchanged") && AutoEZ.mc.player != null) {
                MessageSendHelper.sendWarningMessage(this.getChatName() + " Warning: In order to use the custom " + this.getName() + ", please run the &7" + Command.getCommandPrefix() + "autoez&r command to change it, with '&7$NAME&f' being the username of the killed player");
            }
            AutoEZ.startTime = System.currentTimeMillis();
        }
    }
    
    static {
        AutoEZ.startTime = 0L;
    }
    
    public enum Mode
    {
        GG("gg, $NAME"), 
        ONTOP("KAMI BLUE on top! ez $NAME"), 
        EZD("You just got ez'd $NAME"), 
        EZ_HYPIXEL("E Z Win $NAME"), 
        NAENAE("You just got naenae'd by kami blue plus, $NAME"), 
        CUSTOM;
        
        private String text;
        
        private Mode(final String text) {
            this.text = text;
        }
    }
}

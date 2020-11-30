// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.misc;

import me.xiam.creativehack.setting.Settings;
import net.minecraft.world.GameType;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "Creative", description = "Make sure to have .vanish on", category = Category.MISC)
public class FakeGamemode extends Module
{
    private Setting<GamemodeChanged> gamemode;
    private GameType gameType;
    
    public FakeGamemode() {
        this.gamemode = this.register(Settings.e("Mode", GamemodeChanged.CREATIVE));
    }
    
    @Override
    public void onUpdate() {
        if (FakeGamemode.mc.player == null) {
            return;
        }
        if (this.gamemode.getValue().equals(GamemodeChanged.CREATIVE)) {
            FakeGamemode.mc.playerController.setGameType(this.gameType);
            FakeGamemode.mc.playerController.setGameType(GameType.CREATIVE);
        }
        else if (this.gamemode.getValue().equals(GamemodeChanged.SURVIVAL)) {
            FakeGamemode.mc.playerController.setGameType(this.gameType);
            FakeGamemode.mc.playerController.setGameType(GameType.SURVIVAL);
        }
        else if (this.gamemode.getValue().equals(GamemodeChanged.ADVENTURE)) {
            FakeGamemode.mc.playerController.setGameType(this.gameType);
            FakeGamemode.mc.playerController.setGameType(GameType.ADVENTURE);
        }
        else if (this.gamemode.getValue().equals(GamemodeChanged.SPECTATOR)) {
            FakeGamemode.mc.playerController.setGameType(this.gameType);
            FakeGamemode.mc.playerController.setGameType(GameType.SPECTATOR);
        }
    }
    
    public void onEnable() {
        if (FakeGamemode.mc.player == null) {
            this.disable();
        }
        else {
            this.gameType = FakeGamemode.mc.playerController.getCurrentGameType();
        }
    }
    
    public void onDisable() {
        if (FakeGamemode.mc.player == null) {
            return;
        }
        FakeGamemode.mc.playerController.setGameType(this.gameType);
    }
    
    private enum GamemodeChanged
    {
        SURVIVAL, 
        CREATIVE, 
        ADVENTURE, 
        SPECTATOR;
    }
}

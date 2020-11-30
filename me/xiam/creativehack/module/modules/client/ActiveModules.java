// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.client;

import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.KamiMod;
import java.awt.Color;
import me.xiam.creativehack.util.ColourConverter;
import net.minecraft.util.text.TextFormatting;
import me.xiam.creativehack.util.ColourTextFormatting;
import me.xiam.creativehack.util.InfoCalculator;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "ActiveModules", category = Category.CLIENT, description = "Configures ActiveModules colours and modes", showOnArray = ShowOnArray.OFF)
public class ActiveModules extends Module
{
    private Setting<Boolean> forgeHax;
    public Setting<Mode> mode;
    private Setting<Integer> rainbowSpeed;
    public Setting<Integer> saturationR;
    public Setting<Integer> brightnessR;
    public Setting<Integer> hueC;
    public Setting<Integer> saturationC;
    public Setting<Integer> brightnessC;
    private Setting<Boolean> alternate;
    public Setting<String> chat;
    public Setting<String> combat;
    public Setting<String> experimental;
    public Setting<String> client;
    public Setting<String> render;
    public Setting<String> player;
    public Setting<String> movement;
    public Setting<String> misc;
    
    public ActiveModules() {
        this.forgeHax = this.register(Settings.b("ForgeHax", false));
        this.mode = this.register(Settings.e("Mode", Mode.RAINBOW));
        this.rainbowSpeed = this.register(Settings.integerBuilder().withName("Speed R").withValue(30).withMinimum(0).withMaximum(100).withVisibility(v -> this.mode.getValue().equals(Mode.RAINBOW)).build());
        this.saturationR = this.register(Settings.integerBuilder().withName("Saturation R").withValue(117).withMinimum(0).withMaximum(255).withVisibility(v -> this.mode.getValue().equals(Mode.RAINBOW)).build());
        this.brightnessR = this.register(Settings.integerBuilder().withName("Brightness R").withValue(255).withMinimum(0).withMaximum(255).withVisibility(v -> this.mode.getValue().equals(Mode.RAINBOW)).build());
        this.hueC = this.register(Settings.integerBuilder().withName("Hue C").withValue(178).withMinimum(0).withMaximum(255).withVisibility(v -> this.mode.getValue().equals(Mode.CUSTOM)).build());
        this.saturationC = this.register(Settings.integerBuilder().withName("Saturation C").withValue(156).withMinimum(0).withMaximum(255).withVisibility(v -> this.mode.getValue().equals(Mode.CUSTOM)).build());
        this.brightnessC = this.register(Settings.integerBuilder().withName("Brightness C").withValue(255).withMinimum(0).withMaximum(255).withVisibility(v -> this.mode.getValue().equals(Mode.CUSTOM)).build());
        this.alternate = this.register(Settings.booleanBuilder().withName("Alternate").withValue(true).withVisibility(v -> this.mode.getValue().equals(Mode.INFO_OVERLAY)).build());
        this.chat = this.register(Settings.s("Chat", "162,136,227"));
        this.combat = this.register(Settings.s("Combat", "229,68,109"));
        this.experimental = this.register(Settings.s("Experimental", "211,188,192"));
        this.client = this.register(Settings.s("Client", "56,2,59"));
        this.render = this.register(Settings.s("Render", "105,48,109"));
        this.player = this.register(Settings.s("Player", "255,137,102"));
        this.movement = this.register(Settings.s("Movement", "111,60,145"));
        this.misc = this.register(Settings.s("Misc", "165,102,139"));
    }
    
    private static int getRgb(final String input, final int arrayPos) {
        final String[] toConvert = input.split(",");
        return Integer.parseInt(toConvert[arrayPos]);
    }
    
    public int getInfoColour(final int position) {
        if (!this.alternate.getValue()) {
            return this.settingsToColour(false);
        }
        if (InfoCalculator.isNumberEven(position)) {
            return this.settingsToColour(true);
        }
        return this.settingsToColour(false);
    }
    
    private int settingsToColour(final boolean isOne) {
        Color localColor = null;
        switch (this.infoGetSetting(isOne)) {
            case UNDERLINE:
            case ITALIC:
            case RESET:
            case STRIKETHROUGH:
            case OBFUSCATED:
            case BOLD: {
                localColor = ColourTextFormatting.colourEnumMap.get(TextFormatting.WHITE).colorLocal;
                break;
            }
            default: {
                localColor = ColourTextFormatting.colourEnumMap.get(this.infoGetSetting(isOne)).colorLocal;
                break;
            }
        }
        return ColourConverter.rgbToInt(localColor.getRed(), localColor.getGreen(), localColor.getBlue());
    }
    
    private TextFormatting infoGetSetting(final boolean isOne) {
        final InfoOverlay infoOverlay = (InfoOverlay)KamiMod.MODULE_MANAGER.getModule(InfoOverlay.class);
        if (isOne) {
            return this.setToText(infoOverlay.firstColour.getValue());
        }
        return this.setToText(infoOverlay.secondColour.getValue());
    }
    
    private TextFormatting setToText(final ColourTextFormatting.ColourCode colourCode) {
        return ColourTextFormatting.toTextMap.get(colourCode);
    }
    
    public int getCategoryColour(final Module module) {
        switch (module.getCategory()) {
            case CHAT: {
                return ColourConverter.rgbToInt(getRgb(this.chat.getValue(), 0), getRgb(this.chat.getValue(), 1), getRgb(this.chat.getValue(), 2));
            }
            case COMBAT: {
                return ColourConverter.rgbToInt(getRgb(this.combat.getValue(), 0), getRgb(this.combat.getValue(), 1), getRgb(this.combat.getValue(), 2));
            }
            case EXPERIMENTAL: {
                return ColourConverter.rgbToInt(getRgb(this.experimental.getValue(), 0), getRgb(this.experimental.getValue(), 1), getRgb(this.experimental.getValue(), 2));
            }
            case CLIENT: {
                return ColourConverter.rgbToInt(getRgb(this.client.getValue(), 0), getRgb(this.client.getValue(), 1), getRgb(this.client.getValue(), 2));
            }
            case RENDER: {
                return ColourConverter.rgbToInt(getRgb(this.render.getValue(), 0), getRgb(this.render.getValue(), 1), getRgb(this.render.getValue(), 2));
            }
            case PLAYER: {
                return ColourConverter.rgbToInt(getRgb(this.player.getValue(), 0), getRgb(this.player.getValue(), 1), getRgb(this.player.getValue(), 2));
            }
            case MOVEMENT: {
                return ColourConverter.rgbToInt(getRgb(this.movement.getValue(), 0), getRgb(this.movement.getValue(), 1), getRgb(this.movement.getValue(), 2));
            }
            case MISC: {
                return ColourConverter.rgbToInt(getRgb(this.misc.getValue(), 0), getRgb(this.misc.getValue(), 1), getRgb(this.misc.getValue(), 2));
            }
            default: {
                return ColourConverter.rgbToInt(1, 1, 1);
            }
        }
    }
    
    public int getRainbowSpeed() {
        final int rSpeed = InfoCalculator.reverseNumber(this.rainbowSpeed.getValue(), 1, 100);
        if (rSpeed == 0) {
            return 1;
        }
        return rSpeed;
    }
    
    public String fHax() {
        if (this.forgeHax.getValue()) {
            return ">";
        }
        return "";
    }
    
    public void onDisable() {
        MessageSendHelper.sendDisableMessage(this.getClass());
    }
    
    public enum Mode
    {
        RAINBOW, 
        CUSTOM, 
        CATEGORY, 
        INFO_OVERLAY;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.chat;

import me.xiam.creativehack.util.InfoCalculator;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.xiam.creativehack.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.xiam.creativehack.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import java.util.Random;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "FancyChat", category = Category.CHAT, description = "Makes messages you send fancy", showOnArray = ShowOnArray.OFF)
public class FancyChat extends Module
{
    private Setting<Boolean> uwu;
    private Setting<Boolean> leet;
    private Setting<Boolean> mock;
    private Setting<Boolean> green;
    private Setting<Boolean> randomSetting;
    private Setting<Boolean> commands;
    private static Random random;
    @EventHandler
    public Listener<PacketEvent.Send> listener;
    
    public FancyChat() {
        this.uwu = this.register(Settings.b("uwu", true));
        this.leet = this.register(Settings.b("1337", false));
        this.mock = this.register(Settings.b("mOcK", false));
        this.green = this.register(Settings.b(">", false));
        this.randomSetting = this.register(Settings.booleanBuilder("Random Case").withValue(true).withVisibility(v -> this.mock.getValue()).build());
        this.commands = this.register(Settings.b("Commands", false));
        String s;
        String s2;
        this.listener = new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketChatMessage) {
                s = ((CPacketChatMessage)event.getPacket()).getMessage();
                if (this.commands.getValue() || !this.isCommand(s)) {
                    s2 = this.getText(s);
                    if (s2.length() >= 256) {
                        s2 = s2.substring(0, 256);
                    }
                    ((CPacketChatMessage)event.getPacket()).message = s2;
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    private String getText(String s) {
        if (this.uwu.getValue()) {
            s = this.uwuConverter(s);
        }
        if (this.leet.getValue()) {
            s = this.leetConverter(s);
        }
        if (this.mock.getValue()) {
            s = this.mockingConverter(s);
        }
        if (this.green.getValue()) {
            s = this.greenConverter(s);
        }
        return s;
    }
    
    private String greenConverter(final String input) {
        return "> " + input;
    }
    
    @Override
    public String getHudInfo() {
        final StringBuilder returned = new StringBuilder();
        if (this.uwu.getValue()) {
            returned.append("uwu");
        }
        if (this.leet.getValue()) {
            returned.append(" 1337");
        }
        if (this.mock.getValue()) {
            returned.append(" mOcK");
        }
        if (this.green.getValue()) {
            returned.append(" >");
        }
        return returned.toString();
    }
    
    private boolean isCommand(final String s) {
        for (final String value : CustomChat.cmdCheck) {
            if (s.startsWith(value)) {
                return true;
            }
        }
        return false;
    }
    
    private String leetConverter(final String input) {
        final StringBuilder message = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            String inputChar = input.charAt(i) + "";
            inputChar = inputChar.toLowerCase();
            inputChar = this.leetSwitch(inputChar);
            message.append(inputChar);
        }
        return message.toString();
    }
    
    private String mockingConverter(final String input) {
        final StringBuilder message = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            String inputChar = input.charAt(i) + "";
            int rand = 0;
            if (this.randomSetting.getValue()) {
                rand = (FancyChat.random.nextBoolean() ? 1 : 0);
            }
            if (!InfoCalculator.isNumberEven(i + rand)) {
                inputChar = inputChar.toUpperCase();
            }
            else {
                inputChar = inputChar.toLowerCase();
            }
            message.append(inputChar);
        }
        return message.toString();
    }
    
    private String uwuConverter(String input) {
        input = input.replace("ove", "uv");
        input = input.replace("the", "da");
        input = input.replace("is", "ish");
        input = input.replace("r", "w");
        input = input.replace("ve", "v");
        input = input.replace("l", "w");
        return input;
    }
    
    private String leetSwitch(final String i) {
        switch (i) {
            case "a": {
                return "4";
            }
            case "e": {
                return "3";
            }
            case "g": {
                return "6";
            }
            case "l":
            case "i": {
                return "1";
            }
            case "o": {
                return "0";
            }
            case "s": {
                return "$";
            }
            case "t": {
                return "7";
            }
            default: {
                return i;
            }
        }
    }
    
    static {
        FancyChat.random = new Random();
    }
}

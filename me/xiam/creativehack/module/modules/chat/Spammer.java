// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.chat;

import java.util.Random;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import me.xiam.creativehack.util.MessageSendHelper;
import java.util.ArrayList;
import me.xiam.creativehack.setting.Settings;
import java.util.List;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "Spammer", description = "Spams text from a file on a set delay into the chat", category = Category.CHAT)
public class Spammer extends Module
{
    private Setting<Integer> timeoutTime;
    List<String> tempLines;
    String[] spammer;
    private static long startTime;
    
    public Spammer() {
        this.timeoutTime = this.register((Setting<Integer>)Settings.integerBuilder().withName("Timeout (s)").withMinimum(1).withMaximum(240).withValue(10).build());
        this.tempLines = new ArrayList<String>();
    }
    
    public void onEnable() {
        try {
            MessageSendHelper.sendChatMessage(this.getChatName() + "Trying to find '&7spammer.txt&f'");
            final BufferedReader bufferedReader = new BufferedReader(new FileReader("spammer.txt"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                this.tempLines.add(line);
            }
            bufferedReader.close();
            this.spammer = this.tempLines.toArray(new String[0]);
        }
        catch (FileNotFoundException exception2) {
            MessageSendHelper.sendErrorMessage(this.getChatName() + "Couldn't find a file called '&7spammer.txt&f' inside your '&7.minecraft&f' folder, disabling");
            this.disable();
        }
        catch (IOException exception) {
            MessageSendHelper.sendErrorMessage(exception.toString());
        }
    }
    
    @Override
    public void onUpdate() {
        this.sendMsg(getRandom(this.spammer));
    }
    
    private void sendMsg(final String message) {
        if (Spammer.startTime == 0L) {
            Spammer.startTime = System.currentTimeMillis();
        }
        if (Spammer.startTime + this.timeoutTime.getValue() * 1000 <= System.currentTimeMillis()) {
            Spammer.startTime = System.currentTimeMillis();
            MessageSendHelper.sendServerMessage(message);
        }
    }
    
    public static String getRandom(final String[] array) {
        int rand;
        for (rand = new Random().nextInt(array.length); array[rand].isEmpty() || array[rand].equals(" "); rand = new Random().nextInt(array.length)) {}
        return array[rand];
    }
    
    static {
        Spammer.startTime = 0L;
    }
}

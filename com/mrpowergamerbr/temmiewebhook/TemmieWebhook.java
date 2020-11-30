// 
// Decompiled by Procyon v0.5.36
// 

package com.mrpowergamerbr.temmiewebhook;

import com.mrpowergamerbr.temmiewebhook.exceptions.WebhookException;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;

public class TemmieWebhook
{
    public static final Gson gson;
    public String url;
    boolean blockMainThread;
    
    public TemmieWebhook(final String url) {
        this(url, false);
    }
    
    public TemmieWebhook(final String url, final boolean blocking) {
        this.blockMainThread = false;
        this.url = url;
        this.blockMainThread = blocking;
    }
    
    public void sendMessage(final DiscordMessage dm) {
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                final String strResponse = HttpRequest.post(TemmieWebhook.this.url).acceptJson().contentType("application/json").header("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11").send(TemmieWebhook.gson.toJson((Object)dm)).body();
                if (!strResponse.isEmpty()) {
                    final Response response = (Response)TemmieWebhook.gson.fromJson(strResponse, (Class)Response.class);
                    try {
                        if (!response.getMessage().equals("You are being rate limited.")) {
                            throw new WebhookException(response.getMessage());
                        }
                        try {
                            Thread.sleep(response.getRetryAfter());
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        TemmieWebhook.this.sendMessage(dm);
                    }
                    catch (Exception e2) {
                        throw new WebhookException(strResponse);
                    }
                }
            }
        };
        if (this.blockMainThread) {
            r.run();
        }
        else {
            final Thread t = new Thread(r);
            t.start();
        }
    }
    
    static {
        gson = new Gson();
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package com.mrpowergamerbr.temmiewebhook;

import com.google.gson.annotations.SerializedName;

public class Response
{
    boolean global;
    String message;
    @SerializedName("retry_after")
    int retryAfter;
    
    public boolean isGlobal() {
        return this.global;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public int getRetryAfter() {
        return this.retryAfter;
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package com.mrpowergamerbr.temmiewebhook.embed;

import java.beans.ConstructorProperties;

public class ProviderEmbed
{
    String name;
    String url;
    
    public static ProviderEmbedBuilder builder() {
        return new ProviderEmbedBuilder();
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    @ConstructorProperties({ "name", "url" })
    public ProviderEmbed(final String name, final String url) {
        this.name = name;
        this.url = url;
    }
    
    public ProviderEmbed() {
    }
    
    public static class ProviderEmbedBuilder
    {
        private String name;
        private String url;
        
        ProviderEmbedBuilder() {
        }
        
        public ProviderEmbedBuilder name(final String name) {
            this.name = name;
            return this;
        }
        
        public ProviderEmbedBuilder url(final String url) {
            this.url = url;
            return this;
        }
        
        public ProviderEmbed build() {
            return new ProviderEmbed(this.name, this.url);
        }
        
        @Override
        public String toString() {
            return "ProviderEmbed.ProviderEmbedBuilder(name=" + this.name + ", url=" + this.url + ")";
        }
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.render;

import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.Entity;
import me.xiam.creativehack.util.MessageSendHelper;
import java.util.Iterator;
import java.util.Objects;
import me.xiam.creativehack.util.EntityUtil;
import java.util.HashMap;
import me.xiam.creativehack.setting.Settings;
import java.util.Map;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "MobOwner", description = "Displays the owner of tamed mobs", category = Category.RENDER)
public class MobOwner extends Module
{
    private Setting<Integer> requestTime;
    private Setting<Boolean> debug;
    private Map<String, String> cachedUUIDs;
    private int apiRequests;
    private String invalidText;
    private static long startTime;
    private static long startTime1;
    
    public MobOwner() {
        this.requestTime = this.register((Setting<Integer>)Settings.integerBuilder("Cache Reset").withMinimum(10).withValue(20).build());
        this.debug = this.register(Settings.b("Debug", true));
        this.cachedUUIDs = new HashMap<String, String>() {};
        this.apiRequests = 0;
        this.invalidText = "Offline or invalid UUID!";
    }
    
    private String getUsername(final String uuid) {
        for (final Map.Entry<String, String> entries : this.cachedUUIDs.entrySet()) {
            if (entries.getKey().equalsIgnoreCase(uuid)) {
                return entries.getValue();
            }
        }
        try {
            if (this.apiRequests > 10) {
                return "Too many API requests";
            }
            this.cachedUUIDs.put(uuid, Objects.requireNonNull(EntityUtil.getNameFromUUID(uuid)).replace("\"", ""));
            ++this.apiRequests;
        }
        catch (IllegalStateException illegal) {
            this.cachedUUIDs.put(uuid, this.invalidText);
        }
        for (final Map.Entry<String, String> entries : this.cachedUUIDs.entrySet()) {
            if (entries.getKey().equalsIgnoreCase(uuid)) {
                return entries.getValue();
            }
        }
        return this.invalidText;
    }
    
    private void resetCache() {
        if (MobOwner.startTime == 0L) {
            MobOwner.startTime = System.currentTimeMillis();
        }
        if (MobOwner.startTime + this.requestTime.getValue() * 1000 <= System.currentTimeMillis()) {
            MobOwner.startTime = System.currentTimeMillis();
            for (final Map.Entry<String, String> entries : this.cachedUUIDs.entrySet()) {
                if (entries.getKey().equalsIgnoreCase(this.invalidText)) {
                    this.cachedUUIDs.clear();
                    if (this.debug.getValue()) {
                        MessageSendHelper.sendChatMessage(this.getChatName() + "Reset cached UUIDs list!");
                    }
                }
            }
        }
    }
    
    private void resetRequests() {
        if (MobOwner.startTime1 == 0L) {
            MobOwner.startTime1 = System.currentTimeMillis();
        }
        if (MobOwner.startTime1 + 10000L <= System.currentTimeMillis()) {
            MobOwner.startTime1 = System.currentTimeMillis();
            if (this.apiRequests >= 2) {
                this.apiRequests = 0;
                if (this.debug.getValue()) {
                    MessageSendHelper.sendChatMessage(this.getChatName() + "Reset API requests counter!");
                }
            }
        }
    }
    
    @Override
    public void onUpdate() {
        this.resetRequests();
        this.resetCache();
        for (final Entity entity : MobOwner.mc.world.loadedEntityList) {
            if (entity instanceof EntityTameable) {
                final EntityTameable entityTameable = (EntityTameable)entity;
                if (entityTameable.isTamed() && entityTameable.getOwner() != null) {
                    entityTameable.setAlwaysRenderNameTag(true);
                    entityTameable.setCustomNameTag("Owner: " + entityTameable.getOwner().getDisplayName().getFormattedText());
                }
            }
            if (entity instanceof AbstractHorse) {
                final AbstractHorse abstractHorse = (AbstractHorse)entity;
                if (!abstractHorse.isTame()) {
                    continue;
                }
                if (abstractHorse.getOwnerUniqueId() == null) {
                    continue;
                }
                abstractHorse.setAlwaysRenderNameTag(true);
                abstractHorse.setCustomNameTag("Owner: " + this.getUsername(abstractHorse.getOwnerUniqueId().toString()));
            }
        }
    }
    
    public void onDisable() {
        this.cachedUUIDs.clear();
        for (final Entity entity : MobOwner.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityTameable) && !(entity instanceof AbstractHorse)) {
                continue;
            }
            try {
                entity.setAlwaysRenderNameTag(false);
            }
            catch (Exception ex) {}
        }
    }
    
    static {
        MobOwner.startTime = 0L;
        MobOwner.startTime1 = 0L;
    }
}

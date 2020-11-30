// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.misc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import me.xiam.creativehack.util.MessageSendHelper;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.Entity;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "AutoNametag", description = "Automatically nametags entities", category = Category.MISC)
public class AutoNametag extends Module
{
    private Setting<Mode> modeSetting;
    private Setting<Float> range;
    private Setting<Boolean> debug;
    private String currentName;
    private int currentSlot;
    
    public AutoNametag() {
        this.modeSetting = this.register(Settings.e("Mode", Mode.ANY));
        this.range = this.register((Setting<Float>)Settings.floatBuilder("Range").withMinimum(2.0f).withValue(3.5f).withMaximum(10.0f).build());
        this.debug = this.register(Settings.b("Debug", false));
        this.currentName = "";
        this.currentSlot = -1;
    }
    
    @Override
    public void onUpdate() {
        this.findNameTags();
        this.useNameTag();
    }
    
    private void useNameTag() {
        final int originalSlot = AutoNametag.mc.player.inventory.currentItem;
        for (final Entity w : AutoNametag.mc.world.getLoadedEntityList()) {
            switch (this.modeSetting.getValue()) {
                case WITHER: {
                    if (w instanceof EntityWither && !w.getDisplayName().getUnformattedText().equals(this.currentName) && AutoNametag.mc.player.getDistance(w) <= this.range.getValue()) {
                        if (this.debug.getValue()) {
                            MessageSendHelper.sendChatMessage("Found unnamed " + w.getDisplayName().getUnformattedText());
                        }
                        this.selectNameTags();
                        AutoNametag.mc.playerController.interactWithEntity((EntityPlayer)AutoNametag.mc.player, w, EnumHand.MAIN_HAND);
                        continue;
                    }
                    continue;
                }
                case ANY: {
                    if ((w instanceof EntityMob || w instanceof EntityAnimal) && !w.getDisplayName().getUnformattedText().equals(this.currentName) && AutoNametag.mc.player.getDistance(w) <= this.range.getValue()) {
                        if (this.debug.getValue()) {
                            MessageSendHelper.sendChatMessage("Found unnamed " + w.getDisplayName().getUnformattedText());
                        }
                        this.selectNameTags();
                        AutoNametag.mc.playerController.interactWithEntity((EntityPlayer)AutoNametag.mc.player, w, EnumHand.MAIN_HAND);
                        continue;
                    }
                    continue;
                }
            }
        }
        AutoNametag.mc.player.inventory.currentItem = originalSlot;
    }
    
    private void selectNameTags() {
        if (this.currentSlot == -1 || !this.isNametag(this.currentSlot)) {
            MessageSendHelper.sendErrorMessage(this.getChatName() + "Error: No nametags in hotbar");
            this.disable();
            return;
        }
        AutoNametag.mc.player.inventory.currentItem = this.currentSlot;
    }
    
    private void findNameTags() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoNametag.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (!(stack.getItem() instanceof ItemBlock)) {
                    if (this.isNametag(i)) {
                        this.currentName = stack.getDisplayName();
                        this.currentSlot = i;
                    }
                }
            }
        }
    }
    
    private boolean isNametag(final int i) {
        final ItemStack stack = AutoNametag.mc.player.inventory.getStackInSlot(i);
        final Item tag = stack.getItem();
        return tag instanceof ItemNameTag && !stack.getDisplayName().equals("Name Tag");
    }
    
    private enum Mode
    {
        WITHER, 
        ANY;
    }
}

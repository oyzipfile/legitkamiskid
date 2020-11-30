// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import java.math.RoundingMode;
import java.math.BigDecimal;
import net.minecraft.entity.EntityLivingBase;
import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.util.EntityUtil;
import net.minecraft.entity.passive.AbstractHorse;
import me.xiam.creativehack.command.syntax.SyntaxChunk;
import me.xiam.creativehack.command.Command;

public class EntityStatsCommand extends Command
{
    public EntityStatsCommand() {
        super("entitystats", null, new String[] { "estats" });
        this.setDescription("Print the statistics of the entity you're currently riding");
    }
    
    @Override
    public void call(final String[] args) {
        if (this.mc.player.getRidingEntity() != null && this.mc.player.getRidingEntity() instanceof AbstractHorse) {
            final AbstractHorse horse = (AbstractHorse)this.mc.player.getRidingEntity();
            final float maxHealth = horse.getMaxHealth();
            final double speed = round(43.17 * horse.getAIMoveSpeed(), 2);
            final double jump = round(-0.1817584952 * Math.pow(horse.getHorseJumpStrength(), 3.0) + 3.689713992 * Math.pow(horse.getHorseJumpStrength(), 2.0) + 2.128599134 * horse.getHorseJumpStrength() - 0.343930367, 4);
            final String ownerId = (horse.getOwnerUniqueId() == null) ? "Not tamed." : horse.getOwnerUniqueId().toString();
            final String builder = "&6Entity Statistics:\n&cMax Health: " + maxHealth + "\n&cSpeed: " + speed + "\n&cJump: " + jump + "\n&cOwner: " + EntityUtil.getNameFromUUID(ownerId).replace("\"", "");
            MessageSendHelper.sendChatMessage(builder);
        }
        else if (this.mc.player.getRidingEntity() instanceof EntityLivingBase) {
            final EntityLivingBase entity = (EntityLivingBase)this.mc.player.getRidingEntity();
            MessageSendHelper.sendChatMessage("&6Entity Stats:\n&cMax Health: &b" + entity.getMaxHealth() + " &2HP\n&cSpeed: &b" + round(43.17 * entity.getAIMoveSpeed(), 2) + " &2m/s");
        }
        else {
            MessageSendHelper.sendChatMessage("&4&lError: &cNot riding a compatible entity.");
        }
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

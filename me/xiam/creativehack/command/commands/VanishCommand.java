// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.command.syntax.SyntaxChunk;
import net.minecraft.entity.Entity;
import me.xiam.creativehack.command.Command;

public class VanishCommand extends Command
{
    private static Entity vehicle;
    
    public VanishCommand() {
        super("vanish", null, new String[0]);
        this.setDescription("Allows you to vanish using an entity");
    }
    
    @Override
    public void call(final String[] args) {
        if (this.mc.player.getRidingEntity() != null && VanishCommand.vehicle == null) {
            VanishCommand.vehicle = this.mc.player.getRidingEntity();
            this.mc.player.dismountRidingEntity();
            this.mc.world.removeEntityFromWorld(VanishCommand.vehicle.getEntityId());
            MessageSendHelper.sendChatMessage("Vehicle " + VanishCommand.vehicle.getName() + " removed.");
        }
        else if (VanishCommand.vehicle != null) {
            VanishCommand.vehicle.isDead = false;
            this.mc.world.addEntityToWorld(VanishCommand.vehicle.getEntityId(), VanishCommand.vehicle);
            this.mc.player.startRiding(VanishCommand.vehicle, true);
            MessageSendHelper.sendChatMessage("Vehicle " + VanishCommand.vehicle.getName() + " created.");
            VanishCommand.vehicle = null;
        }
        else {
            MessageSendHelper.sendChatMessage("No Vehicle.");
        }
    }
}

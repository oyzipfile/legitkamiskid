// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.command.commands;

import me.xiam.creativehack.util.MessageSendHelper;
import me.xiam.creativehack.module.modules.render.Pathfind;
import net.minecraft.pathfinding.PathPoint;
import me.xiam.creativehack.command.syntax.ChunkBuilder;
import me.xiam.creativehack.command.Command;

public class PathCommand extends Command
{
    int x;
    int y;
    int z;
    
    public PathCommand() {
        super("path", new ChunkBuilder().append("x").append("y").append("z").build(), new String[0]);
        this.x = Integer.MIN_VALUE;
        this.y = Integer.MIN_VALUE;
        this.z = Integer.MIN_VALUE;
        this.setDescription("Pathfinding for AutoWalk");
    }
    
    @Override
    public void call(final String[] args) {
        if (args[0] != null && args[0].equalsIgnoreCase("retry")) {
            if (this.x != Integer.MIN_VALUE) {
                final PathPoint end = new PathPoint(this.x, this.y, this.z);
                Pathfind.createPath(end);
                if (!Pathfind.points.isEmpty()) {
                    MessageSendHelper.sendChatMessage("Path created!");
                }
                return;
            }
            MessageSendHelper.sendChatMessage("No location to retry pathfinding to.");
        }
        else {
            if (args.length <= 3) {
                MessageSendHelper.sendChatMessage("&cMissing arguments: x, y, z");
                return;
            }
            try {
                this.x = Integer.parseInt(args[0]);
                this.y = Integer.parseInt(args[1]);
                this.z = Integer.parseInt(args[2]);
                final PathPoint end = new PathPoint(this.x, this.y, this.z);
                Pathfind.createPath(end);
                if (!Pathfind.points.isEmpty()) {
                    MessageSendHelper.sendChatMessage("Path created!");
                }
            }
            catch (NumberFormatException e) {
                MessageSendHelper.sendChatMessage("Error: input must be numerical");
            }
        }
    }
}

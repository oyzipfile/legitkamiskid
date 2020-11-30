// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.event;

import me.xiam.creativehack.gui.rgui.component.Component;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import me.xiam.creativehack.util.MessageSendHelper;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraft.client.gui.GuiChat;
import me.xiam.creativehack.command.Command;
import me.xiam.creativehack.module.modules.client.CommandConfig;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import me.xiam.creativehack.util.KamiTessellator;
import me.xiam.creativehack.gui.UIRenderer;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.passive.AbstractHorse;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.modules.render.BossStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import me.xiam.creativehack.gui.rgui.component.container.Container;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.IInventory;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import me.xiam.creativehack.util.Wrapper;
import net.minecraft.client.gui.ScaledResolution;
import me.xiam.creativehack.command.commands.PeekCommand;
import me.xiam.creativehack.gui.kami.KamiGUI;
import me.xiam.creativehack.gui.rgui.component.container.use.Frame;
import me.xiam.creativehack.event.events.DisplaySizeChangedEvent;
import me.xiam.creativehack.KamiMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.living.LivingEvent;

public class ForgeEventProcessor
{
    private int displayWidth;
    private int displayHeight;
    
    @SubscribeEvent
    public void onUpdate(final LivingEvent.LivingUpdateEvent event) {
        if (event.isCanceled()) {
            return;
        }
        if (Minecraft.getMinecraft().displayWidth != this.displayWidth || Minecraft.getMinecraft().displayHeight != this.displayHeight) {
            KamiMod.EVENT_BUS.post(new DisplaySizeChangedEvent());
            this.displayWidth = Minecraft.getMinecraft().displayWidth;
            this.displayHeight = Minecraft.getMinecraft().displayHeight;
            KamiMod.getInstance().getGuiManager().getChildren().stream().filter(component -> component instanceof Frame).forEach(component -> KamiGUI.dock(component));
        }
        if (PeekCommand.sb != null) {
            final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            final int i = scaledresolution.getScaledWidth();
            final int j = scaledresolution.getScaledHeight();
            final GuiShulkerBox gui = new GuiShulkerBox(Wrapper.getPlayer().inventory, (IInventory)PeekCommand.sb);
            gui.setWorldAndResolution(Wrapper.getMinecraft(), i, j);
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)gui);
            PeekCommand.sb = null;
        }
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (Wrapper.getPlayer() == null) {
            return;
        }
        KamiMod.MODULE_MANAGER.onUpdate();
        KamiMod.getInstance().getGuiManager().callTick(KamiMod.getInstance().getGuiManager());
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        KamiMod.MODULE_MANAGER.onWorldRender(event);
    }
    
    @SubscribeEvent
    public void onRenderPre(final RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && KamiMod.MODULE_MANAGER.isModuleEnabled(BossStack.class)) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (event.isCanceled()) {
            return;
        }
        RenderGameOverlayEvent.ElementType target = RenderGameOverlayEvent.ElementType.EXPERIENCE;
        if (!Wrapper.getPlayer().isCreative() && Wrapper.getPlayer().getRidingEntity() instanceof AbstractHorse) {
            target = RenderGameOverlayEvent.ElementType.HEALTHMOUNT;
        }
        if (event.getType() == target) {
            KamiMod.MODULE_MANAGER.onRender();
            GL11.glPushMatrix();
            UIRenderer.renderAndUpdateFrames();
            GL11.glPopMatrix();
            KamiTessellator.releaseGL();
        }
        else if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && KamiMod.MODULE_MANAGER.isModuleEnabled(BossStack.class)) {
            BossStack.render(event);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState()) {
            return;
        }
        final CommandConfig commandConfig = KamiMod.MODULE_MANAGER.getModuleT(CommandConfig.class);
        if (commandConfig.prefixChat.getValue() && ("" + Keyboard.getEventCharacter()).equalsIgnoreCase(Command.getCommandPrefix()) && !Minecraft.getMinecraft().player.isSneaking()) {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new GuiChat(Command.getCommandPrefix()));
        }
        else {
            KamiMod.MODULE_MANAGER.onBind(Keyboard.getEventKey());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatSent(final ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.setCanceled(true);
            try {
                Wrapper.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                if (event.getMessage().length() > 1) {
                    KamiMod.getInstance().commandManager.callCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                }
                else {
                    MessageSendHelper.sendChatMessage("Please enter a command.");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                MessageSendHelper.sendChatMessage("Error occured while running command! (" + e.getMessage() + ")");
            }
            event.setMessage("");
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerDrawn(final RenderPlayerEvent.Pre event) {
        KamiMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerDrawn(final RenderPlayerEvent.Post event) {
        KamiMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onChunkLoaded(final ChunkEvent.Load event) {
        KamiMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onEventMouse(final InputEvent.MouseInputEvent event) {
        KamiMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onChunkLoaded(final ChunkEvent.Unload event) {
        KamiMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onInputUpdate(final InputUpdateEvent event) {
        KamiMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onLivingEntityUseItemEventTick(final LivingEntityUseItemEvent.Start entityUseItemEvent) {
        KamiMod.EVENT_BUS.post(entityUseItemEvent);
    }
    
    @SubscribeEvent
    public void onLivingDamageEvent(final LivingDamageEvent event) {
        KamiMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onEntityJoinWorldEvent(final EntityJoinWorldEvent entityJoinWorldEvent) {
        KamiMod.EVENT_BUS.post(entityJoinWorldEvent);
    }
    
    @SubscribeEvent
    public void onPlayerPush(final PlayerSPPushOutOfBlocksEvent event) {
        KamiMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
        KamiMod.EVENT_BUS.post(event);
    }
    
    @SubscribeEvent
    public void onAttackEntity(final AttackEntityEvent entityEvent) {
        KamiMod.EVENT_BUS.post(entityEvent);
    }
    
    @SubscribeEvent
    public void onRenderBlockOverlay(final RenderBlockOverlayEvent event) {
        KamiMod.EVENT_BUS.post(event);
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import me.xiam.creativehack.event.events.PlayerMoveEvent;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.injection.Inject;
import me.xiam.creativehack.util.BeaconGui;
import net.minecraft.world.IInteractionObject;
import me.xiam.creativehack.module.modules.misc.BeaconSelector;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.inventory.IInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.modules.chat.PortalChat;
import me.xiam.creativehack.KamiMod;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.player.EntityPlayer;

@Mixin({ EntityPlayerSP.class })
public abstract class MixinEntityPlayerSP extends EntityPlayer
{
    public MixinEntityPlayerSP(final World worldIn, final GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }
    
    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V"))
    public void closeScreen(final EntityPlayerSP entityPlayerSP) {
        if (KamiMod.MODULE_MANAGER.isModuleEnabled(PortalChat.class)) {
            return;
        }
    }
    
    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"))
    public void closeScreen(final Minecraft minecraft, final GuiScreen screen) {
        if (KamiMod.MODULE_MANAGER.isModuleEnabled(PortalChat.class)) {
            return;
        }
    }
    
    @Inject(method = { "displayGUIChest" }, at = { @At("HEAD") }, cancellable = true)
    public void onDisplayGUIChest(final IInventory chestInventory, final CallbackInfo ci) {
        if (KamiMod.MODULE_MANAGER.isModuleEnabled(BeaconSelector.class) && chestInventory instanceof IInteractionObject && "minecraft:beacon".equals(((IInteractionObject)chestInventory).getGuiID())) {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new BeaconGui(this.inventory, chestInventory));
            ci.cancel();
        }
    }
    
    @Inject(method = { "move" }, at = { @At("HEAD") }, cancellable = true)
    public void move(final MoverType type, final double x, final double y, final double z, final CallbackInfo info) {
        final PlayerMoveEvent event = new PlayerMoveEvent(type, x, y, z);
        KamiMod.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }
}

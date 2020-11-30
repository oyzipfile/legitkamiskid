// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.lwjgl.input.Keyboard;
import me.xiam.creativehack.module.Module;
import me.xiam.creativehack.module.modules.movement.InventoryMove;
import me.xiam.creativehack.KamiMod;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.util.MovementInput;

@Mixin(value = { MovementInputFromOptions.class }, priority = Integer.MAX_VALUE)
public abstract class MixinMovementInputFromOptions extends MovementInput
{
    @Redirect(method = { "updatePlayerMoveState" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    public boolean isKeyPressed(final KeyBinding keyBinding) {
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().currentScreen != null && !(Minecraft.getMinecraft().currentScreen instanceof GuiChat) && KamiMod.MODULE_MANAGER.isModuleEnabled(InventoryMove.class) && (KamiMod.MODULE_MANAGER.getModuleT(InventoryMove.class).sneak.getValue() || Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode() != keyBinding.getKeyCode() || KamiMod.MODULE_MANAGER.getModuleT(InventoryMove.class).sneak.getValue())) {
            return Keyboard.isKeyDown(keyBinding.getKeyCode());
        }
        return keyBinding.isKeyDown();
    }
}

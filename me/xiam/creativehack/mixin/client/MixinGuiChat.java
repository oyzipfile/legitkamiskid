// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.gui.GuiScreen;
import me.xiam.creativehack.command.Command;
import me.xiam.creativehack.gui.mc.KamiGuiChat;
import me.xiam.creativehack.util.Wrapper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiChat.class })
public abstract class MixinGuiChat
{
    @Shadow
    protected GuiTextField inputField;
    @Shadow
    public String historyBuffer;
    @Shadow
    public int sentHistoryCursor;
    
    @Shadow
    public abstract void initGui();
    
    @Inject(method = { "Lnet/minecraft/client/gui/GuiChat;keyTyped(CI)V" }, at = { @At("RETURN") })
    public void returnKeyTyped(final char typedChar, final int keyCode, final CallbackInfo info) {
        if (!(Wrapper.getMinecraft().currentScreen instanceof GuiChat) || Wrapper.getMinecraft().currentScreen instanceof KamiGuiChat) {
            return;
        }
        if (this.inputField.getText().startsWith(Command.getCommandPrefix())) {
            Wrapper.getMinecraft().displayGuiScreen((GuiScreen)new KamiGuiChat(this.inputField.getText(), this.historyBuffer, this.sentHistoryCursor));
        }
    }
}

// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.mixin.client;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.gui.GuiScreen;
import me.xiam.creativehack.gui.mc.KamiGuiUpdateNotification;
import me.xiam.creativehack.util.Wrapper;
import me.xiam.creativehack.KamiMod;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiMainMenu.class })
public abstract class MixinGuiMainMenu
{
    @Inject(method = { "actionPerformed" }, at = { @At("HEAD") }, cancellable = true)
    public void onActionPerformed(final GuiButton btn, final CallbackInfo callbackInfo) {
        if (!KamiMod.hasAskedToUpdate && KamiMod.latest != null && !KamiMod.isLatest) {
            if (btn.id == 1) {
                Wrapper.getMinecraft().displayGuiScreen((GuiScreen)new KamiGuiUpdateNotification("KAMI Blue Update", "A newer release of KAMI Blue is available (" + KamiMod.latest + ").", btn.id));
                KamiMod.hasAskedToUpdate = true;
                callbackInfo.cancel();
            }
            if (btn.id == 2) {
                Wrapper.getMinecraft().displayGuiScreen((GuiScreen)new KamiGuiUpdateNotification("KAMI Blue Update", "A newer release of KAMI Blue is available (" + KamiMod.latest + ").", btn.id));
                KamiMod.hasAskedToUpdate = true;
                callbackInfo.cancel();
            }
        }
    }
}

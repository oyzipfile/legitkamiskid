// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.gui.mc;

import java.net.URISyntaxException;
import me.xiam.creativehack.KamiMod;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.GuiMainMenu;
import me.xiam.creativehack.util.WebUtils;
import java.net.URI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class KamiGuiUpdateNotification extends GuiScreen
{
    private final String title;
    private final String message;
    private final int singleOrMulti;
    
    public KamiGuiUpdateNotification(final String title, final String message, final int singleOrMulti) {
        this.title = title;
        this.message = message;
        this.singleOrMulti = singleOrMulti;
    }
    
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 200, "Download Latest (Recommended)"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 230, "§cUse Current Version"));
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawGradientRect(0, 0, this.width, this.height, -12574688, -11530224);
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 80, 10260478);
        this.drawCenteredString(this.fontRenderer, this.message, this.width / 2, 110, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) {
    }
    
    protected void actionPerformed(final GuiButton button) {
        if (button.id == 0) {
            try {
                WebUtils.openWebLink(new URI("https://blue.bella.wtf/download"));
                if (this.singleOrMulti == 1) {
                    this.mc.displayGuiScreen((GuiScreen)new GuiWorldSelection((GuiScreen)new GuiMainMenu()));
                    return;
                }
                this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu()));
            }
            catch (URISyntaxException e) {
                KamiMod.log.error("Contact the KAMI Blue developers. Download link could not be parsed into URI reference form.");
            }
            return;
        }
        if (this.singleOrMulti == 1) {
            this.mc.displayGuiScreen((GuiScreen)new GuiWorldSelection((GuiScreen)new GuiMainMenu()));
            return;
        }
        this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu()));
    }
}

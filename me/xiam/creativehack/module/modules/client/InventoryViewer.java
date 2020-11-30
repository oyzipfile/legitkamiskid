// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.client;

import me.xiam.creativehack.util.MessageSendHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.client.gui.Gui;
import me.xiam.creativehack.util.ColourConverter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import me.xiam.creativehack.gui.rgui.component.container.use.Frame;
import me.xiam.creativehack.util.GuiFrameUtil;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "InventoryViewer", category = Category.CLIENT, description = "Configures Inventory Viewer's options", showOnArray = ShowOnArray.OFF)
public class InventoryViewer extends Module
{
    private Setting<Boolean> mcTexture;
    private Setting<Boolean> showIcon;
    private Setting<Boolean> docking;
    private Setting<ViewSize> viewSizeSetting;
    private Setting<Boolean> coloredBackground;
    private Setting<Integer> a;
    private Setting<Integer> r;
    private Setting<Integer> g;
    private Setting<Integer> b;
    private boolean isLeft;
    private boolean isRight;
    private boolean isTop;
    private boolean isBottom;
    
    public InventoryViewer() {
        this.mcTexture = this.register(Settings.b("Use ResourcePack", false));
        this.showIcon = this.register(Settings.booleanBuilder("Show Icon").withValue(true).withVisibility(v -> !this.mcTexture.getValue()).build());
        this.docking = this.register(Settings.booleanBuilder("Automatic Docking").withValue(true).withVisibility(v -> this.showIcon.getValue() && !this.mcTexture.getValue()).build());
        this.viewSizeSetting = this.register((Setting<ViewSize>)Settings.enumBuilder(ViewSize.class).withName("Icon Size").withValue(ViewSize.LARGE).withVisibility(v -> this.showIcon.getValue() && !this.mcTexture.getValue()).build());
        this.coloredBackground = this.register(Settings.booleanBuilder("Colored Background").withValue(true).withVisibility(v -> !this.mcTexture.getValue()).build());
        this.a = this.register(Settings.integerBuilder("Transparency").withMinimum(0).withValue(32).withMaximum(255).withVisibility(v -> this.coloredBackground.getValue() && !this.mcTexture.getValue()).build());
        this.r = this.register(Settings.integerBuilder("Red").withMinimum(0).withValue(155).withMaximum(255).withVisibility(v -> this.coloredBackground.getValue() && !this.mcTexture.getValue()).build());
        this.g = this.register(Settings.integerBuilder("Green").withMinimum(0).withValue(144).withMaximum(255).withVisibility(v -> this.coloredBackground.getValue() && !this.mcTexture.getValue()).build());
        this.b = this.register(Settings.integerBuilder("Blue").withMinimum(0).withValue(255).withMaximum(255).withVisibility(v -> this.coloredBackground.getValue() && !this.mcTexture.getValue()).build());
        this.isLeft = false;
        this.isRight = false;
        this.isTop = false;
        this.isBottom = false;
    }
    
    private int invMoveHorizontal() {
        if (!this.docking.getValue() || this.mcTexture.getValue()) {
            return 0;
        }
        if (this.isLeft) {
            return 45;
        }
        if (this.isRight) {
            return -45;
        }
        return 0;
    }
    
    private int invMoveVertical() {
        if (!this.docking.getValue() || this.mcTexture.getValue()) {
            return 0;
        }
        if (this.isTop) {
            return 10;
        }
        if (this.isBottom) {
            return -10;
        }
        return 0;
    }
    
    private void updatePos() {
        final Frame frame = GuiFrameUtil.getFrameByName("inventory viewer");
        if (frame == null) {
            return;
        }
        this.isTop = frame.getDocking().isTop();
        this.isLeft = frame.getDocking().isLeft();
        this.isRight = frame.getDocking().isRight();
        this.isBottom = frame.getDocking().isBottom();
    }
    
    private ResourceLocation getBox() {
        if (this.mcTexture.getValue()) {
            return new ResourceLocation("textures/gui/container/generic_54.png");
        }
        if (!this.showIcon.getValue()) {
            return new ResourceLocation("kamiblue/clear.png");
        }
        if (this.viewSizeSetting.getValue().equals(ViewSize.LARGE)) {
            return new ResourceLocation("kamiblue/large.png");
        }
        if (this.viewSizeSetting.getValue().equals(ViewSize.SMALL)) {
            return new ResourceLocation("kamiblue/small.png");
        }
        if (this.viewSizeSetting.getValue().equals(ViewSize.MEDIUM)) {
            return new ResourceLocation("kamiblue/medium.png");
        }
        return new ResourceLocation("null");
    }
    
    private void boxRender(final int x, final int y) {
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        if (this.coloredBackground.getValue()) {
            Gui.drawRect(x, y, x + 162, y + 54, ColourConverter.rgbToInt(this.r.getValue(), this.g.getValue(), this.b.getValue(), this.a.getValue()));
        }
        final ResourceLocation box = this.getBox();
        InventoryViewer.mc.renderEngine.bindTexture(box);
        this.updatePos();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        InventoryViewer.mc.ingameGUI.drawTexturedModalRect(x, y, this.invMoveHorizontal() + 7, this.invMoveVertical() + 17, 162, 54);
        GlStateManager.enableDepth();
    }
    
    @Override
    public void onRender() {
        final Frame frame = GuiFrameUtil.getFrameByName("inventory viewer");
        if (frame == null) {
            return;
        }
        if (frame.isPinned() && !frame.isMinimized()) {
            final NonNullList<ItemStack> items = (NonNullList<ItemStack>)InventoryViewer.mc.player.inventory.mainInventory;
            this.boxRender(frame.getX(), frame.getY());
            this.itemRender(items, frame.getX(), frame.getY());
        }
    }
    
    private void itemRender(final NonNullList<ItemStack> items, final int x, final int y) {
        GlStateManager.clear(256);
        for (int size = items.size(), item = 9; item < size; ++item) {
            final int slotX = x + 1 + item % 9 * 18;
            final int slotY = y + 1 + (item / 9 - 1) * 18;
            preItemRender();
            InventoryViewer.mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)items.get(item), slotX, slotY);
            InventoryViewer.mc.getRenderItem().renderItemOverlays(InventoryViewer.mc.fontRenderer, (ItemStack)items.get(item), slotX, slotY);
            postItemRender();
        }
    }
    
    private static void preItemRender() {
        GlStateManager.pushMatrix();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        RenderHelper.enableGUIStandardItemLighting();
    }
    
    private static void postItemRender() {
        RenderHelper.disableStandardItemLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }
    
    public void onDisable() {
        MessageSendHelper.sendDisableMessage(this.getClass());
    }
    
    private enum ViewSize
    {
        LARGE, 
        MEDIUM, 
        SMALL;
    }
}

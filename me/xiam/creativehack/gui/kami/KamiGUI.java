// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.gui.kami;

import me.xiam.creativehack.gui.rgui.util.Docking;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.LinkedList;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.projectile.EntityWitherSkull;
import javax.annotation.Nonnull;
import java.text.NumberFormat;
import me.xiam.creativehack.gui.rgui.component.AbstractComponent;
import me.xiam.creativehack.gui.kami.component.Radar;
import me.xiam.creativehack.util.InfoCalculator;
import net.minecraft.util.text.TextFormatting;
import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.item.EntityItem;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import me.xiam.creativehack.gui.rgui.component.listen.TickListener;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.entity.player.EntityPlayer;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.Entity;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import me.xiam.creativehack.util.Friends;
import me.xiam.creativehack.gui.rgui.render.font.FontRenderer;
import java.util.function.Consumer;
import me.xiam.creativehack.module.modules.client.InfoOverlay;
import me.xiam.creativehack.gui.rgui.component.use.Label;
import me.xiam.creativehack.gui.kami.component.ActiveModules;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.xiam.creativehack.gui.rgui.component.container.Container;
import me.xiam.creativehack.gui.rgui.util.ContainerHelper;
import me.xiam.creativehack.util.Wrapper;
import me.xiam.creativehack.gui.rgui.component.container.use.Frame;
import java.util.Map;
import me.xiam.creativehack.gui.rgui.poof.IPoof;
import me.xiam.creativehack.gui.rgui.poof.PoofInfo;
import me.xiam.creativehack.gui.rgui.component.Component;
import me.xiam.creativehack.gui.rgui.component.use.CheckButton.CheckButtonPoof;
import me.xiam.creativehack.gui.rgui.component.listen.MouseListener;
import me.xiam.creativehack.gui.rgui.component.use.CheckButton;
import me.xiam.creativehack.gui.rgui.layout.Layout;
import me.xiam.creativehack.KamiMod;
import me.xiam.creativehack.gui.kami.component.SettingsPanel;
import me.xiam.creativehack.gui.rgui.component.container.use.Scrollpane;
import me.xiam.creativehack.util.Pair;
import me.xiam.creativehack.module.Module;
import java.util.HashMap;
import me.xiam.creativehack.gui.kami.theme.kami.KamiTheme;
import me.xiam.creativehack.util.ColourHolder;
import me.xiam.creativehack.gui.rgui.render.theme.Theme;
import me.xiam.creativehack.gui.rgui.GUI;

public class KamiGUI extends GUI
{
    public static final RootFontRenderer fontRenderer;
    public Theme theme;
    public static ColourHolder primaryColour;
    private static final int DOCK_OFFSET = 0;
    
    public KamiGUI() {
        super(new KamiTheme());
        this.theme = this.getTheme();
    }
    
    @Override
    public void drawGUI() {
        super.drawGUI();
    }
    
    @Override
    public void initializeGUI() {
        final HashMap<Module.Category, Pair<Scrollpane, SettingsPanel>> categoryScrollpaneHashMap = new HashMap<Module.Category, Pair<Scrollpane, SettingsPanel>>();
        for (final Module module : KamiMod.MODULE_MANAGER.getModules()) {
            if (module.getCategory().isHidden()) {
                continue;
            }
            final Module.Category moduleCategory = module.getCategory();
            if (!categoryScrollpaneHashMap.containsKey(moduleCategory)) {
                final Stretcherlayout stretcherlayout = new Stretcherlayout(1);
                stretcherlayout.setComponentOffsetWidth(0);
                final Scrollpane scrollpane = new Scrollpane(this.getTheme(), stretcherlayout, 300, 260);
                scrollpane.setMaximumHeight(180);
                categoryScrollpaneHashMap.put(moduleCategory, new Pair<Scrollpane, SettingsPanel>(scrollpane, new SettingsPanel(this.getTheme(), (Module)null)));
            }
            final Pair<Scrollpane, SettingsPanel> pair = categoryScrollpaneHashMap.get(moduleCategory);
            final Scrollpane scrollpane = pair.getKey();
            final CheckButton checkButton = new CheckButton(module.getName(), module.getDescription());
            checkButton.setToggled(module.isEnabled());
            final CheckButton checkButton2;
            final Module module2;
            checkButton.addTickListener(() -> {
                checkButton2.setToggled(module2.isEnabled());
                checkButton2.setName(module2.getName());
                checkButton2.setDescription(module2.getDescription());
                return;
            });
            checkButton.addMouseListener(new MouseListener() {
                @Override
                public void onMouseDown(final MouseButtonEvent event) {
                    if (event.getButton() == 1) {
                        pair.getValue().setModule(module);
                        pair.getValue().setX(event.getX() + checkButton.getX());
                        pair.getValue().setY(event.getY() + checkButton.getY());
                    }
                }
                
                @Override
                public void onMouseRelease(final MouseButtonEvent event) {
                }
                
                @Override
                public void onMouseDrag(final MouseButtonEvent event) {
                }
                
                @Override
                public void onMouseMove(final MouseMoveEvent event) {
                }
                
                @Override
                public void onScroll(final MouseScrollEvent event) {
                }
            });
            checkButton.addPoof(new CheckButton.CheckButtonPoof<CheckButton, CheckButton.CheckButtonPoof.CheckButtonPoofInfo>() {
                @Override
                public void execute(final CheckButton component, final CheckButtonPoofInfo info) {
                    if (info.getAction().equals(CheckButtonPoofInfo.CheckButtonPoofInfoAction.TOGGLE)) {
                        module.setEnabled(checkButton.isToggled());
                    }
                }
            });
            scrollpane.addChild(checkButton);
        }
        int x = 10;
        int nexty;
        int y = nexty = 10;
        for (final Map.Entry<Module.Category, Pair<Scrollpane, SettingsPanel>> entry : categoryScrollpaneHashMap.entrySet()) {
            final Stretcherlayout stretcherlayout2 = new Stretcherlayout(1);
            stretcherlayout2.COMPONENT_OFFSET_Y = 1;
            final Frame frame = new Frame(this.getTheme(), stretcherlayout2, entry.getKey().getName());
            final Scrollpane scrollpane2 = entry.getValue().getKey();
            frame.addChild(scrollpane2);
            frame.addChild(entry.getValue().getValue());
            scrollpane2.setOriginOffsetY(0);
            scrollpane2.setOriginOffsetX(0);
            frame.setCloseable(false);
            frame.setX(x);
            frame.setY(y);
            this.addChild(frame);
            nexty = Math.max(y + frame.getHeight() + 10, nexty);
            x += frame.getWidth() + 10;
            if (x > Wrapper.getMinecraft().displayWidth / 1.2f) {
                y = (nexty = nexty);
            }
        }
        this.addMouseListener(new MouseListener() {
            private boolean isNotBetween(final int min, final int val, final int max) {
                return val > max || val < min;
            }
            
            @Override
            public void onMouseDown(final MouseButtonEvent event) {
                final List<SettingsPanel> panels = ContainerHelper.getAllChildren((Class<? extends SettingsPanel>)SettingsPanel.class, (Container)KamiGUI.this);
                for (final SettingsPanel settingsPanel : panels) {
                    if (!settingsPanel.isVisible()) {
                        continue;
                    }
                    final int[] real = GUI.calculateRealPosition(settingsPanel);
                    final int pX = event.getX() - real[0];
                    final int pY = event.getY() - real[1];
                    if (!this.isNotBetween(0, pX, settingsPanel.getWidth()) && !this.isNotBetween(0, pY, settingsPanel.getHeight())) {
                        continue;
                    }
                    settingsPanel.setVisible(false);
                }
            }
            
            @Override
            public void onMouseRelease(final MouseButtonEvent event) {
            }
            
            @Override
            public void onMouseDrag(final MouseButtonEvent event) {
            }
            
            @Override
            public void onMouseMove(final MouseMoveEvent event) {
            }
            
            @Override
            public void onScroll(final MouseScrollEvent event) {
            }
        });
        final ArrayList<Frame> frames = new ArrayList<Frame>();
        Frame frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Active modules");
        frame2.setCloseable(false);
        frame2.addChild(new ActiveModules());
        frame2.setPinnable(true);
        frames.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Info");
        frame2.setCloseable(false);
        frame2.setPinnable(true);
        final Label information = new Label("");
        information.setShadow(true);
        final InfoOverlay info;
        final Label label;
        information.addTickListener(() -> {
            info = KamiMod.MODULE_MANAGER.getModuleT(InfoOverlay.class);
            label.setText("");
            info.infoContents().forEach(label::addLine);
            return;
        });
        frame2.addChild(information);
        information.setFontRenderer(KamiGUI.fontRenderer);
        frames.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Inventory Viewer");
        frame2.setCloseable(false);
        frame2.setMinimizeable(true);
        frame2.setPinnable(true);
        frame2.setPinned(true);
        final Label inventory = new Label("");
        inventory.setShadow(false);
        final AbstractComponent abstractComponent;
        inventory.addTickListener(() -> {
            abstractComponent.setWidth(151);
            abstractComponent.setHeight(40);
            abstractComponent.setOpacity(0.1f);
            return;
        });
        frame2.addChild(inventory);
        inventory.setFontRenderer(KamiGUI.fontRenderer);
        frames.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Friends");
        frame2.setCloseable(false);
        frame2.setPinnable(false);
        frame2.setMinimizeable(true);
        final Label friends = new Label("");
        friends.setShadow(true);
        final Frame finalFrame = frame2;
        final Label label2;
        final Frame frame4;
        friends.addTickListener(() -> {
            label2.setText("");
            if (!frame4.isMinimized()) {
                Friends.friends.getValue().forEach(friend -> label2.addLine(friend.getUsername()));
            }
            else {
                label2.setWidth(50);
            }
            return;
        });
        frame2.addChild(friends);
        friends.setFontRenderer(KamiGUI.fontRenderer);
        frames.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Text Radar");
        final Label list = new Label("");
        final DecimalFormat dfHealth = new DecimalFormat("#.#");
        dfHealth.setRoundingMode(RoundingMode.HALF_UP);
        final StringBuilder healthSB = new StringBuilder();
        final Label label3;
        Minecraft mc;
        List<EntityPlayer> entityList;
        Map<String, Integer> players;
        final Iterator<EntityPlayer> iterator3;
        Entity e;
        String s;
        String posString;
        EntityPlayer ePlayer;
        String weaknessFactor;
        String strengthFactor;
        String extraPaddingForFactors;
        float hpRaw;
        final NumberFormat numberFormat;
        String hp;
        final StringBuilder sb;
        Map<String, Integer> players2;
        final Iterator<Map.Entry<String, Integer>> iterator4;
        Map.Entry<String, Integer> player;
        list.addTickListener(() -> {
            if (!label3.isVisible()) {
                return;
            }
            else {
                label3.setText("");
                mc = Wrapper.getMinecraft();
                if (mc.player == null) {
                    return;
                }
                else {
                    entityList = (List<EntityPlayer>)mc.world.playerEntities;
                    players = new HashMap<String, Integer>();
                    entityList.iterator();
                    while (iterator3.hasNext()) {
                        e = (Entity)iterator3.next();
                        if (e.getName().equals(mc.player.getName())) {
                            continue;
                        }
                        else {
                            if (e.posY > mc.player.posY) {
                                s = ChatFormatting.DARK_GREEN + "+";
                            }
                            else if (e.posY == mc.player.posY) {
                                s = " ";
                            }
                            else {
                                s = ChatFormatting.DARK_RED + "-";
                            }
                            posString = s;
                            ePlayer = (EntityPlayer)e;
                            if (ePlayer.isPotionActive(MobEffects.WEAKNESS)) {
                                weaknessFactor = "W";
                            }
                            else {
                                weaknessFactor = "";
                            }
                            if (ePlayer.isPotionActive(MobEffects.STRENGTH)) {
                                strengthFactor = "S";
                            }
                            else {
                                strengthFactor = "";
                            }
                            if (weaknessFactor.equals("") && strengthFactor.equals("")) {
                                extraPaddingForFactors = "";
                            }
                            else {
                                extraPaddingForFactors = " ";
                            }
                            hpRaw = ((EntityLivingBase)e).getHealth() + ((EntityLivingBase)e).getAbsorptionAmount();
                            hp = numberFormat.format(hpRaw);
                            sb.append('§');
                            if (hpRaw >= 20.0f) {
                                sb.append("a");
                            }
                            else if (hpRaw >= 10.0f) {
                                sb.append("e");
                            }
                            else if (hpRaw >= 5.0f) {
                                sb.append("6");
                            }
                            else {
                                sb.append("c");
                            }
                            sb.append(hp);
                            players.put(ChatFormatting.GRAY + posString + " " + sb.toString() + " " + ChatFormatting.DARK_GRAY + weaknessFactor + ChatFormatting.DARK_PURPLE + strengthFactor + ChatFormatting.GRAY + extraPaddingForFactors + e.getName(), (int)mc.player.getDistance(e));
                            sb.setLength(0);
                        }
                    }
                    if (players.isEmpty()) {
                        label3.setText("");
                        return;
                    }
                    else {
                        players2 = sortByValue(players);
                        players2.entrySet().iterator();
                        while (iterator4.hasNext()) {
                            player = iterator4.next();
                            label3.addLine("§7" + player.getKey() + " " + '§' + "8" + player.getValue());
                        }
                        return;
                    }
                }
            }
        });
        frame2.setCloseable(false);
        frame2.setPinnable(true);
        frame2.setMinimumWidth(75);
        list.setShadow(true);
        frame2.addChild(list);
        list.setFontRenderer(KamiGUI.fontRenderer);
        frames.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Entities");
        final Label entityLabel = new Label("");
        frame2.setCloseable(false);
        final Frame finalFrame2 = frame2;
        entityLabel.addTickListener(new TickListener() {
            Minecraft mc = Wrapper.getMinecraft();
            
            @Override
            public void onTick() {
                if (!finalFrame2.isMinimized()) {
                    if (this.mc.player == null || !entityLabel.isVisible()) {
                        return;
                    }
                    final List<Entity> entityList = new ArrayList<Entity>(this.mc.world.loadedEntityList);
                    if (entityList.size() <= 1) {
                        entityLabel.setText("");
                        return;
                    }
                    final Map<String, Integer> entityCounts = entityList.stream().filter(Objects::nonNull).filter(e -> !(e instanceof EntityPlayer)).collect(Collectors.groupingBy(x$0 -> getEntityName(x$0), (Collector<? super Object, ?, Integer>)Collectors.reducing((D)0, ent -> {
                        if (ent instanceof EntityItem) {
                            return Integer.valueOf(ent.getItem().getCount());
                        }
                        else {
                            return Integer.valueOf(1);
                        }
                    }, Integer::sum)));
                    entityLabel.setText("");
                    finalFrame2.setWidth(50);
                    entityCounts.entrySet().stream().sorted((Comparator<? super Object>)Map.Entry.comparingByValue()).map(entry -> TextFormatting.GRAY + entry.getKey() + " " + TextFormatting.DARK_GRAY + "x" + entry.getValue()).forEach((Consumer<? super Object>)entityLabel::addLine);
                }
                else {
                    finalFrame2.setWidth(50);
                }
            }
        });
        frame2.addChild(entityLabel);
        frame2.setPinnable(true);
        entityLabel.setShadow(true);
        entityLabel.setFontRenderer(KamiGUI.fontRenderer);
        frames.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Coordinates");
        frame2.setCloseable(false);
        frame2.setPinnable(true);
        final Label coordsLabel = new Label("");
        coordsLabel.addTickListener(new TickListener() {
            Minecraft mc = Minecraft.getMinecraft();
            
            @Override
            public void onTick() {
                final boolean inHell = this.mc.world.getBiome(this.mc.player.getPosition()).getBiomeName().equals("Hell");
                final int posX = (int)this.mc.player.posX;
                final int posY = (int)this.mc.player.posY;
                final int posZ = (int)this.mc.player.posZ;
                final float f = inHell ? 8.0f : 0.125f;
                final int hposX = (int)(this.mc.player.posX * f);
                final int hposZ = (int)(this.mc.player.posZ * f);
                final String cardinal = InfoCalculator.cardinalToAxis(Character.toUpperCase(this.mc.player.getHorizontalFacing().toString().charAt(0)));
                final String colouredSeparator = "§7 \u23d0§r";
                final String ow = String.format("%sf%,d%s7, %sf%,d%s7, %sf%,d %s7", '§', posX, '§', '§', posY, '§', '§', posZ, '§');
                final String nether = String.format(" (%sf%,d%s7, %sf%,d%s7, %sf%,d%s7)", '§', hposX, '§', '§', posY, '§', '§', hposZ, '§');
                coordsLabel.setText("");
                coordsLabel.addLine(ow);
                coordsLabel.addLine(cardinal + colouredSeparator + nether);
            }
        });
        frame2.addChild(coordsLabel);
        coordsLabel.setFontRenderer(KamiGUI.fontRenderer);
        coordsLabel.setShadow(true);
        frame2.setHeight(20);
        frames.add(frame2);
        frame2 = new Frame(this.getTheme(), new Stretcherlayout(1), "Radar");
        frame2.setCloseable(false);
        frame2.setMinimizeable(true);
        frame2.setPinnable(true);
        frame2.addChild(new Radar());
        frame2.setWidth(100);
        frame2.setHeight(100);
        frames.add(frame2);
        for (final Frame frame3 : frames) {
            frame3.setX(x);
            frame3.setY(y);
            nexty = Math.max(y + frame3.getHeight() + 10, nexty);
            x += frame3.getWidth() + 10;
            if (x * DisplayGuiScreen.getScale() > Wrapper.getMinecraft().displayWidth / 1.2f) {
                y = (nexty = nexty);
                x = 10;
            }
            this.addChild(frame3);
        }
    }
    
    private static String getEntityName(@Nonnull final Entity entity) {
        if (entity instanceof EntityItem) {
            return TextFormatting.DARK_AQUA + ((EntityItem)entity).getItem().getItem().getItemStackDisplayName(((EntityItem)entity).getItem());
        }
        if (entity instanceof EntityWitherSkull) {
            return TextFormatting.DARK_GRAY + "Wither skull";
        }
        if (entity instanceof EntityEnderCrystal) {
            return TextFormatting.LIGHT_PURPLE + "End crystal";
        }
        if (entity instanceof EntityEnderPearl) {
            return "Thrown ender pearl";
        }
        if (entity instanceof EntityMinecart) {
            return "Minecart";
        }
        if (entity instanceof EntityItemFrame) {
            return "Item frame";
        }
        if (entity instanceof EntityEgg) {
            return "Thrown egg";
        }
        if (entity instanceof EntitySnowball) {
            return "Thrown snowball";
        }
        return entity.getName();
    }
    
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map) {
        final List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, Comparator.comparing(o -> o.getValue()));
        final Map<K, V> result = new LinkedHashMap<K, V>();
        for (final Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    @Override
    public void destroyGUI() {
        this.kill();
    }
    
    public static void dock(final Frame component) {
        final Docking docking = component.getDocking();
        if (docking.isTop()) {
            component.setY(0);
        }
        if (docking.isBottom()) {
            component.setY(Wrapper.getMinecraft().displayHeight / DisplayGuiScreen.getScale() - component.getHeight() - 0);
        }
        if (docking.isLeft()) {
            component.setX(0);
        }
        if (docking.isRight()) {
            component.setX(Wrapper.getMinecraft().displayWidth / DisplayGuiScreen.getScale() - component.getWidth() - 0);
        }
        if (docking.isCenterHorizontal()) {
            component.setX(Wrapper.getMinecraft().displayWidth / (DisplayGuiScreen.getScale() * 2) - component.getWidth() / 2);
        }
        if (docking.isCenterVertical()) {
            component.setY(Wrapper.getMinecraft().displayHeight / (DisplayGuiScreen.getScale() * 2) - component.getHeight() / 2);
        }
    }
    
    static {
        fontRenderer = new RootFontRenderer(1.0f);
        KamiGUI.primaryColour = new ColourHolder(29, 29, 29);
    }
}

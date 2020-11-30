// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.client;

import java.util.function.ToIntFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import me.xiam.creativehack.util.MessageSendHelper;
import net.minecraft.init.Items;
import me.xiam.creativehack.module.modules.movement.TimerSpeed;
import net.minecraft.client.Minecraft;
import me.xiam.creativehack.util.InfoCalculator;
import java.util.ArrayList;
import net.minecraft.util.text.TextFormatting;
import me.xiam.creativehack.setting.Settings;
import me.xiam.creativehack.util.ColourTextFormatting;
import me.xiam.creativehack.util.TimeUtil;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "InfoOverlay", category = Category.CLIENT, description = "Configures the game information overlay", showOnArray = ShowOnArray.OFF)
public class InfoOverlay extends Module
{
    private Setting<Page> page;
    private Setting<Boolean> version;
    private Setting<Boolean> username;
    private Setting<Boolean> tps;
    private Setting<Boolean> fps;
    private Setting<Boolean> ping;
    private Setting<Boolean> durability;
    private Setting<Boolean> memory;
    private Setting<Boolean> timerSpeed;
    private Setting<Boolean> totems;
    private Setting<Boolean> endCrystals;
    private Setting<Boolean> expBottles;
    private Setting<Boolean> godApples;
    private Setting<Boolean> speed;
    private Setting<SpeedUnit> speedUnit;
    private Setting<Boolean> time;
    public Setting<TimeUtil.TimeType> timeTypeSetting;
    public Setting<TimeUtil.TimeUnit> timeUnitSetting;
    public Setting<Boolean> doLocale;
    public Setting<ColourTextFormatting.ColourCode> firstColour;
    public Setting<ColourTextFormatting.ColourCode> secondColour;
    
    public InfoOverlay() {
        this.page = this.register((Setting<Page>)Settings.enumBuilder(Page.class).withName("Page").withValue(Page.ONE).build());
        this.version = this.register(Settings.booleanBuilder("Version").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.username = this.register(Settings.booleanBuilder("Username").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.tps = this.register(Settings.booleanBuilder("TPS").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.fps = this.register(Settings.booleanBuilder("FPS").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.ping = this.register(Settings.booleanBuilder("Ping").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.durability = this.register(Settings.booleanBuilder("Item Damage").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.memory = this.register(Settings.booleanBuilder("RAM Used").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.timerSpeed = this.register(Settings.booleanBuilder("Timer Speed").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.ONE)).build());
        this.totems = this.register(Settings.booleanBuilder("Totems").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.TWO)).build());
        this.endCrystals = this.register(Settings.booleanBuilder("End Crystals").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.TWO)).build());
        this.expBottles = this.register(Settings.booleanBuilder("EXP Bottles").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.TWO)).build());
        this.godApples = this.register(Settings.booleanBuilder("God Apples").withValue(false).withVisibility(v -> this.page.getValue().equals(Page.TWO)).build());
        this.speed = this.register(Settings.booleanBuilder("Speed").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.THREE)).build());
        this.speedUnit = this.register((Setting<SpeedUnit>)Settings.enumBuilder(SpeedUnit.class).withName("Speed Unit").withValue(SpeedUnit.KMH).withVisibility(v -> this.page.getValue().equals(Page.THREE) && this.speed.getValue()).build());
        this.time = this.register(Settings.booleanBuilder("Time").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.THREE)).build());
        this.timeTypeSetting = this.register((Setting<TimeUtil.TimeType>)Settings.enumBuilder(TimeUtil.TimeType.class).withName("Time Format").withValue(TimeUtil.TimeType.HHMMSS).withVisibility(v -> this.page.getValue().equals(Page.THREE) && this.time.getValue()).build());
        this.timeUnitSetting = this.register((Setting<TimeUtil.TimeUnit>)Settings.enumBuilder(TimeUtil.TimeUnit.class).withName("Time Unit").withValue(TimeUtil.TimeUnit.H12).withVisibility(v -> this.page.getValue().equals(Page.THREE) && this.time.getValue()).build());
        this.doLocale = this.register(Settings.booleanBuilder("Time Show AMPM").withValue(true).withVisibility(v -> this.page.getValue().equals(Page.THREE) && this.time.getValue()).build());
        this.firstColour = this.register((Setting<ColourTextFormatting.ColourCode>)Settings.enumBuilder(ColourTextFormatting.ColourCode.class).withName("First Colour").withValue(ColourTextFormatting.ColourCode.WHITE).withVisibility(v -> this.page.getValue().equals(Page.THREE)).build());
        this.secondColour = this.register((Setting<ColourTextFormatting.ColourCode>)Settings.enumBuilder(ColourTextFormatting.ColourCode.class).withName("Second Colour").withValue(ColourTextFormatting.ColourCode.BLUE).withVisibility(v -> this.page.getValue().equals(Page.THREE)).build());
    }
    
    public static String getStringColour(final TextFormatting c) {
        return c.toString();
    }
    
    private TextFormatting setToText(final ColourTextFormatting.ColourCode colourCode) {
        return ColourTextFormatting.toTextMap.get(colourCode);
    }
    
    public ArrayList<String> infoContents() {
        final ArrayList<String> infoContents = new ArrayList<String>();
        if (this.version.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + "Cr4tiv3h4ck" + getStringColour(this.setToText(this.secondColour.getValue())) + " " + "v1.1.3");
        }
        if (this.username.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + "Welcome" + getStringColour(this.setToText(this.secondColour.getValue())) + " " + InfoOverlay.mc.getSession().getUsername() + "!");
        }
        if (this.time.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + TimeUtil.getFinalTime(this.setToText(this.secondColour.getValue()), this.setToText(this.firstColour.getValue()), this.timeUnitSetting.getValue(), this.timeTypeSetting.getValue(), this.doLocale.getValue()));
        }
        if (this.tps.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + InfoCalculator.tps() + getStringColour(this.setToText(this.secondColour.getValue())) + " tps");
        }
        if (this.fps.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + Minecraft.debugFPS + getStringColour(this.setToText(this.secondColour.getValue())) + " fps");
        }
        if (this.speed.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + InfoCalculator.speed(this.useUnitKmH(), InfoOverlay.mc) + getStringColour(this.setToText(this.secondColour.getValue())) + " " + this.unitType(this.speedUnit.getValue()));
        }
        if (this.timerSpeed.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + TimerSpeed.returnGui() + getStringColour(this.setToText(this.secondColour.getValue())) + "t");
        }
        if (this.ping.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + InfoCalculator.ping(InfoOverlay.mc) + getStringColour(this.setToText(this.secondColour.getValue())) + " ms");
        }
        if (this.durability.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + InfoCalculator.dura(InfoOverlay.mc) + getStringColour(this.setToText(this.secondColour.getValue())) + " dura");
        }
        if (this.memory.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + InfoCalculator.memory() + getStringColour(this.setToText(this.secondColour.getValue())) + "mB free");
        }
        if (this.totems.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + getItems(Items.TOTEM_OF_UNDYING) + getStringColour(this.setToText(this.secondColour.getValue())) + " Totems");
        }
        if (this.endCrystals.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + getItems(Items.END_CRYSTAL) + getStringColour(this.setToText(this.secondColour.getValue())) + " Crystals");
        }
        if (this.expBottles.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + getItems(Items.EXPERIENCE_BOTTLE) + getStringColour(this.setToText(this.secondColour.getValue())) + " EXP Bottles");
        }
        if (this.godApples.getValue()) {
            infoContents.add(getStringColour(this.setToText(this.firstColour.getValue())) + getItems(Items.GOLDEN_APPLE) + getStringColour(this.setToText(this.secondColour.getValue())) + " God Apples");
        }
        return infoContents;
    }
    
    public void onDisable() {
        MessageSendHelper.sendDisableMessage(this.getClass());
    }
    
    public boolean useUnitKmH() {
        return this.speedUnit.getValue().equals(SpeedUnit.KMH);
    }
    
    public static int getItems(final Item i) {
        return InfoOverlay.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::func_190916_E).sum() + InfoOverlay.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::func_190916_E).sum();
    }
    
    public static int getArmor(final Item i) {
        return InfoOverlay.mc.player.inventory.armorInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::func_190916_E).sum();
    }
    
    private String unitType(final SpeedUnit s) {
        switch (s) {
            case MPS: {
                return "m/s";
            }
            case KMH: {
                return "km/h";
            }
            default: {
                return "Invalid unit type (mps or kmh)";
            }
        }
    }
    
    private enum SpeedUnit
    {
        MPS, 
        KMH;
    }
    
    private enum Page
    {
        ONE, 
        TWO, 
        THREE;
    }
}

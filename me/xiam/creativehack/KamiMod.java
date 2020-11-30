// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack;

import me.zero.alpine.EventManager;
import org.apache.logging.log4j.LogManager;
import org.apache.commons.io.IOUtils;
import java.net.URL;
import com.google.gson.JsonParser;
import java.io.File;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import com.google.gson.JsonPrimitive;
import me.xiam.creativehack.gui.rgui.component.Component;
import java.util.Optional;
import java.util.Iterator;
import me.xiam.creativehack.gui.rgui.component.container.Container;
import me.xiam.creativehack.gui.rgui.util.ContainerHelper;
import me.xiam.creativehack.gui.rgui.component.AlignedComponent;
import me.xiam.creativehack.gui.rgui.util.Docking;
import me.xiam.creativehack.gui.rgui.component.container.use.Frame;
import com.google.gson.JsonElement;
import java.util.Map;
import me.xiam.creativehack.setting.config.Configuration;
import java.nio.file.LinkOption;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.NoSuchFileException;
import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import me.xiam.creativehack.module.modules.hidden.RunConfig;
import me.xiam.creativehack.util.RichPresence;
import me.xiam.creativehack.setting.SettingsRegister;
import me.xiam.creativehack.command.Command;
import me.xiam.creativehack.util.Friends;
import me.xiam.creativehack.util.Wrapper;
import me.xiam.creativehack.util.LagCompensator;
import me.xiam.creativehack.event.ForgeEventProcessor;
import net.minecraftforge.common.MinecraftForge;
import me.xiam.creativehack.module.Module;
import java.util.function.Consumer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.opengl.Display;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import me.xiam.creativehack.setting.Settings;
import com.google.common.base.Converter;
import com.google.gson.JsonObject;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.command.CommandManager;
import me.xiam.creativehack.gui.kami.KamiGUI;
import me.xiam.creativehack.module.ModuleManager;
import me.zero.alpine.EventBus;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "kamiblue", name = "KAMI Blue", version = "v1.1.3")
public class KamiMod
{
    public static final String MODNAME = "KAMI Blue";
    public static final String MODID = "kamiblue";
    public static final String MODVER = "v1.1.3";
    public static final String MODVERSMALL = "v1.1.3";
    public static final String MODVERBROAD = "v1.1.3";
    public static final String MCVER = "1.12.2";
    public static final String APP_ID = "782918759219593216";
    private static final String UPDATE_JSON = "https://raw.githubusercontent.com/kami-blue/assets/assets/assets/updateChecker.json";
    public static final String DONATORS_JSON = "https://raw.githubusercontent.com/kami-blue/assets/assets/assets/donators.json";
    public static final String CAPES_JSON = "https://raw.githubusercontent.com/kami-blue/assets/assets/assets/capes.json";
    public static final String GITHUB_LINK = "https://github.com/kami-blue/";
    public static final String WEBSITE_LINK = "https://blue.bella.wtf";
    public static final String KAMI_KANJI = "Cr4tiv3h4ck";
    public static final char colour = '§';
    public static final char separator = '\u23d0';
    private static final String KAMI_CONFIG_NAME_DEFAULT = "KAMIBlueConfig.json";
    public static final Logger log;
    public static final EventBus EVENT_BUS;
    public static final ModuleManager MODULE_MANAGER;
    public static String latest;
    public static boolean isLatest;
    public static boolean hasAskedToUpdate;
    @Mod.Instance
    private static KamiMod INSTANCE;
    public KamiGUI guiManager;
    public CommandManager commandManager;
    private Setting<JsonObject> guiStateSetting;
    
    public KamiMod() {
        this.guiStateSetting = Settings.custom("gui", new JsonObject(), new Converter<JsonObject, JsonObject>() {
            protected JsonObject doForward(final JsonObject jsonObject) {
                return jsonObject;
            }
            
            protected JsonObject doBackward(final JsonObject jsonObject) {
                return jsonObject;
            }
        }).buildAndRegister("");
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        this.updateCheck();
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        DiscordPresence.setCustomIcons();
        Display.setTitle("Cr4tiv3h4ck");
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        KamiMod.log.info("\n\nInitializing Cr4tiv3h4ck");
        KamiMod.MODULE_MANAGER.register();
        KamiMod.MODULE_MANAGER.getModules().stream().filter(module -> module.alwaysListening).forEach(KamiMod.EVENT_BUS::subscribe);
        MinecraftForge.EVENT_BUS.register((Object)new ForgeEventProcessor());
        LagCompensator.INSTANCE = new LagCompensator();
        Wrapper.init();
        (this.guiManager = new KamiGUI()).initializeGUI();
        this.commandManager = new CommandManager();
        Friends.initFriends();
        SettingsRegister.register("commandPrefix", Command.commandPrefix);
        loadConfiguration();
        KamiMod.log.info("Settings loaded");
        new RichPresence();
        KamiMod.log.info("Rich Presence Users init!\n");
        KamiMod.MODULE_MANAGER.getModules().stream().filter(Module::isEnabled).forEach(Module::enable);
        KamiMod.MODULE_MANAGER.getModule(RunConfig.class).enable();
        KamiMod.log.info("KAMI Blue Mod initialized!\n");
        UtilHelper.main();
    }
    
    public static String getConfigName() {
        final Path config = Paths.get("KAMIBlueLastConfig.txt", new String[0]);
        String kamiConfigName = "KAMIBlueConfig.json";
        try (final BufferedReader reader = Files.newBufferedReader(config)) {
            kamiConfigName = reader.readLine();
            if (!isFilenameValid(kamiConfigName)) {
                kamiConfigName = "KAMIBlueConfig.json";
            }
        }
        catch (NoSuchFileException e3) {
            try (final BufferedWriter writer = Files.newBufferedWriter(config, new OpenOption[0])) {
                writer.write("KAMIBlueConfig.json");
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return kamiConfigName;
    }
    
    public static void loadConfiguration() {
        try {
            loadConfigurationUnsafe();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void loadConfigurationUnsafe() throws IOException {
        final String kamiConfigName = getConfigName();
        final Path kamiConfig = Paths.get(kamiConfigName, new String[0]);
        if (!Files.exists(kamiConfig, new LinkOption[0])) {
            return;
        }
        Configuration.loadConfiguration(kamiConfig);
        final JsonObject gui = KamiMod.INSTANCE.guiStateSetting.getValue();
        for (final Map.Entry<String, JsonElement> entry : gui.entrySet()) {
            final Optional<Component> optional = KamiMod.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).filter(component -> component.getTitle().equals(entry.getKey())).findFirst();
            if (optional.isPresent()) {
                final JsonObject object = entry.getValue().getAsJsonObject();
                final Frame frame = optional.get();
                frame.setX(object.get("x").getAsInt());
                frame.setY(object.get("y").getAsInt());
                final Docking docking = Docking.values()[object.get("docking").getAsInt()];
                if (docking.isLeft()) {
                    ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.LEFT);
                }
                else if (docking.isRight()) {
                    ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.RIGHT);
                }
                else if (docking.isCenterVertical()) {
                    ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.CENTER);
                }
                frame.setDocking(docking);
                frame.setMinimized(object.get("minimized").getAsBoolean());
                frame.setPinned(object.get("pinned").getAsBoolean());
            }
            else {
                System.err.println("Found GUI config entry for " + entry.getKey() + ", but found no frame with that name");
            }
        }
        getInstance().getGuiManager().getChildren().stream().filter(component -> component instanceof Frame && component.isPinnable() && component.isVisible()).forEach(component -> component.setOpacity(0.0f));
    }
    
    public static void saveConfiguration() {
        try {
            saveConfigurationUnsafe();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveConfigurationUnsafe() throws IOException {
        final JsonObject object = new JsonObject();
        final JsonObject frameObject;
        final JsonObject jsonObject;
        KamiMod.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).map(component -> component).forEach(frame -> {
            frameObject = new JsonObject();
            frameObject.add("x", (JsonElement)new JsonPrimitive((Number)frame.getX()));
            frameObject.add("y", (JsonElement)new JsonPrimitive((Number)frame.getY()));
            frameObject.add("docking", (JsonElement)new JsonPrimitive((Number)Arrays.asList(Docking.values()).indexOf(frame.getDocking())));
            frameObject.add("minimized", (JsonElement)new JsonPrimitive(Boolean.valueOf(frame.isMinimized())));
            frameObject.add("pinned", (JsonElement)new JsonPrimitive(Boolean.valueOf(frame.isPinned())));
            jsonObject.add(frame.getTitle(), (JsonElement)frameObject);
            return;
        });
        KamiMod.INSTANCE.guiStateSetting.setValue(object);
        final Path outputFile = Paths.get(getConfigName(), new String[0]);
        if (!Files.exists(outputFile, new LinkOption[0])) {
            Files.createFile(outputFile, (FileAttribute<?>[])new FileAttribute[0]);
        }
        Configuration.saveConfiguration(outputFile);
        KamiMod.MODULE_MANAGER.getModules().forEach(Module::destroy);
    }
    
    public static boolean isFilenameValid(final String file) {
        final File f = new File(file);
        try {
            f.getCanonicalPath();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    
    public static KamiMod getInstance() {
        return KamiMod.INSTANCE;
    }
    
    public KamiGUI getGuiManager() {
        return this.guiManager;
    }
    
    public CommandManager getCommandManager() {
        return this.commandManager;
    }
    
    public void updateCheck() {
        try {
            KamiMod.log.info("Attempting Cr4tiv3h4ck update check...");
            final JsonParser parser = new JsonParser();
            final String latestVersion = parser.parse(IOUtils.toString(new URL("https://raw.githubusercontent.com/kami-blue/assets/assets/assets/updateChecker.json"))).getAsJsonObject().getAsJsonObject("version").get("1.12.2-latest").getAsString();
            KamiMod.isLatest = latestVersion.equals("v1.1.3");
            KamiMod.latest = latestVersion;
            if (!KamiMod.isLatest) {
                KamiMod.log.warn("You are running an outdated version of Cr4tiv3h4ck.\nCurrent: v1.1.3\nLatest: " + latestVersion);
                return;
            }
            KamiMod.log.info("Your Cr4tiv3h4cke (v1.1.3) is up-to-date with the latest stable release.");
        }
        catch (IOException e) {
            KamiMod.latest = null;
            KamiMod.log.error("Oes noes! An exception was thrown during the update check.");
            e.printStackTrace();
        }
    }
    
    static {
        log = LogManager.getLogger("KAMI Blue");
        EVENT_BUS = new EventManager();
        MODULE_MANAGER = new ModuleManager();
        KamiMod.hasAskedToUpdate = true;
    }
}

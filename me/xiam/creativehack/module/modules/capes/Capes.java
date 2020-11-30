// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.module.modules.capes;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import java.io.File;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import me.xiam.creativehack.util.Wrapper;
import net.minecraft.client.renderer.IImageBuffer;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.image.BufferedImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.entity.AbstractClientPlayer;
import me.xiam.creativehack.KamiMod;
import java.io.Reader;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.Collections;
import java.util.HashMap;
import me.xiam.creativehack.setting.Settings;
import java.util.Map;
import me.xiam.creativehack.setting.Setting;
import me.xiam.creativehack.module.Module;

@Info(name = "Capes", category = Category.CLIENT, description = "Controls the display of KAMI Blue capes", showOnArray = ShowOnArray.OFF)
public class Capes extends Module
{
    public Setting<Boolean> overrideOtherCapes;
    public static Capes INSTANCE;
    private Map<String, CachedCape> allCapes;
    private boolean hasBegunDownload;
    
    public Capes() {
        this.overrideOtherCapes = this.register(Settings.b("Override Other Capes", true));
        this.allCapes = Collections.unmodifiableMap((Map<? extends String, ? extends CachedCape>)new HashMap<String, CachedCape>());
        this.hasBegunDownload = false;
        Capes.INSTANCE = this;
    }
    
    public void onEnable() {
        if (!this.hasBegunDownload) {
            this.hasBegunDownload = true;
            new Thread() {
                @Override
                public void run() {
                    try {
                        final HttpsURLConnection connection = (HttpsURLConnection)new URL("https://raw.githubusercontent.com/kami-blue/assets/assets/assets/capes.json").openConnection();
                        connection.connect();
                        final CapeUser[] capeUser = (CapeUser[])new Gson().fromJson((Reader)new InputStreamReader(connection.getInputStream()), (Class)CapeUser[].class);
                        connection.disconnect();
                        final HashMap<String, CachedCape> capesByURL = new HashMap<String, CachedCape>();
                        final HashMap<String, CachedCape> capesByUUID = new HashMap<String, CachedCape>();
                        for (final CapeUser cape : capeUser) {
                            CachedCape o = capesByURL.get(cape.url);
                            if (o == null) {
                                o = new CachedCape(cape);
                                capesByURL.put(cape.url, o);
                            }
                            capesByUUID.put(cape.uuid, o);
                        }
                        Capes.this.allCapes = (Map<String, CachedCape>)Collections.unmodifiableMap((Map<?, ?>)capesByUUID);
                    }
                    catch (Exception e) {
                        KamiMod.log.error("Failed to load capes");
                    }
                }
            }.start();
        }
    }
    
    public static ResourceLocation getCapeResource(final AbstractClientPlayer player) {
        final CachedCape result = Capes.INSTANCE.allCapes.get(player.getUniqueID().toString());
        if (result == null) {
            return null;
        }
        result.request();
        return result.location;
    }
    
    private static BufferedImage parseCape(final BufferedImage img) {
        int imageWidth = 64;
        int imageHeight = 32;
        for (int srcWidth = img.getWidth(), srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageWidth *= 2, imageHeight *= 2) {}
        final BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
        final Graphics g = imgNew.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return imgNew;
    }
    
    private static String formatUUID(final String uuid) {
        return uuid.replaceAll("-", "");
    }
    
    public class CapeUser
    {
        public String uuid;
        public String url;
    }
    
    private static class CachedCape
    {
        public final ResourceLocation location;
        public final String url;
        private boolean hasRequested;
        
        public CachedCape(final CapeUser cape) {
            this.hasRequested = false;
            this.location = new ResourceLocation("capes/kami/" + formatUUID(cape.uuid));
            this.url = cape.url;
        }
        
        public void request() {
            if (this.hasRequested) {
                return;
            }
            this.hasRequested = true;
            final IImageBuffer iib = (IImageBuffer)new IImageBuffer() {
                public BufferedImage parseUserSkin(final BufferedImage image) {
                    return parseCape(image);
                }
                
                public void skinAvailable() {
                }
            };
            final TextureManager textureManager = Wrapper.getMinecraft().getTextureManager();
            textureManager.getTexture(this.location);
            final ThreadDownloadImageData textureCape = new ThreadDownloadImageData((File)null, this.url, (ResourceLocation)null, iib);
            textureManager.loadTexture(this.location, (ITextureObject)textureCape);
        }
    }
}

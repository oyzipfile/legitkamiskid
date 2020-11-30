// 
// Decompiled by Procyon v0.5.36
// 

package me.xiam.creativehack.util;

import java.util.Iterator;
import java.util.Arrays;
import java.io.Reader;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import me.xiam.creativehack.KamiMod;
import java.awt.Desktop;
import java.net.URI;

public class WebUtils
{
    public static void openWebLink(final URI url) {
        try {
            Desktop.getDesktop().browse(url);
        }
        catch (IOException e) {
            KamiMod.log.error("Couldn't open link: " + url.toString());
        }
    }
    
    public static List<GithubUser> getContributors() {
        KamiMod.log.info("Attempting to get contributors from github api...");
        final List<GithubUser> contributorsAsList = new LinkedList<GithubUser>((Collection<? extends GithubUser>)Collections.emptyList());
        HttpsURLConnection connection;
        GithubUser[] contributors;
        final List list;
        new Thread(() -> {
            try {
                connection = (HttpsURLConnection)new URL("https://api.github.com/repos/kami-blue/client/contributors").openConnection();
                connection.connect();
                contributors = (GithubUser[])new Gson().fromJson((Reader)new InputStreamReader(connection.getInputStream()), (Class)GithubUser[].class);
                connection.disconnect();
                list.addAll(Arrays.asList(contributors));
            }
            catch (Throwable t) {
                KamiMod.log.error("Attempt to get contributors from github api failed.\nError:\n\n" + t.toString());
            }
            return;
        }).start();
        return contributorsAsList;
    }
    
    public static List<GithubUser> getContributors(final List<Integer> exceptions) {
        KamiMod.log.info("Attempting to get contributors from github api...");
        final List<GithubUser> contributorsAsList = new LinkedList<GithubUser>((Collection<? extends GithubUser>)Collections.emptyList());
        HttpsURLConnection connection;
        GithubUser[] contributors;
        final GithubUser[] array;
        int length;
        int i = 0;
        GithubUser githubUser;
        final List<GithubUser> list;
        final Iterator<Integer> iterator;
        int exception;
        new Thread(() -> {
            try {
                connection = (HttpsURLConnection)new URL("https://api.github.com/repos/kami-blue/client/contributors").openConnection();
                connection.connect();
                contributors = (GithubUser[])new Gson().fromJson((Reader)new InputStreamReader(connection.getInputStream()), (Class)GithubUser[].class);
                connection.disconnect();
                for (length = array.length; i < length; ++i) {
                    githubUser = array[i];
                    list.add(githubUser);
                    exceptions.iterator();
                    while (iterator.hasNext()) {
                        exception = iterator.next();
                        if (githubUser.id == exception) {
                            list.remove(githubUser);
                        }
                    }
                }
            }
            catch (Throwable t) {
                KamiMod.log.error("Attempt to get contributors from github api failed.\nError:\n\n" + t.toString());
                MessageSendHelper.sendErrorMessage("Attempt to get contributors from github api failed.\nError:\n\n" + t.toString());
            }
            return;
        }).start();
        return contributorsAsList;
    }
    
    public class GithubUser
    {
        public String login;
        public int id;
        public String contributions;
    }
}

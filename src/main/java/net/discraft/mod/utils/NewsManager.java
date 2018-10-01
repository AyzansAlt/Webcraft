package net.discraft.mod.utils;

import com.google.common.collect.ImmutableList;
import net.discraft.mod.DiscraftReferences;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NewsManager {

    private static final URL NEWS_URL;
    private static final URL NEWS_URL_BATTLEGROUNDS;
    private static final URL UPDATE_URL;

    public static File newsCacheFile = new File("cache/news.txt");
    public static File updatesCacheFile = new File("cache/updates.txt");
    public static File cacheFolder = new File("cache/");

    static {
        try {
            NEWS_URL = new URL(DiscraftReferences.externalLocation + "news.txt");
            NEWS_URL_BATTLEGROUNDS = new URL(DiscraftReferences.externalLocation + "news_battlegrounds.txt");
            UPDATE_URL = new URL(DiscraftReferences.externalLocation + "updates.txt");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompletableFuture<List<String>> fetchNews() {
        return CompletableFuture.supplyAsync(NewsManager::downloadNews);
    }

    public static CompletableFuture<List<String>> fetchNewsBattlegrounds() {
        return CompletableFuture.supplyAsync(NewsManager::downloadNewsBattlegrounds);
    }

    public static CompletableFuture<List<String>> fetchUpdates() {
        return CompletableFuture.supplyAsync(NewsManager::downloadUpdates);
    }

    public static ImmutableList<String> downloadNews() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(NEWS_URL.openStream()));
             Stream<String> lines = reader.lines()) {
            reader.close();
            return ImmutableList.copyOf(lines.toArray(String[]::new));
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    }

    public static ImmutableList<String> downloadNewsBattlegrounds() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(NEWS_URL_BATTLEGROUNDS.openStream()));
             Stream<String> lines = reader.lines()) {
            reader.close();
            return ImmutableList.copyOf(lines.toArray(String[]::new));
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    }

    public static ImmutableList<String> downloadUpdates() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(UPDATE_URL.openStream()));
             Stream<String> lines = reader.lines()) {
            reader.close();
            return ImmutableList.copyOf(lines.toArray(String[]::new));
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    }

    public static List<String> getNewsAsStringList() throws IOException {

        if (!cacheFolder.exists()) {
            cacheFolder.mkdirs();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(NEWS_URL.openStream()));
        List<String> lines = reader.lines().collect(Collectors.toList());

        if (!newsCacheFile.exists()) {
            newsCacheFile.createNewFile();
        }

        FileUtils.writeStringToFile(newsCacheFile, "" + lines.hashCode(), Charset.defaultCharset());

        return lines;

    }

    public static List<String> getUpdatesAsStringList() throws IOException {

        if (!cacheFolder.exists()) {
            cacheFolder.mkdirs();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(UPDATE_URL.openStream()));
        List<String> lines = reader.lines().collect(Collectors.toList());

        if (!updatesCacheFile.exists()) {
            updatesCacheFile.createNewFile();
        }

        FileUtils.writeStringToFile(updatesCacheFile, "" + lines.hashCode(), Charset.defaultCharset());

        reader.close();

        return lines;

    }

    public static boolean mustCheckNews() throws IOException {

        if (newsCacheFile.exists()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(NEWS_URL.openStream()));
            List<String> lines = reader.lines().collect(Collectors.toList());

            float hashGiven = Float.parseFloat("" + lines.hashCode());
            float hashKnown = Float.parseFloat("" + readFile(newsCacheFile));

            return !(hashGiven == hashKnown);
        }
        return true;

    }

    public static boolean mustCheckUpdates() throws IOException {

        if (updatesCacheFile.exists()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(UPDATE_URL.openStream()));
            List<String> lines = reader.lines().collect(Collectors.toList());

            float hashGiven = Float.parseFloat("" + lines.hashCode());
            float hashKnown = Float.parseFloat("" + readFile(updatesCacheFile));

            return !(hashGiven == hashKnown);
        }

        return true;

    }

    private static String readFile(File file) throws IOException {
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");

            try {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }

                return stringBuilder.toString();
            } finally {
                reader.close();
            }
        } else {
            return "unknown";
        }
    }

}

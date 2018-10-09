package net.discraft.mod.utils;

import net.discraft.mod.Discraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DiscraftCrashChecker {

    public File folder = new File("/crash-reports/");
    public boolean hasCrashFiles = false;
    public ArrayList<File> crashReports = new ArrayList<>();

    /**
     * Initialize - Find and log all crash reports
     */
    public void init() {

        /* If crash reports folder doesn't exist, create it */
        if (!folder.exists()) {
            folder.mkdirs();
        }

        crashReports = new ArrayList<>();

        /* For each crash report file, check if it's discraft-related, then add to list */
        if (folder.listFiles() != null && folder.listFiles().length > 0) {
            for (File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    if (doesFileContainDiscraft(fileEntry)) {
                        Discraft.getInstance().getLogger().printError("Discraft", "Found report '" + fileEntry.getName() + "' in crash-reports directory!");
                        crashReports.add(fileEntry);
                    }
                }
            }
        } else {
            Discraft.getInstance().getLogger().printLine("Discraft", "No reports found in crash-reports directory!");
        }

    }

    public void sendCrashReports() {

        for (File file : crashReports) {


        }

    }

    public boolean doesFileContainDiscraft(File givenFIle) {

        try {
            Scanner scanner = new Scanner(givenFIle);
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                if (line.contains("discraft")) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            //handle this
        }

        return false;

    }

}

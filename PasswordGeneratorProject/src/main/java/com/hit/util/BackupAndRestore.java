package com.hit.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class BackupAndRestore {

    // Note! class is tested as part of "LoginManagementServicesTest"
    Timer timer;

    public BackupAndRestore() {
        timer = new Timer();
    }

    public void backup(String fromFilePath, String toPathBackup, long delay, long period) {
        TimerTask backupTask = new TimerTask() {

            public void run() {

                // Load files from given path
                File fileToBackup = new File(fromFilePath);
                File fileToCreate = new File(toPathBackup);

                // Make sure file to backup actually exists
                if (!fileToBackup.exists()) {
                    System.out.println("Backup failed! The file to backup was not found.");
                    return;
                }

                // Create backup file
                try {
                    fileToBackup.createNewFile();
                }
                catch (IOException e) {
                    System.out.println("Backup failed! couldn't create backup file - " + e.getMessage());
                    return;
                }

                // Create I/O streams
                FileChannel inputChannel = null;
                FileChannel outputChannel = null;
                try {
                    FileInputStream inputStream = new FileInputStream(fileToBackup);
                    FileOutputStream outputStream = new FileOutputStream(fileToCreate);
                    inputChannel = inputStream.getChannel();
                    outputChannel = outputStream.getChannel();
                    inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                } catch (Exception e) {
                    System.out.println("Backup failed! couldn't copy file contents - " + e.getMessage());
                }

                // Release files when done copying
                finally {
                    try {
                        if (inputChannel != null)
                            inputChannel.close();
                        if (outputChannel != null)
                            outputChannel.close();
                    }
                    catch (Exception e) {
                        System.out.println("Failed to release backup files - " + e.getMessage());
                    }
                }
            }
        };

        timer.scheduleAtFixedRate(backupTask, delay, period);
    }

    public Object restore(String fromFilePath) {

        // We do not deserialize here!
        // Since there are multiple services, we don't know which data was written to a specific backup
        // It's up for whoever called the restore function to parse and cast the file content.
        String content = "";

        try {
            for (String line : Files.readAllLines(Paths.get(fromFilePath))) {
                content += line;
            }
            return content;
        } catch (IOException e) {
            System.out.println("Failed to restore backup files - " + e.getMessage());
            return content;
        }
    }

}

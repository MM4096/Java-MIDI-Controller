package com.mm4096.midicontroller.tools;

import java.nio.file.Paths;

public class FileHelper {
    public static String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }

        return extension;
    }

    public static String getFileName(String fileName) {
        String name = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            name = fileName.substring(0, i);
        }

        return name;
    }

    public static String getUserDataDirectory() {
        return Paths.get(System.getProperty("user.home"), ".midicontroller").toString();
    }

    public static String getPatchDirectory() {
        return Paths.get(getUserDataDirectory(), "patches").toString();
    }

    public static String getConfigDirectory() {
        return Paths.get(getUserDataDirectory(), "config").toString();
    }

    public static String getTempDirectory() {
        return Paths.get(getUserDataDirectory(), ".temp").toString();
    }

    public static void createDirectories() {
        boolean r1 = Paths.get(getPatchDirectory()).toFile().mkdirs();
        boolean r2 = Paths.get(getConfigDirectory()).toFile().mkdirs();
        boolean r3 = Paths.get(getTempDirectory()).toFile().mkdirs();
    }

    public static void writeToFile(String path, String content) {
        try {
            java.nio.file.Files.write(Paths.get(path), content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(String path) {
        try {
            return new String(java.nio.file.Files.readAllBytes(Paths.get(path)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String[] readLinesFromFile(String path) {
        try {
            return new String(java.nio.file.Files.readAllBytes(Paths.get(path))).split("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    public static String[] getFiles(String path) {
        return Paths.get(path).toFile().list();
    }

    public static boolean isFile(String path) {
        return Paths.get(path).toFile().isFile();
    }

    public static boolean isDirectory(String path) {
        return Paths.get(path).toFile().isDirectory();
    }

    public static boolean fileExists(String path) {
        return Paths.get(path).toFile().exists();
    }
}

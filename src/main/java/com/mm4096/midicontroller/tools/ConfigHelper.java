package com.mm4096.midicontroller.tools;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigHelper {
    public static boolean configExists(String configPath) {
        return FileHelper.fileExists(Paths.get(FileHelper.getConfigDirectory(), configPath).toString());
    }
}

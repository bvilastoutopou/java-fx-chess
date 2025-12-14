package com.example.chess;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsManager {
    private static final String fileName = "config.properties";
    private static Properties properties = new Properties();

    static {
        try (FileInputStream in = new FileInputStream(fileName)) {
            properties.load(in);
        } catch (IOException e) {
            properties.setProperty("theme", "0");
            properties.setProperty("language", "en");
            properties.setProperty("FEN","rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
            save();
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static void set(String key, String value) {
        properties.setProperty(key, value);
        save();
    }

    private static void save() {
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            properties.store(out, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

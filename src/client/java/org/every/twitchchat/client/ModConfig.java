package org.every.twitchchat.client;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.every.twitchchat.Twitchchat;

public class ModConfig {
    private static final Path CONFIG_PATH = Path.of("config", "twitchchat.json");
    private String twitchChannel = "";
    private String formatMessage = "✌ %s: %s";

    public void load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                ModConfig temp = new Gson().fromJson(json, ModConfig.class);

                if (temp.twitchChannel != null) {
                    this.twitchChannel = temp.twitchChannel;
                }
                if (temp.formatMessage != null) {
                    this.formatMessage = temp.formatMessage;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            JsonObject obj = new JsonObject();
            obj.addProperty("twitchChannel", twitchChannel);
            obj.addProperty("formatMessage", formatMessage);
            Files.writeString(CONFIG_PATH, new Gson().toJson(obj));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getValue(String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field field = getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(this);
    }

    public void setValue(String fieldName, Object value) throws IllegalAccessException, NoSuchFieldException {
        Field privateField = getClass().getDeclaredField(fieldName);
        privateField.setAccessible(true); // Make it accessible
        Twitchchat.LOGGER.info("Original private variable: " + privateField.get(this));
        privateField.set(this, value);
        Twitchchat.LOGGER.info("Vars: " + Arrays.toString(getClass().getDeclaredFields()));
        save();
    }
}
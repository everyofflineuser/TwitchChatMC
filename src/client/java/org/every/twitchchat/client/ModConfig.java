package org.every.twitchchat.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ModConfig {
    private static final Path CONFIG_PATH = Path.of("config", "twitchchat.json");
    private String twitchChannel = "";

    public void load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                JsonObject obj = new Gson().fromJson(json, JsonObject.class);
                twitchChannel = obj.get("twitchChannel").getAsString();
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
            Files.writeString(CONFIG_PATH, new Gson().toJson(obj));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTwitchChannel() {
        return twitchChannel;
    }

    public void setTwitchChannel(String channel) {
        this.twitchChannel = channel;
        save();
    }
}
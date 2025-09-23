package org.every.twitchchat.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import com.mojang.brigadier.arguments.StringArgumentType;
import org.every.twitchchat.Twitchchat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public class TwitchchatClient implements ClientModInitializer {
    private static Twitch4jClient twitch4jClient;
    private static ModConfig config;

    @Override
    public void onInitializeClient() {
        config = new ModConfig();
        config.load();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(literal("twitchchat")
                    .then(argument("channel", StringArgumentType.word())
                            .executes(context -> {
                                String channel = StringArgumentType.getString(context, "channel");
                                try {
                                    config.setValue("twitchChannel", channel);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                } catch (NoSuchFieldException e) {
                                    throw new RuntimeException(e);
                                }
                                restartTwitchClient();
                                context.getSource().sendFeedback(Text.literal("Twitch channel set to: " + channel));
                                return 1;
                            }))
                    .executes(context -> {
                        try {
                            String twitchChannel = config.getValue("twitchChannel").toString();
                            context.getSource().sendFeedback(Text.literal("Current Twitch channel: " +
                                    (twitchChannel.isEmpty() ? "not set" : twitchChannel)));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (NoSuchFieldException e) {
                            throw new RuntimeException(e);
                        }
                        return 1;
                    }));
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            if (twitch4jClient != null) {
                twitch4jClient.disconnect();
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (twitch4jClient != null) {
                twitch4jClient.disconnect();
            }
        });
        ClientPlayConnectionEvents.JOIN.register((handler,packetSender,client) -> {
            if (twitch4jClient == null) {
                restartTwitchClient();
            }
        });
    }

    private static void restartTwitchClient() {
        Twitchchat.LOGGER.info("Attempting to restart Twitch client...");
        if (twitch4jClient != null) {
            twitch4jClient.disconnect();
        }
        try {
            String twitchChannel = (String) config.getValue("twitchChannel");
            Twitchchat.LOGGER.info("Config twitchChannel: '" + twitchChannel + "'");
            if (twitchChannel != null && !twitchChannel.isEmpty()) {
                twitch4jClient = new Twitch4jClient(twitchChannel);
                twitch4jClient.connect();
                Twitchchat.LOGGER.info("Twitch client connection initiated.");

                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player != null) {
                    client.player.sendMessage(Text.literal("[TwitchChat] Attempting connect to Channel: " + twitchChannel), false);
                }
            } else {
                Twitchchat.LOGGER.info("Twitch channel is not set. Client not started.");
            }
        } catch (Exception e) {
            Twitchchat.LOGGER.error("Error restarting Twitch client: " + e.getMessage());
            e.printStackTrace();

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                client.player.sendMessage(Text.literal("[Twitch] Error connection: " + e.getMessage()), false);
            }
        }
    }

    public static void onTwitchMessage(String username, String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        //Text twitchPrefix = Text.literal("[Twitch] ")
        //        .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x9146FF)));
        //Text twitchPrefix = Text.literal("✌ ");

        //Text usernameText = Text.literal(username + ": ");

        //Text messageText = Text.literal(message)
        //        .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xE0E0E0)));

        try {
            String formatMessage = (String) config.getValue("formatMessage");
            if (formatMessage != null && !formatMessage.isEmpty()) {
                String formattedMessage = String.format(formatMessage, username, message);
                client.player.sendMessage(Text.of(formattedMessage), false);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Twitchchat.LOGGER.error("Error accessing formatMessage field: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
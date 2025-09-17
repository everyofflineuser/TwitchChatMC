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
                                config.setTwitchChannel(channel);
                                restartTwitchClient();
                                context.getSource().sendFeedback(Text.literal("Twitch channel set to: " + channel));
                                return 1;
                            }))
                    .executes(context -> {
                        context.getSource().sendFeedback(Text.literal("Current Twitch channel: " +
                                (config.getTwitchChannel().isEmpty() ? "not set" : config.getTwitchChannel())));
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
        if (twitch4jClient != null) {
            twitch4jClient.disconnect();
        }
        if (!config.getTwitchChannel().isEmpty()) {
            twitch4jClient = new Twitch4jClient(config.getTwitchChannel());
            twitch4jClient.connect();
        }
    }

    public static void onTwitchMessage(String username, String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        //Text twitchPrefix = Text.literal("[Twitch] ")
        //        .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x9146FF)));
        Text twitchPrefix = Text.literal("✌ ");

        Text usernameText = Text.literal(username + ": ");

        Text messageText = Text.literal(message)
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xE0E0E0)));

        client.player.sendMessage(Text.empty()
                .append(twitchPrefix)
                .append(usernameText)
                .append(messageText), false);
    }
}
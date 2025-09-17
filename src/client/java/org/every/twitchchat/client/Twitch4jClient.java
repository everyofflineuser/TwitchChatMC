package org.every.twitchchat.client;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

public class Twitch4jClient {
    private final String channel;
    private TwitchClient twitchClient;

    public Twitch4jClient(String channel) {
        this.channel = channel.toLowerCase();
    }

    public void connect() {
        twitchClient = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .build();

        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, event -> {
            if (event.getChannel().getName().equalsIgnoreCase(channel)) {
                TwitchchatClient.onTwitchMessage(
                        event.getUser().getName(),
                        event.getMessage()
                );
            }
        });

        twitchClient.getChat().joinChannel(channel);
    }

    public void disconnect() {
        if (twitchClient != null) {
            twitchClient.getChat().leaveChannel(channel);
            twitchClient.close();
        }
    }

    private int parseColor(String color) {
        try {
            return color != null ? Integer.parseInt(color.substring(1), 16) : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
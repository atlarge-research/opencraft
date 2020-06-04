package net.glowstone.util.config;

import lombok.Data;

@Data
public class BrokerConfig {

    private final String type;
    private final String host;
    private final Integer port;
    private final String virtualHost;
    private final String username;
    private final String password;
    private final ChannelConfig channel;
}

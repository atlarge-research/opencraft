package net.glowstone.util.config;

import lombok.Data;

/**
 * The broker config provides a collection of configuration options that can be parsed together for use in the broker
 * factory.
 */
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

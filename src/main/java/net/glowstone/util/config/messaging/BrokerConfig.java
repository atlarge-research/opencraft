package net.glowstone.util.config.messaging;

import lombok.Data;

/**
 * The broker config provides a collection of configuration options that can be parsed together for use in the broker
 * factory.
 */
@Data
public final class BrokerConfig {

    private final ChannelConfig channel = new ChannelConfig();

    private final String type = "readwrite";
    private String host;
    private Integer port;
    private String virtualHost;
    private String username;
    private String password;
}

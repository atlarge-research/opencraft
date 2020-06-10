package net.glowstone.util.config;

import lombok.Data;

/**
 * The channel config provides a collection of configuration options that can be parsed together for use in the broker
 * factory, in particular in the newChannelFactory method.
 */
@Data
public class ChannelConfig {
    private final String type;
    private final Integer parallelismThreshold;
}

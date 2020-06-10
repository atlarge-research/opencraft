package net.glowstone.util.config.messaging;

import lombok.Data;

/**
 * The channel config provides a collection of configuration options that can be parsed together for use in the broker
 * factory, in particular in the newChannelFactory method.
 */
@Data
public final class ChannelConfig {

    private String type = "unsafe";
    private Integer parallelismThreshold;
}

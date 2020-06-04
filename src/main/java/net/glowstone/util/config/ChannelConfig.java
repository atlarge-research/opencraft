package net.glowstone.util.config;

import lombok.Data;

@Data
public class ChannelConfig {
    private final String type;
    private final Integer parallelismThreshold;
}

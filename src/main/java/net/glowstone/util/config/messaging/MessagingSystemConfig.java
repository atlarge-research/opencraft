package net.glowstone.util.config.messaging;

import lombok.Data;

/**
 * The MessagingSystemConfig provides the configuration options required to built a messaging system.
 */
@Data
public class MessagingSystemConfig {

    private final BrokerConfig broker = new BrokerConfig();

    private String policy;
}

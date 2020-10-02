package science.atlarge.opencraft.opencraft.util.config;

import lombok.Data;

/**
 * The broker config provides a collection of configuration options that can be parsed together for use in the broker
 * factory.
 */
@Data
public class BrokerConfig {

    private final BrokerType type;

    // Asynchronous options
    private final Boolean async;
    private final Integer threads;
    private final Integer capacity;

    // Channel options
    private final ChannelType channel;

    // JMS options
    private final String host;
    private final Integer port;
    private final String username;
    private final String password;

    // RabbitMQ options
    private final String virtualHost;
}

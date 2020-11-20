package science.atlarge.opencraft.opencraft.messaging;

import com.google.common.collect.ImmutableMap;
import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.util.config.ServerConfig;

public class MessagingFactory {

    final private static ImmutableMap<String, MessagingProvider> messagingProviderMap = ImmutableMap.of(
            "dyconit", new DyconitMessagingProvider(),
            "pubsub", new PubSubMessagingProvider()
    );

    public static Messaging fromConfig(GlowWorld world, GlowServer server) {
        String type = server.getConfig().getString(ServerConfig.Key.OPENCRAFT_MESSAGING_TYPE);
        MessagingProvider provider = messagingProviderMap.get(type);
        if (provider == null) {
            throw new RuntimeException("Messaging system '" + type + "' does not exist.");
        }
        GlowServer.logger.info("Using " + type + " messaging system");
        return provider.build(server, world);
    }
}

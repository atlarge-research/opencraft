package science.atlarge.opencraft.opencraft.messaging;

import com.flowpowered.network.Message;
import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.messaging.codecs.CompositeCodec;
import science.atlarge.opencraft.opencraft.messaging.dyconits.policies.ChunkPolicy;
import science.atlarge.opencraft.opencraft.messaging.filters.FeedbackFilter;
import science.atlarge.opencraft.opencraft.util.config.ServerConfig;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import science.atlarge.opencraft.dyconits.DyconitSystem;
import science.atlarge.opencraft.messaging.Broker;
import science.atlarge.opencraft.messaging.Filter;
import science.atlarge.opencraft.messaging.MessagingSystem;

public class MessagingFactory {

    private static DyconitMessaging dyconitMessagingSystem;

    public static Messaging fromConfig(GlowWorld world, GlowServer server) {
        String type = server.getConfig().getString(ServerConfig.Key.OPENCRAFT_MESSAGING_TYPE);
        Messaging res;
        if (type.equals("dyconit")) {
            if (dyconitMessagingSystem == null) {
                dyconitMessagingSystem = new DyconitMessaging(new DyconitSystem<>(new ChunkPolicy(server.getViewDistance()), new FeedbackFilter(), true));
            }
            res = dyconitMessagingSystem;
        } else { // if type.equals("pubsub")
            type = "pubsub";
            science.atlarge.opencraft.opencraft.messaging.policies.ChunkPolicy policy = new science.atlarge.opencraft.opencraft.messaging.policies.ChunkPolicy(world, server.getViewDistance());
            Broker<Chunk, Player, Message> broker = Brokers.newBroker(server.getBrokerConfig(), CompositeCodec::new);
            Filter<Player, Message> filter = new FeedbackFilter();
            res = new PubSubMessaging(new MessagingSystem<>(policy, broker, filter));
        }
        GlowServer.logger.info("Using " + type + " messaging system.");
        return res;
    }
}

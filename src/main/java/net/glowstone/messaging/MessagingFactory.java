package net.glowstone.messaging;

import com.flowpowered.network.Message;
import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;
import net.glowstone.messaging.codecs.CompositeCodec;
import net.glowstone.messaging.dyconits.policies.DyconitChunkPolicy;
import net.glowstone.messaging.filters.FeedbackFilter;
import net.glowstone.messaging.policies.ChunkPolicy;
import net.glowstone.util.config.ServerConfig;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import science.atlarge.opencraft.dyconits.DyconitSystem;
import science.atlarge.opencraft.messaging.Broker;
import science.atlarge.opencraft.messaging.Filter;
import science.atlarge.opencraft.messaging.MessagingSystem;

public class MessagingFactory {

    DyconitMessaging dyconitMessagingSystem;

    public Messaging fromConfig(GlowWorld world, GlowServer server) {
        String type = server.getConfig().getString(ServerConfig.Key.OPENCRAFT_MESSAGING_TYPE);
        Messaging res;
        if (type.equals("dyconit")) {
            if (dyconitMessagingSystem == null) {
                dyconitMessagingSystem = new DyconitMessaging(new DyconitSystem<>(new DyconitChunkPolicy(server.getViewDistance()), new FeedbackFilter()));
            }
            res = dyconitMessagingSystem;
        } else { // if type.equals("pubsub")
            type = "pubsub";
            ChunkPolicy policy = new ChunkPolicy(world, server.getViewDistance());
            Broker<Chunk, Player, Message> broker = Brokers.newBroker(server.getBrokerConfig(), CompositeCodec::new);
            Filter<Player, Message> filter = new FeedbackFilter();
            res = new PubSubMessaging(new MessagingSystem<>(policy, broker, filter));
        }
        GlowServer.logger.info("Using " + type + " messaging system.");
        return res;
    }
}

package science.atlarge.opencraft.opencraft.messaging;

import com.flowpowered.network.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import science.atlarge.opencraft.messaging.Broker;
import science.atlarge.opencraft.messaging.Filter;
import science.atlarge.opencraft.messaging.MessagingSystem;
import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.messaging.codecs.CompositeCodec;
import science.atlarge.opencraft.opencraft.messaging.filters.FeedbackFilter;

public class PubSubMessagingProvider implements MessagingProvider {
    @Override
    public Messaging build(GlowServer server, GlowWorld world) {
        science.atlarge.opencraft.opencraft.messaging.policies.ChunkPolicy policy = new science.atlarge.opencraft.opencraft.messaging.policies.ChunkPolicy(world, server.getViewDistance());
        Broker<Chunk, Player, Message> broker = Brokers.newBroker(server.getBrokerConfig(), CompositeCodec::new);
        Filter<Player, Message> filter = new FeedbackFilter();
        return new PubSubMessaging(new MessagingSystem<>(policy, broker, filter));
    }
}

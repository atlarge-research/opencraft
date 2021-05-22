package science.atlarge.opencraft.opencraft.messaging;

import com.flowpowered.network.Message;
import org.bukkit.entity.Player;
import science.atlarge.opencraft.dyconits.DyconitSystem;
import science.atlarge.opencraft.dyconits.MessageListQueue;
import science.atlarge.opencraft.dyconits.MessageQueueFactory;
import science.atlarge.opencraft.dyconits.policies.DyconitPolicy;
import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.messaging.dyconits.MergeMessageQueue;
import science.atlarge.opencraft.opencraft.messaging.dyconits.policies.PolicyFactory;
import science.atlarge.opencraft.opencraft.messaging.filters.FeedbackFilter;
import science.atlarge.opencraft.opencraft.util.config.ServerConfig;

public class DyconitMessagingProvider implements MessagingProvider {

    private static DyconitMessaging dyconitMessagingSystem;

    @Override
    public Messaging build(GlowServer server, GlowWorld world) {
        if (dyconitMessagingSystem == null) {
            String policyString = server.getConfig().getString(ServerConfig.Key.OPENCRAFT_POLICY);
            DyconitPolicy<Player, Message> policy = PolicyFactory.policyFromString(policyString, server);
            if (policy == null) {
                throw new RuntimeException("Policy '" + policyString + "' does not exist.");
            }
            boolean merge = server.getConfig().getBoolean(ServerConfig.Key.OPENCRAFT_MESSAGE_MERGING);
            MessageQueueFactory<Message> queueFactory = merge ? MergeMessageQueue::new : MessageListQueue::new;
            dyconitMessagingSystem = new DyconitMessaging(new DyconitSystem<>(
                    policy,
                    new FeedbackFilter(),
                    queueFactory
            ));
        }
        return dyconitMessagingSystem;
    }
}

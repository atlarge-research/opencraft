package science.atlarge.opencraft.opencraft.messaging.dyconits.policies.weights;

import com.flowpowered.network.Message;

public interface WeighMessage {
    int weigh(Message message);
}

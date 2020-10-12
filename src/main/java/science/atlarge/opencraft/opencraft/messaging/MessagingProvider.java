package science.atlarge.opencraft.opencraft.messaging;

import science.atlarge.opencraft.opencraft.GlowServer;
import science.atlarge.opencraft.opencraft.GlowWorld;

public interface MessagingProvider {
    Messaging build(GlowServer server, GlowWorld world);
}

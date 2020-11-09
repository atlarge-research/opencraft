package net.glowstone.chunk.policy;

import net.glowstone.GlowWorld;

public class NaiveServerlessChunkLoadingPolicy extends DefaultChunkLoadingPolicy {

    public NaiveServerlessChunkLoadingPolicy(GlowWorld world) {
        super(world);
    }

    public int getPolicyIndex() {
        return 0;
    }
}

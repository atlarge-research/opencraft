package science.atlarge.opencraft.opencraft.chunk.policy;

import science.atlarge.opencraft.opencraft.GlowWorld;

public class NaiveServerlessChunkLoadingPolicy extends DefaultChunkLoadingPolicy {

    public NaiveServerlessChunkLoadingPolicy(GlowWorld world) {
        super(world);
        this.populateServerless = true;
    }

    public int getPolicyIndex() {
        return 0;
    }
}

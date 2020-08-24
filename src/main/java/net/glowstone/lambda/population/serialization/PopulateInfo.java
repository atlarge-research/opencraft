package net.glowstone.lambda.population.serialization;

import com.google.gson.Gson;
import java.util.List;
import java.util.Random;
import net.glowstone.GlowWorld;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.net.message.play.game.BlockChangeMessage;


public class PopulateInfo {
    private static final Gson gson = JsonUtil.getGson();

    public static class PopulateInput {
        public GlowWorld world;
        public Random random;
        public int x;
        public int z;

        public PopulateInput(GlowWorld world, Random random, int x, int z) {
            this.world = world;
            this.random = random;
            this.x = x;
            this.z = z;
        }

        public String toJson() {
            return gson.toJson(this);
        }

        public static PopulateInput fromJson(String jsn) {
            return gson.fromJson(jsn, PopulateInput.class);
        }
    }

    public static class PopulateOutput {
        List<BlockChangeMessage> changedBlocks;
        List<GlowChunk> populatedChunks;

        public PopulateOutput(List<BlockChangeMessage> changedBlocks, List<GlowChunk> populatedChunks) {
            this.changedBlocks = changedBlocks;
            this.populatedChunks = populatedChunks;
        }

        public String toJson() {
            return gson.toJson(this);
        }

        public static PopulateOutput fromJson(String jsn) {
            return gson.fromJson(jsn, PopulateOutput.class);
        }
    }
}

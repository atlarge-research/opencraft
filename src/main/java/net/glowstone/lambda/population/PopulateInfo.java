package net.glowstone.lambda.population;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.glowstone.GlowWorld;
import net.glowstone.block.GlowBlock;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.lambda.population.serialization.GlowSerializer;
import net.glowstone.lambda.population.serialization.json.JsonSerializer;
import net.glowstone.net.message.play.game.BlockChangeMessage;
import net.glowstone.scheduler.PulseTask;


public class PopulateInfo {
    private static final GlowSerializer serializer = new JsonSerializer();

    public static class PopulateInput {
        public GlowWorld world;
        public Random random;
        public ArrayList<GlowChunk> adjacentChunks;
        public int x;
        public int z;

        public PopulateInput(GlowWorld world, Random random, ArrayList<GlowChunk> adjacentChunks, int x, int z) {
            this.world = world;
            this.random = random;
            this.adjacentChunks = adjacentChunks;
            this.x = x;
            this.z = z;
        }

        public String serialize() {
            // do it twice to bypass AWS Lambda auto deserialization
            return serializer.serialize(serializer.serialize(this));
        }

        public static PopulateInput deserialize(String src) {
            return serializer.deserialize(src, PopulateInput.class);
        }
    }

    public static class PopulateOutput {
        public List<BlockChangeMessage> changedBlocks;
        public List<GlowChunk> populatedChunks;
        public List<PulseTaskInfo> pulseTasks;

        public PopulateOutput(GlowWorld world) {
            this.changedBlocks = world.getPopulatedBlockMessages();
            this.populatedChunks = world.getChunkManager().getKnownChunks();
            this.pulseTasks = world.getPopulatedPulseTasks();
        }

        public String serialize() {
            return serializer.serialize(this);
        }

        private static void writeToFile(String jsn) {
            try {
                String fileName = "./logs/lambda-" + System.currentTimeMillis() + ".txt";
                File f = new File(fileName);
                if (f.createNewFile()) {
                    FileWriter fw = new FileWriter(fileName);
                    fw.write(jsn);
                    fw.close();
                    System.out.println("Error written to " + fileName);
                }
            } catch (Exception ex) {
                System.err.println("Failed to write error to file");
            }
        }

        public static PopulateOutput deserialize(String src) {
            // do it twice to bypass AWS Lambda auto serialization
            try {
                PopulateOutput out = serializer.deserialize(serializer.deserialize(src, String.class), PopulateOutput.class);
                if (out == null) {
                    writeToFile(src);
                }
                return out;
            } catch (Exception e) {
                writeToFile(src);
                return null;
            }
        }

        /**
         * Used to serialize PulseTasks generated on AWS Lambda
         */
        public static class PulseTaskInfo {
            private final GlowBlock block;
            private final boolean async;
            private final long delay;
            private final boolean single;

            public PulseTaskInfo(GlowBlock block, boolean async, long delay, boolean single) {
                this.block = block;
                this.async = async;
                this.delay = delay;
                this.single = single;
            }

            public PulseTask getPulseTask(GlowWorld world) {
                block.setWorld(world);
                return new PulseTask(block, async, delay, single);
            }
        }
    }
}

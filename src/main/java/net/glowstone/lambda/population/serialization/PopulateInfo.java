package net.glowstone.lambda.population.serialization;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.glowstone.GlowWorld;
import net.glowstone.block.GlowBlock;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.net.message.play.game.BlockChangeMessage;
import net.glowstone.scheduler.PulseTask;


public class PopulateInfo {
    private static final Gson gson = JsonUtil.getGson();

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

        public String toJson() {
            // do it twice to bypass AWS Lambda auto deserialization
            return gson.toJson(gson.toJson(this));
        }

        public static PopulateInput fromJson(String jsn) {
            return gson.fromJson(jsn, PopulateInput.class);
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

        public String toJson() {
            return gson.toJson(this);
        }

        private static void writeToFile(String jsn) {
            try {
                File f = new File("tmp.txt");
                if (f.createNewFile()) {
                    FileWriter fw = new FileWriter("tmp.txt");
                    fw.write(jsn);
                    fw.close();
                    System.out.println("Error written to file");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public static PopulateOutput fromJson(String jsn) {
            // do it twice to bypass AWS Lambda auto serialization
            try {
                PopulateOutput out = gson.fromJson(gson.fromJson(jsn, String.class), PopulateOutput.class);
                if (out == null) {
                    writeToFile(jsn);
                }
                return out;
            } catch (Exception e) {
                writeToFile(jsn);
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

package science.atlarge.opencraft.opencraft.population;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.chunk.GlowChunk;
import science.atlarge.opencraft.opencraft.io.anvil.AnvilChunkIoService;
import science.atlarge.opencraft.opencraft.population.serialization.GlowSerializer;
import science.atlarge.opencraft.opencraft.population.serialization.json.JsonSerializer;
import science.atlarge.opencraft.opencraft.net.message.play.game.BlockChangeMessage;
import science.atlarge.opencraft.opencraft.scheduler.PulseTask;


public class PopulateInfo {
    private static final GlowSerializer serializer = new JsonSerializer();

    public static class PopulateInput {
        public GlowWorld world;
        public int x;
        public int z;

        public PopulateInput(GlowWorld world, int x, int z) {
            this.world = world;
            this.x = x;
            this.z = z;
        }

        public String serialize() {
            // serialize twice to bypass AWS Lambda auto deserialization
            if (world.getSerializedCache().equals("")) {
                world.setSerializedCache(serializer.serialize(world));
            }
            String worldCache = world.getSerializedCache();
            String inp = String.format("%d,%d,%s", x, z, worldCache);
            return serializer.serialize(inp);
        }

        public static PopulateInput deserialize(String src) {
            String[] deserialized = src.split(",", 3);
            return new PopulateInput(
                    serializer.deserialize(deserialized[2], GlowWorld.class),
                    Integer.parseInt(deserialized[0]),
                    Integer.parseInt(deserialized[1])
            );
            //return serializer.deserialize(src, PopulateInput.class);
        }
    }

    public static class PopulateOutput {
        public List<BlockChangeMessage> changedBlocks;
        public String populatedChunkData;
        public List<PulseTaskInfo> pulseTasks;

        public PopulateOutput(GlowWorld world, GlowChunk populated) {
            this(world, serializer.serialize(populated));
        }

        public PopulateOutput(GlowWorld world, String populated) {
            this.changedBlocks = world.getPopulatedBlockMessages();
            this.populatedChunkData = populated;
            this.pulseTasks = world.getPopulatedPulseTasks();
        }

        public boolean getChunk(GlowChunk chunk) {
            try {
                return AnvilChunkIoService.read(chunk, populatedChunkData);
            } catch (IOException e) {
                return false;
            }
        }

        public String serialize() {
            return serializer.serialize(this);
        }

        private static void dumpErr(String jsn) {
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
                    dumpErr(src);
                }
                return out;
            } catch (Exception e) {
                dumpErr(src);
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

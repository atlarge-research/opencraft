package science.atlarge.opencraft.opencraft.population;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Random;
import java.util.stream.Collectors;

import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.chunk.GlowChunk;
import science.atlarge.opencraft.opencraft.population.PopulateInfo.PopulateInput;
import science.atlarge.opencraft.opencraft.population.PopulateInfo.PopulateOutput;
import org.bukkit.generator.BlockPopulator;


/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<String, String> {
    public String handleRequest(String input, final Context context) {
        // for cold boot prevention
        if (input.equals("")) {
            return "";
        }

        // TODO: error handling
        PopulateInput deserialized = PopulateInput.deserialize(input);

        GlowWorld world = deserialized.world;

        Random random = new Random(world.getSeed());
        long xrand = (random.nextLong() / 2 << 1) + 1;
        long zrand = (random.nextLong() / 2 << 1) + 1;
        random.setSeed(deserialized.x * xrand + deserialized.z * zrand ^ world.getSeed());

        // set the world field of the chunk manager
        world.getChunkManager().setWorld(world);

        // enable serverless on world
        world.setServerless(true);

        GlowChunk chunkToPopulate = world.getChunkAt(deserialized.x, deserialized.z);

        for (BlockPopulator p : world.getGenerator().getDefaultPopulators(world)) {
            p.populate(world, random, chunkToPopulate);
        }

        // only send messages that are outside of this chunk
        // information for the blocks inside this chunk get sent as chunk data
        world.setPopulatedBlockMessages(
            world.getPopulatedBlockMessages().stream().filter(msg -> {
                int msgChunkX = msg.getX() >> 4;
                int msgChunkZ = msg.getZ() >> 4;
                return msgChunkX != chunkToPopulate.getX() || msgChunkZ != chunkToPopulate.getZ();
            }).collect(Collectors.toList())
        );

        return new PopulateOutput(world, chunkToPopulate).serialize();
    }
}

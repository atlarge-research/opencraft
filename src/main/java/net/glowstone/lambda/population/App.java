package net.glowstone.lambda.population;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Random;
import net.glowstone.GlowWorld;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.lambda.population.serialization.PopulateInfo.PopulateInput;
import net.glowstone.lambda.population.serialization.PopulateInfo.PopulateOutput;
import org.bukkit.generator.BlockPopulator;


/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<String, String> {
    public String handleRequest(String input, final Context context) {
        // TODO: error handling
        PopulateInput deserialized = PopulateInput.fromJson(input);

        GlowWorld world = deserialized.world;
        Random random = deserialized.random;

        // set the world field of the chunk manager
        world.getChunkManager().setWorld(world);

        // set the chunksForLambda field in chunk manager
        world.getChunkManager().setKnownChunks(deserialized.adjacentChunks);

        // for each chunk set their world field
        for (GlowChunk chunk : world.getChunkManager().getKnownChunks()) {
            chunk.setWorld(world);
        }

        // enable serverless on world
        world.setServerless(true);

        GlowChunk chunk = world.getChunkAt(deserialized.x, deserialized.z);

        for (BlockPopulator p : world.getGenerator().getDefaultPopulators(world)) {
            p.populate(world, random, chunk);
        }

        return new PopulateOutput(world).toJson();
    }
}

package net.glowstone.lambda.population.serialization.json.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;

import net.glowstone.chunk.GlowChunk;
import net.glowstone.io.anvil.AnvilChunkIoService;

public class ChunkSerializer implements JsonSerializer<GlowChunk> {
    @Override
    public JsonElement serialize(GlowChunk src, Type typeOfSrc, JsonSerializationContext context) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(buffer);

        try {
            AnvilChunkIoService.write(src, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonObject obj = new JsonObject();
        obj.addProperty(
            "ChunkData",
            new String(Base64.getEncoder().encode(buffer.toByteArray()))
        );

        return obj;
    }
}

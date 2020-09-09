package net.glowstone.lambda.population.serialization.json.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.lang.reflect.Type;

public class IntListDeserializer implements JsonDeserializer<IntList> {
    @Override
    public IntList deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jsonArray = json.getAsJsonArray();

        IntArrayList dest = new IntArrayList(jsonArray.size());

        for (int i = 0; i < jsonArray.size(); ++i) {
            dest.add(jsonArray.get(i).getAsInt());
        }

        return dest;
    }
}

package net.glowstone.lambda.population.serialization.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Random;
import java.util.function.BiFunction;
import net.glowstone.generator.decorators.overworld.TreeDecorator.TreeDecoration;
import net.glowstone.generator.objects.trees.GenericTree;
import net.glowstone.util.BlockStateDelegate;

public class TreeDecorationDeserializer implements JsonDeserializer<TreeDecoration> {

    @SuppressWarnings("unchecked")
    private static BiFunction<Random, BlockStateDelegate, ? extends GenericTree> getConstructor(String className) {
        try {
            Class<?> c = Class.forName(className);
            Constructor<? extends GenericTree> con =
                    (Constructor<? extends GenericTree>) c.getConstructor(Random.class, BlockStateDelegate.class);

            return (random, blockStateDelegate) -> {
                try {
                    return con.newInstance(random, blockStateDelegate);
                } catch (Exception e) {
                    return null;
                }
            };
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public TreeDecoration deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jobject = json.getAsJsonObject();

        BiFunction<Random, BlockStateDelegate, ? extends GenericTree> constructor =
                getConstructor(jobject.get("constructor").getAsString());
        if (constructor == null) {
            return null;
        }

        return new TreeDecoration(constructor,
                jobject.get("weight").getAsInt());
    }
}

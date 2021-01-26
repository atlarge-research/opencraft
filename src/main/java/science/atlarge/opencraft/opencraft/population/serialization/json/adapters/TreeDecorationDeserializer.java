package science.atlarge.opencraft.opencraft.population.serialization.json.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Random;
import java.util.function.BiFunction;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.TreeDecorator.TreeDecoration;
import science.atlarge.opencraft.opencraft.generator.objects.trees.GenericTree;
import science.atlarge.opencraft.opencraft.util.BlockStateDelegate;

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
        JsonObject obj = json.getAsJsonObject();

        BiFunction<Random, BlockStateDelegate, ? extends GenericTree> constructor =
                getConstructor(obj.get("constructor").getAsString());
        if (constructor == null) {
            return null;
        }

        return new TreeDecoration(constructor, obj.get("weight").getAsInt());
    }
}

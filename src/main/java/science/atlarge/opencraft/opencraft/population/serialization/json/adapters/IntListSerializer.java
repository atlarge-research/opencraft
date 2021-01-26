package science.atlarge.opencraft.opencraft.population.serialization.json.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.unimi.dsi.fastutil.ints.IntList;

import java.lang.reflect.Type;

public class IntListSerializer implements JsonSerializer<IntList> {

    @Override
    public JsonElement serialize(IntList src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < src.size(); ++i) {
            jsonArray.add(src.getInt(i));
        }

        return jsonArray;
    }
}

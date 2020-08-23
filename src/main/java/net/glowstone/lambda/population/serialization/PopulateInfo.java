package net.glowstone.lambda.population.serialization;

import com.google.gson.Gson;
import net.glowstone.GlowWorld;

public class PopulateInfo {
    private static final Gson gson = JsonUtil.getGson();

    public GlowWorld world;
    public int x;
    public int z;

    public PopulateInfo(GlowWorld world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public String toJson() {
        return gson.toJson(this);
    }

    public static PopulateInfo fromJson(String jsn) {
        return gson.fromJson(jsn, PopulateInfo.class);
    }
}

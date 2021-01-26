package science.atlarge.opencraft.opencraft.population.serialization;

import java.lang.reflect.Type;

public interface GlowSerializer {
    String serialize(Object src, Type typeOfSrc);

    String serialize(Object src);

    <T> T deserialize(String src, Type typeOfT);
}

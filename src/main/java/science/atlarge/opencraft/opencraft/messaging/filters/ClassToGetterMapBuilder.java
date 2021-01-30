package science.atlarge.opencraft.opencraft.messaging.filters;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A builder for an immutable class-to-getter map. The mapping is performed between explicit types and getters, meaning
 * that it does not consider super- or subtypes.
 *
 * @param <Base> the base type of classes added to the map.
 * @param <Value> the return type of getters added to the map.
 */
final class ClassToGetterMapBuilder<Base, Value> {

    private final ImmutableMap.Builder<Class<? extends Base>, Function<Base, Value>> builder;

    /**
     * Create a class-to-getter map builder.
     */
    ClassToGetterMapBuilder() {
        builder = new ImmutableMap.Builder<>();
    }

    /**
     * Add a getter to the map.
     *
     * @param type the class with which the getter should be associated.
     * @param getter the getter that should be stored.
     * @param <Type> the type of value the getter is called on.
     */
    <Type extends Base> ClassToGetterMapBuilder<Base, Value> put(Class<Type> type, Function<Type, Value> getter) {
        builder.put(type, message -> {
            Type typedMessage = type.cast(message);
            return getter.apply(typedMessage);
        });
        return this;
    }

    /**
     * Create an immutable copy of the map in its current state.
     *
     * @return an immutable copy of the map.
     */
    Map<Class<? extends Base>, Function<Base, Value>> build() {
        return builder.build();
    }
}

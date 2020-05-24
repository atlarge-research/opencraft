package net.glowstone.messaging.filters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A builder for an immutable class-to-getter map. The mapping is performed between explicit types and getters, meaning
 * that it does not consider super- or subtypes.
 *
 * @param <Base> the base type of classes added to the map.
 * @param <Value> the return type of getters added to the map.
 */
final class ClassGetterMapBuilder<Base, Value> {

    private final Map<Class<? extends Base>, Function<Base, Value>> getters;

    /**
     * Create a class-to-getter map builder.
     */
    ClassGetterMapBuilder() {
        getters = new HashMap<>();
    }

    /**
     * Get an immutable copy of the map in its current state.
     *
     * @return an immutable copy of the map.
     */
    Map<Class<? extends Base>, Function<Base, Value>> getImmutableMap() {
        return Collections.unmodifiableMap(getters);
    }

    /**
     * Add a getter to the map.
     *
     * @param type the class with which the getter should be associated.
     * @param getter the getter that should be stored.
     * @param <Type> the type of value the getter is called on.
     */
    <Type extends Base> void add(Class<Type> type, Function<Type, Value> getter) {
        getters.put(type, message -> {
            Type typedMessage = type.cast(message);
            return getter.apply(typedMessage);
        });
    }
}

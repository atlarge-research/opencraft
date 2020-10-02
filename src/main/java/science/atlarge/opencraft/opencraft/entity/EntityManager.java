package science.atlarge.opencraft.opencraft.entity;

import static com.google.common.collect.Multimaps.newSetMultimap;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import science.atlarge.opencraft.opencraft.EventFactory;
import science.atlarge.opencraft.opencraft.chunk.GlowChunk;
import science.atlarge.opencraft.opencraft.entity.physics.BoundingBox;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

/**
 * A class which manages all of the entities within a world.
 *
 * @author Graham Edgecombe
 */
public class EntityManager implements Iterable<GlowEntity> {

    /**
     * A map of all the entity ids to the corresponding entities.
     */
    private final Map<Integer, GlowEntity> entities = new ConcurrentHashMap<>();

    /**
     * A map of entity types to a set containing all entities of that type.
     */
    private final Multimap<Class<? extends GlowEntity>, GlowEntity> groupedEntities
            = newSetMultimap(new ConcurrentHashMap<>(),
                    Sets::newConcurrentHashSet);

    /**
     * Returns all entities with the specified type.
     *
     * @param type The {@link Class} for the type.
     * @param <T> The type of entity.
     * @return A collection of entities with the specified type.
     */
    @SuppressWarnings("unchecked")
    public <T extends GlowEntity> Collection<T> getAll(Class<T> type) {
        if (GlowEntity.class.isAssignableFrom(type)) {
            return (Collection<T>) groupedEntities.get(type);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Gets all entities.
     *
     * @return A list of entities.
     */
    public List<GlowEntity> getAll() {
        return new ArrayList<>(entities.values());
    }

    /**
     * Get all player entities.
     *
     * @return a list of player entities.
     */
    public List<GlowPlayer> getPlayers() {
        return entities.values().stream()
                .filter(GlowPlayer.class::isInstance)
                .map(GlowPlayer.class::cast)
                .collect(Collectors.toList());
    }

    /**
     * Gets an entity by its uuid.
     * @param uuid The uuid.
     * @return The entity, or {@code null} if it could not be found.
     */
    public Entity getEntity(UUID uuid) {
        for (Entity entity : getAll()) {
            if (entity.getUniqueId().equals(uuid)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Gets an entity by its id.
     *
     * @param id The id.
     * @return The entity, or {@code null} if it could not be found.
     */
    public GlowEntity getEntity(int id) {
        return entities.get(id);
    }

    /**
     * Gets a list with all living entities.
     * @return a list with all living entities.
     */
    public List<LivingEntity> getLivingEntities() {
        return getAll().stream()
                .filter(e -> e instanceof GlowLivingEntity)
                .map(e -> (GlowLivingEntity) e)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Gets all entities by class.
     * @param cls the class.
     * @param <T> the type.
     * @return a collection of entities.
     */
    @SuppressWarnings("unchecked")
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> cls) {
        return getAll().stream()
                .filter(e -> cls.isAssignableFrom(e.getClass()))
                .map(e -> (T) e)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets a collection of entities by classes.
     * @param classes the classes.
     * @return a collection of entities.
     */
    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        ArrayList<Entity> result = new ArrayList<>();
        for (Entity e : getAll()) {
            for (Class<?> cls : classes) {
                if (cls.isAssignableFrom(e.getClass())) {
                    result.add(e);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Returns a list of entities within a bounding box centered around a Location.
     *
     * <p>Some implementations may impose artificial restrictions on the size of the search bounding
     * box.
     *
     * @param location The center of the bounding box
     * @param x        1/2 the size of the box along x axis
     * @param y        1/2 the size of the box along y axis
     * @param z        1/2 the size of the box along z axis
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
        Vector minCorner = new Vector(
                location.getX() - x, location.getY() - y, location.getZ() - z);
        Vector maxCorner = new Vector(
                location.getX() + x, location.getY() + y, location.getZ() + z);
        BoundingBox searchBox = BoundingBox.fromCorners(minCorner, maxCorner); // TODO: test
        GlowEntity except = null;
        return getEntitiesInside(searchBox, except);
    }

    /**
     * Returns all entities that are inside or partly inside the given bounding box, with optionally
     * one exception.
     * @param searchBox the bounding box to search inside
     * @param except the entity to exclude, or null to include all
     * @return the entities contained in or touching {@code searchBox}, other than {@code except}
     */
    public List<Entity> getEntitiesInside(BoundingBox searchBox, GlowEntity except) {
        // todo: narrow search based on the box's corners
        return getAll().stream()
                .filter(entity -> entity != except && entity.intersects(searchBox))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Registers the entity to this world.
     *
     * @param entity The entity.
     */
    @SuppressWarnings("unchecked")
    void register(GlowEntity entity) {
        if (entity.entityId == 0) {
            throw new IllegalStateException("Entity has not been assigned an id.");
        }
        entities.put(entity.entityId, entity);
        groupedEntities.put(entity.getClass(), entity);
        ((GlowChunk) entity.location.getChunk()).getRawEntities().add(entity);
        EventFactory.getInstance().callEvent(
                new EntityAddToWorldEvent(entity)
        );
    }

    /**
     * Unregister the entity to this world.
     *
     * @param entity The entity.
     */
    void unregister(GlowEntity entity) {
        EventFactory.getInstance().callEvent(new EntityRemoveFromWorldEvent(entity));
        entities.remove(entity.entityId);
        groupedEntities.remove(entity.getClass(), entity);
        ((GlowChunk) entity.location.getChunk()).getRawEntities().remove(entity);
    }

    /**
     * Notes that an entity has moved from one location to another for physics and storage purposes.
     *
     * @param entity The entity.
     * @param newLocation The new location.
     */
    void move(GlowEntity entity, Location newLocation) {
        Chunk prevChunk = entity.location.getChunk();
        Chunk newChunk = newLocation.getChunk();
        if (prevChunk != newChunk) {
            ((GlowChunk) prevChunk).getRawEntities().remove(entity);
            ((GlowChunk) newChunk).getRawEntities().add(entity);
        }
    }

    @Override
    public Iterator<GlowEntity> iterator() {
        return entities.values().iterator();
    }
}

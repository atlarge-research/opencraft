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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import science.atlarge.opencraft.opencraft.EventFactory;
import science.atlarge.opencraft.opencraft.chunk.GlowChunk;
import science.atlarge.opencraft.opencraft.entity.physics.BoundingBox;

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
     * Used to prevent iterating over (and thereby generating UUIDs for) entities while registering a new entity.
     */
    @Getter
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    /**
     * Returns all entities with the specified type.
     *
     * @param type The {@link Class} for the type.
     * @param <T>  The type of entity.
     * @return A collection of entities with the specified type.
     */
    public <T extends GlowEntity> Collection<T> getAll(Class<T> type) {
        readLock.lock();
        try {
            if (GlowEntity.class.isAssignableFrom(type)) {
                return (Collection<T>) groupedEntities.get(type);
            } else {
                return Collections.emptyList();
            }
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Gets all entities.
     *
     * @return A list of entities.
     */
    public List<GlowEntity> getAll() {
        readLock.lock();
        try {
            return new ArrayList<>(entities.values());
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Get all player entities.
     *
     * @return a list of player entities.
     */
    public List<GlowPlayer> getPlayers() {
        readLock.lock();
        try {
            return entities.values().stream()
                    .filter(GlowPlayer.class::isInstance)
                    .map(GlowPlayer.class::cast)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Gets an entity by its uuid.
     *
     * @param uuid The uuid.
     * @return The entity, or {@code null} if it could not be found.
     */
    public Entity getEntity(UUID uuid) {
        readLock.lock();
        try {
            for (Entity entity : getAll()) {
                if (entity.getUniqueId().equals(uuid)) {
                    return entity;
                }
            }
            return null;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Gets an entity by its id.
     *
     * @param id The id.
     * @return The entity, or {@code null} if it could not be found.
     */
    public GlowEntity getEntity(int id) {
        readLock.lock();
        try {
            return entities.get(id);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Gets a list with all living entities.
     *
     * @return a list with all living entities.
     */
    public List<LivingEntity> getLivingEntities() {
        readLock.lock();
        try {
            return getAll().stream()
                    .filter(e -> e instanceof GlowLivingEntity)
                    .map(e -> (GlowLivingEntity) e)
                    .collect(Collectors.toCollection(LinkedList::new));
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Gets all entities by class.
     *
     * @param cls the class.
     * @param <T> the type.
     * @return a collection of entities.
     */
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> cls) {
        readLock.lock();
        try {
            return getAll().stream()
                    .filter(e -> cls.isAssignableFrom(e.getClass()))
                    .map(e -> (T) e)
                    .collect(Collectors.toCollection(ArrayList::new));
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Gets a collection of entities by classes.
     *
     * @param classes the classes.
     * @return a collection of entities.
     */
    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        readLock.lock();
        try {
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
        } finally {
            readLock.unlock();
        }
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
        readLock.lock();
        try {
            Vector minCorner = new Vector(
                    location.getX() - x, location.getY() - y, location.getZ() - z);
            Vector maxCorner = new Vector(
                    location.getX() + x, location.getY() + y, location.getZ() + z);
            BoundingBox searchBox = BoundingBox.fromCorners(minCorner, maxCorner); // TODO: test
            GlowEntity except = null;
            return getEntitiesInside(searchBox, except);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Returns all entities that are inside or partly inside the given bounding box, with optionally
     * one exception.
     *
     * @param searchBox the bounding box to search inside
     * @param except    the entity to exclude, or null to include all
     * @return the entities contained in or touching {@code searchBox}, other than {@code except}
     */
    public List<Entity> getEntitiesInside(BoundingBox searchBox, GlowEntity except) {
        readLock.lock();
        try {
            // todo: narrow search based on the box's corners
            return getAll().stream()
                    .filter(entity -> entity != except && entity.intersects(searchBox))
                    .collect(Collectors.toCollection(LinkedList::new));
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Registers the entity to this world.
     *
     * @param entity The entity.
     */
    void register(GlowEntity entity) {
        writeLock.lock();
        try {
            if (entity.entityId == 0) {
                throw new IllegalStateException("Entity has not been assigned an id.");
            }
            entities.put(entity.entityId, entity);
            groupedEntities.put(entity.getClass(), entity);
            ((GlowChunk) entity.location.getChunk()).getRawEntities().add(entity);
            EventFactory.getInstance().callEvent(
                    new EntityAddToWorldEvent(entity)
            );
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Unregister the entity to this world.
     *
     * @param entity The entity.
     */
    void unregister(GlowEntity entity) {
        writeLock.lock();
        try {
            EventFactory.getInstance().callEvent(new EntityRemoveFromWorldEvent(entity));
            entities.remove(entity.entityId);
            groupedEntities.remove(entity.getClass(), entity);
            ((GlowChunk) entity.location.getChunk()).getRawEntities().remove(entity);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Notes that an entity has moved from one location to another for physics and storage purposes.
     *
     * @param entity      The entity.
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
        readLock.lock();
        try {
            return entities.values().iterator();
        } finally {
            readLock.unlock();
        }
    }
}

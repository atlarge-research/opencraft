package net.glowstone.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flowpowered.network.Message;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;
import net.glowstone.EventFactory;
import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;
import net.glowstone.ServerProvider;
import net.glowstone.block.GlowBlock;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.entity.objects.GlowPainting;
import net.glowstone.entity.passive.GlowChicken;
import net.glowstone.entity.physics.BoundingBox;
import net.glowstone.entity.physics.EntityBoundingBox;
import net.glowstone.entity.projectile.GlowArrow;
import net.glowstone.inventory.GlowPlayerInventory;
import net.glowstone.scoreboard.GlowScoreboard;
import net.glowstone.scoreboard.GlowScoreboardManager;
import net.glowstone.util.Coordinates;
import net.glowstone.util.GameRuleManager;
import net.glowstone.util.Vectors;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Superclass for tests of entity classes. Configures necessary mocks for subclasses.
 * TODO: Create subclasses to test all types of entities.
 *
 * @param <T> the class under test
 */
public abstract class GlowEntityTest<T extends GlowEntity> {

    // Mockito mocks
    @Mock
    protected ItemFactory itemFactory;
    @Mock
    protected GlowChunk chunk;
    @Mock
    protected GlowBlock block;
    @Mock
    protected GlowWorld world;
    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    protected GlowServer server;
    @Mock
    protected GlowScoreboardManager scoreboardManager;
    @Mock
    protected EventFactory eventFactory;
    @Mock
    private PluginManager pluginManager;
    @Mock
    protected GlowPlayer player;

    // Real objects
    protected Location location;
    protected EntityIdManager idManager;
    protected EntityManager entityManager;
    protected GlowScoreboard scoreboard;
    protected Logger log;
    protected final Function<? super Location, ? extends T> entityCreator;
    protected T entity;
    private EventFactory oldEventFactory;
    protected GlowPlayerInventory inventory;


    protected GlowEntityTest(Function<? super Location, ? extends T> entityCreator) {
        this.entityCreator = entityCreator;
    }

    /**
     * Override this to return false in subclasses that call super.{@link #setUp()}, if the entity
     * under test should not be created in the super method.
     *
     * @return true if GlowEntity's implementation of {@link #setUp()} is to invoke {@link
     * #entityCreator}; false otherwise.
     */
    public boolean createEntityInSuperSetUp() {
        return true;
    }


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(server.getItemFactory()).thenReturn(itemFactory);
        when(server.getPluginManager()).thenReturn(pluginManager);
        ServerProvider.setMockServer(server);
        log = Logger.getLogger(getClass().getSimpleName());
        when(server.getLogger()).thenReturn(log);
        location = new Location(world, 0, 0, 0);
        when(world.getServer()).thenReturn(server);
        when(world.getDifficulty()).thenReturn(Difficulty.NORMAL);
        when(server.getWorlds()).thenReturn(Collections.singletonList(world));
        when(world.getBlockAt(any(Location.class))).thenReturn(block);
        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(block);
        when(block.getType()).thenReturn(Material.DIRT);
        when(block.getRelative(any(BlockFace.class))).thenReturn(block);
        when(world.getChunkAt(any(Location.class))).thenReturn(chunk);
        when(world.getChunkAt(any(Block.class))).thenReturn(chunk);
        when(world.getChunkAt(anyInt(), anyInt())).thenReturn(chunk);
        when(world.getGameRuleMap()).thenReturn(new GameRuleManager());
        when(server.getItemFactory()).thenReturn(itemFactory);
        entityManager = Mockito.spy(new EntityManager());
        when(world.getEntityManager()).thenReturn(entityManager);
        idManager = new EntityIdManager();
        when(server.getEntityIdManager()).thenReturn(idManager);
        when(server.getScoreboardManager()).thenReturn(scoreboardManager);
        scoreboard = new GlowScoreboard();
        when(scoreboardManager.getMainScoreboard()).thenReturn(scoreboard);
        when(itemFactory.ensureServerConversions(any(ItemStack.class)))
                .thenAnswer(returnsFirstArg());
        oldEventFactory = EventFactory.getInstance();
        EventFactory.setInstance(eventFactory);
        when(eventFactory.callEvent(any(Event.class))).thenAnswer(returnsFirstArg());
        if (createEntityInSuperSetUp()) {
            entity = entityCreator.apply(location);
        }
        when(eventFactory.onEntityDamage(any(EntityDamageEvent.class))).thenAnswer(
                returnsFirstArg());
        inventory = new GlowPlayerInventory(player);
        when(player.getInventory()).thenReturn(inventory);
        when(player.getGameMode()).thenReturn(GameMode.SURVIVAL);
    }

    @After
    public void tearDown() {
        EventFactory.setInstance(oldEventFactory);
        ServerProvider.setMockServer(null);
        // https://www.atlassian.com/blog/archives/reducing_junit_memory_usage
        world = null;
        server = null;
        scoreboardManager = null;
        itemFactory = null;
        chunk = null;
        block = null;
        log = null;
        entity = null;
        player = null;
    }

    @Test
    public void testCreateSpawnMessage() {
        List<Message> messages = entity.createSpawnMessage();
        assertFalse(messages.isEmpty());
        // Should start with an instance of one of the Spawn*Message classes
        assertTrue(messages.get(0).getClass().getSimpleName().startsWith("Spawn"));
    }

    @Test
    public void getCoordinates() {
        final double x = 32.0;
        final double y = 64.0;
        final double z = 31.5;
        final Coordinates coordinates = new Coordinates(x, z);

        GlowEntity chicken = new GlowChicken(new Location(world, x, y, z));

        assertEquals(coordinates, chicken.getCoordinates());
    }

    @Test
    public void testVelocityWater() {
        entity.velocity.setX(1.0);
        entity.velocity.setY(1.0);
        entity.velocity.setZ(1.0);
        entity.setGravity(true);
        entity.setFriction(true);
        when(location.getBlock().getType()).thenReturn(Material.WATER);
        Vector expectedResult = new Vector(0.8, 0.77, 0.8);
        entity.computeVelocity();
        assertTrue(Vectors.equals(expectedResult, entity.velocity, 0.15));
    }

    @Test
    public void testVelocityLava() {
        entity.velocity.setX(1.0);
        entity.velocity.setY(1.0);
        entity.velocity.setZ(1.0);
        entity.setGravity(true);
        entity.setFriction(true);
        when(location.getBlock().getType()).thenReturn(Material.LAVA);
        Vector expectedResult = new Vector(0.5, 0.47, 0.5);
        entity.computeVelocity();
        assertTrue(Vectors.equals(expectedResult, entity.velocity, 0.15));
    }

    @Test
    public void testCollisionNoBlocks() {
        entity.setVelocity(new Vector(0.1, 0.1, 0.1));
        entity.boundingBox = mock(EntityBoundingBox.class);

        when(entity.boundingBox.createCopyAt(any(Location.class))).thenReturn(entity.boundingBox);
        when(entity.boundingBox.getBroadPhase(any(Vector.class))).thenReturn(null);
        when(entity.boundingBox.getSize()).thenReturn(new Vector(1.0, 1.0, 1.0));

        entity.resolveCollisions();

        if (!(entity instanceof GlowPainting)) {
            verify(entity.boundingBox).getBroadPhase(any(Vector.class));
        }
    }

    @Test
    public void testCollisionWithBlock() {
        Vector epsilon = new Vector(0.1, 0.1, 0.1);
        entity.setVelocity(epsilon.clone());
        when(block.getType()).thenReturn(Material.DIRT);
        entity.boundingBox = new EntityBoundingBox(1.0, 1.0);
        Vector min = entity.boundingBox.minCorner;
        min.subtract(epsilon);
        Vector max = entity.boundingBox.maxCorner;
        max.add(epsilon);
        BoundingBox boundingBox = BoundingBox.fromCorners(min, max);
        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(block);
        when(block.getBoundingBoxes()).thenReturn(Arrays.asList(boundingBox));
        entity.resolveCollisions();
        assertEquals(0.0, location.getX(), Double.MIN_VALUE);
        assertEquals(0.0, location.getY(), Double.MIN_VALUE);
        assertEquals(0.0, location.getZ(), Double.MIN_VALUE);
    }

    @Test
    public void testCollisionWithBlockAsProjectile() {
        Vector epsilon = new Vector(0.1, 0.1, 0.1);
        when(world.getBlockAt(any())).thenReturn(block);
        when(block.getType()).thenReturn(Material.DIRT);
        GlowArrow entityArrow = new GlowArrow(location);
        entityArrow.setVelocity(epsilon.clone());
        entityArrow.boundingBox = new EntityBoundingBox(1.0, 1.0);
        Vector min = entityArrow.boundingBox.minCorner;
        min.subtract(epsilon);
        Vector max = entityArrow.boundingBox.maxCorner;
        max.add(epsilon);
        BoundingBox boundingBox = BoundingBox.fromCorners(min, max);
        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(block);
        when(block.getBoundingBoxes()).thenReturn(Arrays.asList(boundingBox));

        entityArrow.resolveCollisions();

        assertEquals(entityArrow.getFireTicks(), 0);
    }
}

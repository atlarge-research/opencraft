package science.atlarge.opencraft.opencraft.entity.ai;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.bukkit.entity.EntityType;
import org.junit.Before;
import org.junit.Test;
import science.atlarge.opencraft.opencraft.entity.GlowLivingEntity;

public class TaskManagerTest {

    private static class CountingTask extends EntityTask {
        private int executions;

        CountingTask(String name) {
            super(name);
        }

        @Override
        public boolean isInstant() {
            return true;
        }

        @Override
        public int getDurationMin() {
            return 0;
        }

        @Override
        public int getDurationMax() {
            return 0;
        }

        @Override
        public boolean shouldStart(GlowLivingEntity entity) {
            return false;
        }

        @Override
        public void start(GlowLivingEntity entity) {
        }

        @Override
        public void end(GlowLivingEntity entity) {
        }

        @Override
        public void execute(GlowLivingEntity entity) {
            executions++;
        }
    }

    private final GlowLivingEntity entity = mock(GlowLivingEntity.class);
    private TaskManager manager;

    @Before
    public void before() {
        org.mockito.Mockito.when(entity.hasAI()).thenReturn(true);
        manager = new TaskManager(entity);
    }

    @Test
    public void testPulseRunsEveryTask() {
        CountingTask first = new CountingTask("first");
        CountingTask second = new CountingTask("second");
        CountingTask third = new CountingTask("third");
        manager.addTask(first);
        manager.addTask(second);
        manager.addTask(third);

        manager.pulse();

        assertEquals(1, first.executions);
        assertEquals(1, second.executions);
        assertEquals(1, third.executions);
    }

    @Test
    public void testAddingTaskWithSameNameReplacesIt() {
        CountingTask old = new CountingTask("task");
        CountingTask replacement = new CountingTask("task");
        manager.addTask(old);
        manager.addTask(replacement);

        manager.pulse();

        assertEquals(0, old.executions);
        assertEquals(1, replacement.executions);
    }

    @Test
    public void testZombieTargetingStateRunsFollowAndAttack() {
        // Registered by the zombie constructor, repeated here to keep the test independent.
        EntityDirector.registerEntityMobState(EntityType.ZOMBIE, HostileMobState.TARGETING,
                "follow_player");
        EntityDirector.registerEntityMobState(EntityType.ZOMBIE, HostileMobState.TARGETING,
                "attack_player");
        org.mockito.Mockito.when(entity.getType()).thenReturn(EntityType.ZOMBIE);
        org.mockito.Mockito.when(entity.getState()).thenReturn(HostileMobState.TARGETING);

        manager.updateState();

        assertEquals(FollowPlayerTask.class, manager.getTask("follow_player").getClass());
        assertEquals(AttackPlayerTask.class, manager.getTask("attack_player").getClass());
    }

    @Test
    public void testRegisteringMobStateTaskTwiceKeepsOneEntry() {
        EntityDirector.registerEntityMobState(EntityType.SKELETON, MobState.IDLE, "look_around");
        EntityDirector.registerEntityMobState(EntityType.SKELETON, MobState.IDLE, "look_around");

        assertEquals(1, EntityDirector.getEntityMobStateTask(EntityType.SKELETON, MobState.IDLE)
                .size());
    }
}

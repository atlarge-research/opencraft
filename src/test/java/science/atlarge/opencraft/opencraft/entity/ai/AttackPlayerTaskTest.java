package science.atlarge.opencraft.opencraft.entity.ai;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import science.atlarge.opencraft.opencraft.GlowWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.junit.Before;
import org.junit.Test;
import science.atlarge.opencraft.opencraft.entity.GlowLivingEntity;

public class AttackPlayerTaskTest {

    private final GlowWorld world = mock(GlowWorld.class);
    private final GlowLivingEntity zombie = mock(GlowLivingEntity.class);
    private final Player player = mock(Player.class);
    private AttackPlayerTask task;

    @Before
    public void before() {
        task = new AttackPlayerTask();
        when(world.getDifficulty()).thenReturn(Difficulty.NORMAL);
        when(zombie.getWorld()).thenReturn(world);
        when(zombie.getLocation()).thenReturn(new Location(world, 0, 64, 0));
        when(zombie.getNearbyEntities(anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(Collections.singletonList(player));
        when(player.getLocation()).thenReturn(new Location(world, 1, 64, 0));
        when(player.getGameMode()).thenReturn(GameMode.SURVIVAL);
    }

    @Test
    public void testHitsPlayerInReach() {
        task.execute(zombie);
        verify(player).damage(3.0, zombie, DamageCause.ENTITY_ATTACK);
    }

    @Test
    public void testDamageDependsOnDifficulty() {
        when(world.getDifficulty()).thenReturn(Difficulty.HARD);
        task.execute(zombie);
        verify(player).damage(4.5, zombie, DamageCause.ENTITY_ATTACK);
    }

    @Test
    public void testWaitsForCooldownBetweenHits() {
        for (int i = 0; i <= AttackPlayerTask.COOLDOWN_TICKS; i++) {
            task.execute(zombie);
        }
        verify(player, times(1)).damage(3.0, zombie, DamageCause.ENTITY_ATTACK);
        task.execute(zombie);
        verify(player, times(2)).damage(3.0, zombie, DamageCause.ENTITY_ATTACK);
    }

    @Test
    public void testDoesNotHitPlayerOutOfReach() {
        when(player.getLocation()).thenReturn(new Location(world, 5, 64, 0));
        task.execute(zombie);
        verify(player, never()).damage(anyDouble(), org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any());
    }

    @Test
    public void testDoesNotHitOnPeaceful() {
        when(world.getDifficulty()).thenReturn(Difficulty.PEACEFUL);
        task.execute(zombie);
        verify(player, never()).damage(anyDouble(), org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any());
    }

    @Test
    public void testDoesNotHitCreativePlayer() {
        when(player.getGameMode()).thenReturn(GameMode.CREATIVE);
        task.execute(zombie);
        verify(player, never()).damage(anyDouble(), org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any());
    }
}

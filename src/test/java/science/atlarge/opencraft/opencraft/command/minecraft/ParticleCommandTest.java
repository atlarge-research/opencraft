package science.atlarge.opencraft.opencraft.command.minecraft;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import science.atlarge.opencraft.opencraft.command.CommandTestWithFakePlayers;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;

public class ParticleCommandTest extends CommandTestWithFakePlayers<ParticleCommand> {

    private GlowPlayer player;

    public ParticleCommandTest() {
        super(ParticleCommand::new, "player1");
    }

    @Override
    public void before() {
        super.before();
        player = fakePlayers[0];
        when(player.hasPermission(ArgumentMatchers.anyString())).thenReturn(true);
        when(player.getWorld()).thenReturn(world);
    }

    private boolean run(String... args) {
        return command.execute(player, "particle", args);
    }

    @Test
    public void testVanillaNamesAreCaseInsensitive() {
        assertThat(ParticleCommand.byName("largeexplode"), is(Particle.EXPLOSION_LARGE));
        assertThat(ParticleCommand.byName("FIREWORKSSPARK"), is(Particle.FIREWORKS_SPARK));
        assertThat(ParticleCommand.byName("heart"), is(Particle.HEART));
    }

    @Test
    public void testUnknownNameIsRejected() {
        assertThat(ParticleCommand.byName("sparkles"), is(nullValue()));
        assertThat(run("sparkles", "0", "0", "0", "0", "0", "0", "1"), is(false));
        verify(world, never()).spawnParticle(any(Particle.class), any(Location.class), anyInt(),
                anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    public void testSpawnsParticleInTheWorld() {
        assertThat(run("heart", "1", "2", "3", "0.5", "0.25", "0.75", "2", "10"), is(true));
        verify(world).spawnParticle(Particle.HEART, new Location(world, 1, 2, 3), 10, 0.5, 0.25,
                0.75, 2.0);
    }

    @Test
    public void testCountDefaultsToZero() {
        assertThat(run("flame", "0", "0", "0", "0", "1", "0", "0.1"), is(true));
        verify(world).spawnParticle(Particle.FLAME, new Location(world, 0, 0, 0), 0, 0.0, 1.0, 0.0,
                0.1);
    }

    @Test
    public void testRelativeCoordinates() {
        assertThat(run("flame", "~1", "~", "~-1", "0", "0", "0", "0"), is(true));
        verify(world).spawnParticle(Particle.FLAME, new Location(world, 11.5, 20.0, 29.5), 0, 0.0,
                0.0, 0.0, 0.0);
    }

    @Test
    public void testRejectsBadNumbersAndNegativeSpeed() {
        assertThat(run("heart", "a", "0", "0", "0", "0", "0", "1"), is(false));
        assertThat(run("heart", "0", "0", "0", "0", "0", "0", "-1"), is(false));
        assertThat(run("heart", "0", "0", "0", "0", "0", "0", "1", "-5"), is(false));
        verify(world, never()).spawnParticle(any(Particle.class), any(Location.class), anyInt(),
                anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    public void testTooFewArguments() {
        assertThat(run("heart", "0", "0", "0"), is(false));
    }
}

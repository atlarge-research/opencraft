package science.atlarge.opencraft.opencraft.command.minecraft;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.bukkit.Location;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.junit.Test;
import science.atlarge.opencraft.opencraft.command.CommandTestWithFakePlayers;

public class TpCommandTest extends CommandTestWithFakePlayers<TpCommand> {

    public TpCommandTest() {
        super(TpCommand::new, "player1");
    }

    private Location teleportedTo(String... args) {
        assertThat(command.execute(opSender, "label", args), is(true));
        ArgumentCaptor<Location> captor = ArgumentCaptor.forClass(Location.class);
        Mockito.verify(fakePlayers[0]).teleport(captor.capture());
        return captor.getValue();
    }

    @Test
    public void testTeleportToAbsoluteCoordinates() {
        Location result = teleportedTo("player1", "1", "64", "-3");
        assertThat(result.getX(), is(1.5));
        assertThat(result.getY(), is(64.0));
        assertThat(result.getZ(), is(-2.5));
    }

    @Test
    public void testTeleportToCoordinatesRelativeToTarget() {
        Location result = teleportedTo("player1", "~1", "~", "~-2");
        assertThat(result.getX(), is(11.5));
        assertThat(result.getY(), is(20.0));
        assertThat(result.getZ(), is(28.5));
    }

    @Test
    public void testTeleportToCoordinatesWithRotation() {
        Location result = teleportedTo("player1", "0", "70", "0", "90", "~10");
        assertThat(result.getYaw(), is(90.0f));
        assertThat(result.getPitch(), is(10.0f));
    }

    @Test
    public void testInvalidCoordinatesAreRejected() {
        assertThat(command.execute(opSender, "label",
                new String[]{"player1", "a", "b", "c"}), is(false));
        Mockito.verify(fakePlayers[0], Mockito.never()).teleport(Mockito.any(Location.class));
    }
}

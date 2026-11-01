package science.atlarge.opencraft.opencraft.command.minecraft;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.when;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import science.atlarge.opencraft.opencraft.command.CommandTest;

public class DifficultyCommandTest extends CommandTest<DifficultyCommand> {

    public DifficultyCommandTest() {
        super(DifficultyCommand::new);
    }

    @Test
    public void testDifficultyAppliesToAllWorlds() {
        PowerMockito.mockStatic(Bukkit.class);
        World overworld = PowerMockito.mock(World.class);
        World nether = PowerMockito.mock(World.class);
        when(Bukkit.getWorlds()).thenReturn(ImmutableList.of(overworld, nether));

        assertThat(command.execute(opSender, "label", new String[]{"hard"}), is(true));

        Mockito.verify(overworld).setDifficulty(Difficulty.HARD);
        Mockito.verify(nether).setDifficulty(Difficulty.HARD);
    }

    @Test
    public void testUnknownDifficultyChangesNothing() {
        PowerMockito.mockStatic(Bukkit.class);
        World world = PowerMockito.mock(World.class);
        when(Bukkit.getWorlds()).thenReturn(ImmutableList.of(world));

        assertThat(command.execute(opSender, "label", new String[]{"nonsense"}), is(false));

        Mockito.verify(world, Mockito.never()).setDifficulty(Mockito.any(Difficulty.class));
    }
}

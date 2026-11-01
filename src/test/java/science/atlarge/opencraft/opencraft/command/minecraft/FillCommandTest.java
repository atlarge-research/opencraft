package science.atlarge.opencraft.opencraft.command.minecraft;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.command.CommandTestWithFakePlayers;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;

public class FillCommandTest extends CommandTestWithFakePlayers<FillCommand> {

    private final Map<String, GlowBlock> blocks = new HashMap<>();
    private GlowPlayer player;

    public FillCommandTest() {
        super(FillCommand::new, "player1");
    }

    @Override
    public void before() {
        super.before();
        blocks.clear();
        player = fakePlayers[0];
        when(player.hasPermission(ArgumentMatchers.anyString())).thenReturn(true);
        when(player.getWorld()).thenReturn(world);
        when(world.getBlockAt(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt())).thenAnswer(invocation -> {
                    String key = invocation.getArgument(0) + "," + invocation.getArgument(1)
                            + "," + invocation.getArgument(2);
                    return blocks.computeIfAbsent(key, k -> {
                        GlowBlock block = mock(GlowBlock.class);
                        when(block.getType()).thenReturn(Material.AIR);
                        return block;
                    });
                });
    }

    private boolean fill(String... args) {
        return command.execute(player, "fill", args);
    }

    private GlowBlock block(int x, int y, int z) {
        return (GlowBlock) world.getBlockAt(x, y, z);
    }

    @Test
    public void testFillsAllBlocksOfTheRegion() {
        assertThat(fill("0", "0", "0", "1", "1", "1", "stone"), is(true));
        for (int x = 0; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = 0; z <= 1; z++) {
                    verify(block(x, y, z)).setType(Material.STONE, (byte) 0, true);
                }
            }
        }
        assertThat(blocks.size(), is(8));
    }

    @Test
    public void testCornersMayBeGivenInAnyOrder() {
        assertThat(fill("1", "1", "1", "0", "0", "0", "stone"), is(true));
        assertThat(blocks.size(), is(8));
    }

    @Test
    public void testRefusesRegionsThatAreTooLarge() {
        assertThat(fill("0", "0", "0", "32", "32", "32", "stone"), is(false));
        assertThat(blocks.size(), is(0));
    }

    @Test
    public void testAllowsRegionOfExactlyTheMaximumSize() {
        assertThat(fill("0", "0", "0", "31", "31", "31", "stone"), is(true));
        assertThat(blocks.size(), is(FillCommand.MAX_BLOCKS));
    }

    @Test
    public void testHollowEmptiesTheInside() {
        assertThat(fill("0", "0", "0", "2", "2", "2", "stone", "0", "hollow"), is(true));
        verify(block(1, 1, 1)).setType(Material.AIR, (byte) 0, true);
        verify(block(0, 1, 1)).setType(Material.STONE, (byte) 0, true);
        verify(block(2, 2, 2)).setType(Material.STONE, (byte) 0, true);
    }

    @Test
    public void testOutlineLeavesTheInsideAlone() {
        assertThat(fill("0", "0", "0", "2", "2", "2", "stone", "0", "outline"), is(true));
        verify(block(1, 1, 1), never()).setType(ArgumentMatchers.any(Material.class), anyByte(),
                anyBoolean());
        verify(block(0, 1, 1)).setType(Material.STONE, (byte) 0, true);
    }

    @Test
    public void testKeepOnlyFillsAir() {
        GlowBlock occupied = block(0, 0, 0);
        when(occupied.getType()).thenReturn(Material.DIRT);
        assertThat(fill("0", "0", "0", "1", "0", "0", "stone", "0", "keep"), is(true));
        verify(occupied, never()).setType(ArgumentMatchers.any(Material.class), anyByte(),
                anyBoolean());
        verify(block(1, 0, 0)).setType(Material.STONE, (byte) 0, true);
    }

    @Test
    public void testReplaceFilterOnlyReplacesThatBlock() {
        GlowBlock dirt = block(0, 0, 0);
        when(dirt.getType()).thenReturn(Material.DIRT);
        assertThat(fill("0", "0", "0", "1", "0", "0", "stone", "0", "replace", "dirt"), is(true));
        verify(dirt).setType(Material.STONE, (byte) 0, true);
        verify(block(1, 0, 0), never()).setType(ArgumentMatchers.any(Material.class), anyByte(),
                anyBoolean());
    }

    @Test
    public void testFailsWhenNothingMatches() {
        assertThat(fill("0", "0", "0", "1", "0", "0", "stone", "0", "replace", "dirt"), is(false));
    }

    @Test
    public void testRejectsUnknownBlock() {
        assertThat(fill("0", "0", "0", "1", "1", "1", "nonsense"), is(false));
        assertThat(blocks.size(), is(0));
    }

    @Test
    public void testRejectsUnknownMode() {
        assertThat(fill("0", "0", "0", "1", "1", "1", "stone", "0", "sideways"), is(false));
        assertThat(blocks.size(), is(0));
    }

    @Test
    public void testShellDetection() {
        assertThat(FillCommand.isOnShell(1, 1, 1, 0, 0, 0, 2, 2, 2), is(false));
        assertThat(FillCommand.isOnShell(0, 1, 1, 0, 0, 0, 2, 2, 2), is(true));
        assertThat(FillCommand.isOnShell(1, 2, 1, 0, 0, 0, 2, 2, 2), is(true));
        assertThat(FillCommand.isOnShell(1, 1, 1, 1, 1, 1, 1, 1, 1), is(true));
    }
}

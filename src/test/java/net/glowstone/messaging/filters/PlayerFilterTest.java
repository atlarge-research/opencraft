package net.glowstone.messaging.filters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.flowpowered.network.Message;
import net.glowstone.net.message.play.game.BlockBreakAnimationMessage;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Verify that the PlayerFilter filters the correct typed and valued messages.
 */
class PlayerFilterTest {

    private static final int PLAYER_ID = 1;

    private PlayerFilter filter;
    private Player player;

    @BeforeEach
    void beforeEach() {
        filter = new PlayerFilter();
        player = mock(Player.class);
        when(player.getEntityId()).thenReturn(PLAYER_ID);
    }

    /**
     * Verify that BlockBreakAnimationMessages are filtered, such that the original author does not receive it.
     */
    @Test
    void filterAuthoredBlockBreakAnimationTest() {
        BlockBreakAnimationMessage message = new BlockBreakAnimationMessage(PLAYER_ID, 0, 0, 0, 0);
        assertFalse(filter.filter(player, message));
    }

    /**
     * Verify that BlockBreakAnimationMessages are not filtered from non-author players.
     */
    @Test
    void filterOtherBlockBreakAnimationTest() {
        BlockBreakAnimationMessage message = new BlockBreakAnimationMessage(PLAYER_ID + 1, 0, 0, 0, 0);
        assertTrue(filter.filter(player, message));
    }

    /**
     * Verify that messages are not filtered by default.
     */
    @Test
    void filterMessageBaseTest() {
        Message message = mock(Message.class);
        assertTrue(filter.filter(player, message));
    }
}

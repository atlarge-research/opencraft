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

    private PlayerFilter filter;
    private Player player;

    @BeforeEach
    void beforeEach() {
        filter = new PlayerFilter();
        player = mock(Player.class);
    }

    /**
     * Verify that null messages are properly filtered.
     */
    @Test
    void filterNullTest() {
        assertFalse(filter.filter(player, null));
    }

    /**
     * Verify that BlockBreakAnimationMessages are filtered, such that the original author does not receive it.
     */
    @Test
    void filterAuthoredBlockBreakAnimationTest() {
        when(player.getEntityId()).thenReturn(1);
        BlockBreakAnimationMessage message = mock(BlockBreakAnimationMessage.class);
        when(message.getId()).thenReturn(1);
        assertFalse(filter.filter(player, message));
    }

    /**
     * Verify that BlockBreakAnimationMessages are not filtered from non-author players.
     */
    @Test
    void filterOtherBlockBreakAnimationTest() {
        when(player.getEntityId()).thenReturn(1);
        BlockBreakAnimationMessage message = mock(BlockBreakAnimationMessage.class);
        when(message.getId()).thenReturn(2);
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

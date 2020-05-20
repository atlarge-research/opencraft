package net.glowstone.messaging.filters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.flowpowered.network.Message;
import net.glowstone.net.message.play.game.BlockBreakAnimationMessage;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerFilterTest {

    PlayerFilter filter;
    Player player;

    @BeforeEach
    void beforeEach() {
        filter = new PlayerFilter();
        player = mock(Player.class);
    }

    @Test
    void filterNullTest() {
        assertFalse(filter.filter(player, null));
    }

    @Test
    void filterAuthoredBlockBreakAnimationTest() {
        when(player.getEntityId()).thenReturn(1);
        BlockBreakAnimationMessage message = mock(BlockBreakAnimationMessage.class);
        when(message.getId()).thenReturn(1);
        assertFalse(filter.filter(player, message));
    }

    @Test
    void filterOtherBlockBreakAnimationTest() {
        when(player.getEntityId()).thenReturn(1);
        BlockBreakAnimationMessage message = mock(BlockBreakAnimationMessage.class);
        when(message.getId()).thenReturn(2);
        assertTrue(filter.filter(player, message));
    }

    @Test
    void filterMessageBaseTest() {
        Message message = mock(Message.class);
        assertTrue(filter.filter(player, message));
    }
}

package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.CombatEventMessage;
import net.glowstone.util.TextMessage;

public class CombatEventCodecTest extends CodecTest<CombatEventMessage> {

    @Override
    protected Codec<CombatEventMessage> createCodec() {
        return new CombatEventCodec();
    }

    @Override
    protected CombatEventMessage createMessage() {
        return new CombatEventMessage(CombatEventMessage.Event.ENTER_COMBAT, 2, 3);
    }
}

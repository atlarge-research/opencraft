package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.CombatEventMessage;

public class CombatEventCodecTest extends CodecTest<CombatEventMessage> {

    @Override
    protected Codec<CombatEventMessage> createCodec() {
        return new CombatEventCodec();
    }

    @Override
    protected CombatEventMessage createMessage() {
        return new CombatEventMessage(CombatEventMessage.Event.END_COMBAT, 2, 3);
    }
}

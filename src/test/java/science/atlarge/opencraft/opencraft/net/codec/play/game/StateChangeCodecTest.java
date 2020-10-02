package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.StateChangeMessage;

public class StateChangeCodecTest extends CodecTest<StateChangeMessage> {

    @Override
    protected Codec<StateChangeMessage> createCodec() {
        return new StateChangeCodec();
    }

    @Override
    protected StateChangeMessage createMessage() {
        return new StateChangeMessage(StateChangeMessage.Reason.GAMEMODE, 2.0f);
    }
}

package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.JoinGameMessage;

public class JoinGameCodecTest extends CodecTest<JoinGameMessage> {

    @Override
    protected Codec<JoinGameMessage> createCodec() {
        return new JoinGameCodec();
    }

    @Override
    protected JoinGameMessage createMessage() {
        return new JoinGameMessage(1, 2, 3, 4, 5, "six", false);
    }
}

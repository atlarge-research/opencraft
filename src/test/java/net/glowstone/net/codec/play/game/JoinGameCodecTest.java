package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.JoinGameMessage;

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

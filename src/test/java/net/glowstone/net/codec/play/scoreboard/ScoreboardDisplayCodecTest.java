package net.glowstone.net.codec.play.scoreboard;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.scoreboard.ScoreboardDisplayMessage;

public class ScoreboardDisplayCodecTest extends CodecTest<ScoreboardDisplayMessage> {

    @Override
    protected Codec<ScoreboardDisplayMessage> createCodec() {
        return new ScoreboardDisplayCodec();
    }

    @Override
    protected ScoreboardDisplayMessage createMessage() {
        return new ScoreboardDisplayMessage(1, "two");
    }
}

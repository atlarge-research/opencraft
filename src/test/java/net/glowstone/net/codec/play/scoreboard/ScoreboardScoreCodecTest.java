package net.glowstone.net.codec.play.scoreboard;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.scoreboard.ScoreboardScoreMessage;

public class ScoreboardScoreCodecTest extends CodecTest<ScoreboardScoreMessage> {

    @Override
    protected Codec<ScoreboardScoreMessage> createCodec() {
        return new ScoreboardScoreCodec();
    }

    @Override
    protected ScoreboardScoreMessage createMessage() {
        return new ScoreboardScoreMessage("one", "two", 3);
    }
}

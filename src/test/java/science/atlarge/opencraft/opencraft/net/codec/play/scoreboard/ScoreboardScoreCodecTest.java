package science.atlarge.opencraft.opencraft.net.codec.play.scoreboard;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.scoreboard.ScoreboardScoreMessage;

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

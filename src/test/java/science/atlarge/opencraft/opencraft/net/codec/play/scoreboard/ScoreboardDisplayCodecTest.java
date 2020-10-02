package science.atlarge.opencraft.opencraft.net.codec.play.scoreboard;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.scoreboard.ScoreboardDisplayMessage;

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

package science.atlarge.opencraft.opencraft.net.codec.play.scoreboard;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.scoreboard.ScoreboardObjectiveMessage;
import science.atlarge.opencraft.opencraft.scoreboard.RenderType;

public class ScoreboardObjectiveCodecTest extends CodecTest<ScoreboardObjectiveMessage> {

    @Override
    protected Codec<ScoreboardObjectiveMessage> createCodec() {
        return new ScoreboardObjectiveCodec();
    }

    @Override
    protected ScoreboardObjectiveMessage createMessage() {
        return ScoreboardObjectiveMessage.create("one", "two", RenderType.INTEGER);
    }
}

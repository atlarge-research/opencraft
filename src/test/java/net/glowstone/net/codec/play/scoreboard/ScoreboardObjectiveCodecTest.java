package net.glowstone.net.codec.play.scoreboard;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.scoreboard.ScoreboardObjectiveMessage;
import net.glowstone.scoreboard.RenderType;

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

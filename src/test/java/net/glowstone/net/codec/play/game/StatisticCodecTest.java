package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import java.util.HashMap;
import java.util.Map;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.StatisticMessage;

public class StatisticCodecTest extends CodecTest<StatisticMessage> {

    @Override
    protected Codec<StatisticMessage> createCodec() {
        return new StatisticCodec();
    }

    @Override
    protected StatisticMessage createMessage() {
        Map<String, Integer> values = new HashMap<>();
        values.put("one", 1);
        return new StatisticMessage(values);
    }
}

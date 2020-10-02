package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import java.util.ArrayList;
import java.util.List;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.BlockChangeMessage;
import net.glowstone.net.message.play.game.MultiBlockChangeMessage;

public class MultiBlockChangeCodecTest extends CodecTest<MultiBlockChangeMessage> {

    @Override
    protected Codec<MultiBlockChangeMessage> createCodec() {
        return new MultiBlockChangeCodec();
    }

    @Override
    protected MultiBlockChangeMessage createMessage() {
        List<BlockChangeMessage> records = new ArrayList<>();
        records.add(new BlockChangeMessage(1, 2, 3, 4));
        return new MultiBlockChangeMessage(1, 2, records);
    }
}

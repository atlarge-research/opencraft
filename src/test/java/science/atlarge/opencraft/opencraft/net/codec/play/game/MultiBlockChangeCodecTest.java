package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import java.util.ArrayList;
import java.util.List;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.BlockChangeMessage;
import science.atlarge.opencraft.opencraft.net.message.play.game.MultiBlockChangeMessage;

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

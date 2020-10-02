package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.RelativeEntityPositionMessage;

public class RelativeEntityPositionCodecTest extends CodecTest<RelativeEntityPositionMessage> {

    @Override
    protected Codec<RelativeEntityPositionMessage> createCodec() {
        return new RelativeEntityPositionCodec();
    }

    @Override
    protected RelativeEntityPositionMessage createMessage() {
        return new RelativeEntityPositionMessage(1, (short) 2, (short) 3, (short) 4, false);
    }
}

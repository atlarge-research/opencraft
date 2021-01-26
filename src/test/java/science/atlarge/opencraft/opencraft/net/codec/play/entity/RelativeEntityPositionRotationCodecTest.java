package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.RelativeEntityPositionRotationMessage;

public class RelativeEntityPositionRotationCodecTest extends CodecTest<RelativeEntityPositionRotationMessage> {

    @Override
    protected Codec<RelativeEntityPositionRotationMessage> createCodec() {
        return new RelativeEntityPositionRotationCodec();
    }

    @Override
    protected RelativeEntityPositionRotationMessage createMessage() {
        return new RelativeEntityPositionRotationMessage(1, (short) 2, (short) 3, (short) 4, 5, 6, false);
    }
}

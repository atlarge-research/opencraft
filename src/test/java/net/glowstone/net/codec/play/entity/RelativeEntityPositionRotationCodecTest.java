package net.glowstone.net.codec.play.entity;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.entity.RelativeEntityPositionRotationMessage;

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

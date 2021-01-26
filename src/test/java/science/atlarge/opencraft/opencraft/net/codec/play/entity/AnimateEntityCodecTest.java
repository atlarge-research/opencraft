package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.AnimateEntityMessage;

public class AnimateEntityCodecTest extends CodecTest<AnimateEntityMessage> {

    @Override
    protected Codec<AnimateEntityMessage> createCodec() {
        return new AnimateEntityCodec();
    }

    @Override
    protected AnimateEntityMessage createMessage() {
        return new AnimateEntityMessage(1, 2);
    }
}

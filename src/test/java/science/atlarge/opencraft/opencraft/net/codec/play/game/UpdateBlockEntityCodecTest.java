package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.UpdateBlockEntityMessage;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;

public class UpdateBlockEntityCodecTest extends CodecTest<UpdateBlockEntityMessage> {

    @Override
    protected Codec<UpdateBlockEntityMessage> createCodec() {
        return new UpdateBlockEntityCodec();
    }

    @Override
    protected UpdateBlockEntityMessage createMessage() {
        return new UpdateBlockEntityMessage(1, 2, 3, 4, new CompoundTag());
    }
}

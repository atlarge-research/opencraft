package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.UpdateBlockEntityMessage;
import net.glowstone.util.nbt.CompoundTag;

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

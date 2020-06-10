package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.UnloadChunkMessage;

public class UnloadChunkCodecTest extends CodecTest<UnloadChunkMessage> {

    @Override
    protected Codec<UnloadChunkMessage> createCodec() {
        return new UnloadChunkCodec();
    }

    @Override
    protected UnloadChunkMessage createMessage() {
        return new UnloadChunkMessage(1, 2);
    }
}

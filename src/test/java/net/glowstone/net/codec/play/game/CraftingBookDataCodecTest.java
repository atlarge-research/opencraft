package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.CraftingBookDataMessage;

public class CraftingBookDataCodecTest extends CodecTest<CraftingBookDataMessage> {

    @Override
    protected Codec<CraftingBookDataMessage> createCodec() {
        return new CraftingBookDataCodec();
    }

    @Override
    protected CraftingBookDataMessage createMessage() {
        return new CraftingBookDataMessage(1, false, true);
    }
}

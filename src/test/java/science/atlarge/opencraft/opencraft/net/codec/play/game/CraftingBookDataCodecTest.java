package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.CraftingBookDataMessage;

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

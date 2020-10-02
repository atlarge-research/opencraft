package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.CraftRecipeRequestMessage;

public class CraftRecipeRequestCodecTest extends CodecTest<CraftRecipeRequestMessage> {

    @Override
    protected Codec<CraftRecipeRequestMessage> createCodec() {
        return new CraftRecipeRequestCodec();
    }

    @Override
    protected CraftRecipeRequestMessage createMessage() {
        return new CraftRecipeRequestMessage(1, 2, false);
    }
}

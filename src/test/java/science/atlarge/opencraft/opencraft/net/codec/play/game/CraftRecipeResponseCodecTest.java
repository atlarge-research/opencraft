package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.CraftRecipeResponseMessage;

public class CraftRecipeResponseCodecTest extends CodecTest<CraftRecipeResponseMessage> {

    @Override
    protected Codec<CraftRecipeResponseMessage> createCodec() {
        return new CraftRecipeResponseCodec();
    }

    @Override
    protected CraftRecipeResponseMessage createMessage() {
        return new CraftRecipeResponseMessage(1, 2);
    }
}

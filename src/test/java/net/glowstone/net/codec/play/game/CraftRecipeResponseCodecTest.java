package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.CraftRecipeResponseMessage;

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

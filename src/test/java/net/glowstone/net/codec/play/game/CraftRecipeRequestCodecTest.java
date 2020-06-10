package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.CraftRecipeRequestMessage;

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

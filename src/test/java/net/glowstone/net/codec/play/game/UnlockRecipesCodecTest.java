package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.UnlockRecipesMessage;

public class UnlockRecipesCodecTest extends CodecTest<UnlockRecipesMessage> {

    @Override
    protected Codec<UnlockRecipesMessage> createCodec() {
        return new UnlockRecipesCodec();
    }

    @Override
    protected UnlockRecipesMessage createMessage() {
        return new UnlockRecipesMessage(1, true, false, new int[] {4, 5});
    }
}

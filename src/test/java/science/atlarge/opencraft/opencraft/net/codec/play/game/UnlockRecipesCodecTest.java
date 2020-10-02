package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.UnlockRecipesMessage;

public class UnlockRecipesCodecTest extends CodecTest<UnlockRecipesMessage> {

    @Override
    protected Codec<UnlockRecipesMessage> createCodec() {
        return new UnlockRecipesCodec();
    }

    @Override
    protected UnlockRecipesMessage createMessage() {
        return new UnlockRecipesMessage(1, true, false, new int[]{ 4, 5 });
    }
}

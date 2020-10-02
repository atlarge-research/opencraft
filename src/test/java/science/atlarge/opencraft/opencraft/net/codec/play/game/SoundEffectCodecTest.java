package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.SoundEffectMessage;
import org.bukkit.SoundCategory;

public class SoundEffectCodecTest extends CodecTest<SoundEffectMessage> {

    @Override
    protected Codec<SoundEffectMessage> createCodec() {
        return new SoundEffectCodec();
    }

    @Override
    protected SoundEffectMessage createMessage() {
        return new SoundEffectMessage(1, SoundCategory.MASTER, 3.0, 4.0, 5.0, 6.0f, 7.0f);
    }
}

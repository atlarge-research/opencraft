package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.game.NamedSoundEffectMessage;
import org.bukkit.SoundCategory;

public class NamedSoundEffectCodecTest extends CodecTest<NamedSoundEffectMessage> {

    @Override
    protected Codec<NamedSoundEffectMessage> createCodec() {
        return new NamedSoundEffectCodec();
    }

    @Override
    protected NamedSoundEffectMessage createMessage() {
        return new NamedSoundEffectMessage("one", SoundCategory.MASTER, 3.0, 4.0, 5.0, 6.0f, 7.0f);
    }
}

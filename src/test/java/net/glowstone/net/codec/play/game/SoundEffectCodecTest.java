package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.game.SoundEffectMessage;
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

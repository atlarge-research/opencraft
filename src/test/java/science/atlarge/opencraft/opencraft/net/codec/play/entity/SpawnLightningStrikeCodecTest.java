package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SpawnLightningStrikeMessage;

public class SpawnLightningStrikeCodecTest extends CodecTest<SpawnLightningStrikeMessage> {

    @Override
    protected Codec<SpawnLightningStrikeMessage> createCodec() {
        return new SpawnLightningStrikeCodec();
    }

    @Override
    protected SpawnLightningStrikeMessage createMessage() {
        return new SpawnLightningStrikeMessage(1, 2, 3.0, 4.0, 5.0);
    }
}

package science.atlarge.opencraft.opencraft.net.codec.play.player;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.player.SteerBoatMessage;

public class SteerBoatCodecTest extends CodecTest<SteerBoatMessage> {

    @Override
    protected Codec<SteerBoatMessage> createCodec() {
        return new SteerBoatCodec();
    }

    @Override
    protected SteerBoatMessage createMessage() {
        return new SteerBoatMessage(false, true);
    }
}

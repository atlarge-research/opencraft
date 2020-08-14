package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.SteerBoatMessage;

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

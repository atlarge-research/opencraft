package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.play.player.SteerVehicleMessage;

public class SteerVehicleCodecTest extends CodecTest<SteerVehicleMessage> {

    @Override
    protected Codec<SteerVehicleMessage> createCodec() {
        return new SteerVehicleCodec();
    }

    @Override
    protected SteerVehicleMessage createMessage() {
        return new SteerVehicleMessage(1.0f, 2.0f, false, true);
    }
}

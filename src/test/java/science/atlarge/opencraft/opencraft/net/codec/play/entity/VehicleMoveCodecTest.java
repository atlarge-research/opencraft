package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.VehicleMoveMessage;

public class VehicleMoveCodecTest extends CodecTest<VehicleMoveMessage> {

    @Override
    protected Codec<VehicleMoveMessage> createCodec() {
        return new VehicleMoveCodec();
    }

    @Override
    protected VehicleMoveMessage createMessage() {
        return new VehicleMoveMessage(1.0, 2.0, 3.0, 4.0f, 5.0f);
    }
}

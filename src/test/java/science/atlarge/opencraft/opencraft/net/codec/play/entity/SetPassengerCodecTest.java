package science.atlarge.opencraft.opencraft.net.codec.play.entity;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.play.entity.SetPassengerMessage;

public class SetPassengerCodecTest extends CodecTest<SetPassengerMessage> {

    @Override
    protected Codec<SetPassengerMessage> createCodec() {
        return new SetPassengerCodec();
    }

    @Override
    protected SetPassengerMessage createMessage() {
        int id = 1;
        int[] passengers = new int[] { 2, 3 };
        return new SetPassengerMessage(id, passengers);
    }
}

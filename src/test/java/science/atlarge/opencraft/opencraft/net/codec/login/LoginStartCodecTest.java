package science.atlarge.opencraft.opencraft.net.codec.login;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.login.LoginStartMessage;

public class LoginStartCodecTest extends CodecTest<LoginStartMessage> {

    @Override
    protected Codec<LoginStartMessage> createCodec() {
        return new LoginStartCodec();
    }

    @Override
    protected LoginStartMessage createMessage() {
        return new LoginStartMessage("one");
    }
}

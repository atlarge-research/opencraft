package science.atlarge.opencraft.opencraft.net.codec.login;

import com.flowpowered.network.Codec;
import science.atlarge.opencraft.opencraft.net.codec.CodecTest;
import science.atlarge.opencraft.opencraft.net.message.login.LoginSuccessMessage;

public class LoginSuccessCodecTest extends CodecTest<LoginSuccessMessage> {

    @Override
    protected Codec<LoginSuccessMessage> createCodec() {
        return new LoginSuccessCodec();
    }

    @Override
    protected LoginSuccessMessage createMessage() {
        return new LoginSuccessMessage("one", "two");
    }
}

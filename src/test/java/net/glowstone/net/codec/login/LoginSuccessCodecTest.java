package net.glowstone.net.codec.login;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.login.LoginSuccessMessage;

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

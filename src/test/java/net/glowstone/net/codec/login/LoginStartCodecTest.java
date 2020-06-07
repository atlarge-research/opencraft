package net.glowstone.net.codec.login;

import com.flowpowered.network.Codec;
import net.glowstone.net.codec.CodecTest;
import net.glowstone.net.message.login.LoginStartMessage;

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

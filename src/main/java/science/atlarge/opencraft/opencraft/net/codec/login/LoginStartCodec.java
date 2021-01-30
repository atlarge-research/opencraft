package science.atlarge.opencraft.opencraft.net.codec.login;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.login.LoginStartMessage;

public final class LoginStartCodec implements Codec<LoginStartMessage> {

    @Override
    public LoginStartMessage decode(ByteBuf buffer) throws IOException {
        return new LoginStartMessage(ByteBufUtils.readUTF8(buffer));
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, LoginStartMessage message) throws IOException {
        ByteBufUtils.writeUTF8(buffer, message.getUsername());
        return buffer;
    }
}

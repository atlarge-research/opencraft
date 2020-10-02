package science.atlarge.opencraft.opencraft.net.codec.login;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.login.LoginSuccessMessage;

public final class LoginSuccessCodec implements Codec<LoginSuccessMessage> {

    @Override
    public LoginSuccessMessage decode(ByteBuf buffer) throws IOException {
        String uuid = ByteBufUtils.readUTF8(buffer);
        String username = ByteBufUtils.readUTF8(buffer);
        return new LoginSuccessMessage(uuid, username);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, LoginSuccessMessage message) throws IOException {
        ByteBufUtils.writeUTF8(buffer, message.getUuid());
        ByteBufUtils.writeUTF8(buffer, message.getUsername());
        return buffer;
    }
}

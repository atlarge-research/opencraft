package science.atlarge.opencraft.opencraft.net.codec.login;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.login.EncryptionKeyResponseMessage;

public final class EncryptionKeyResponseCodec implements Codec<EncryptionKeyResponseMessage> {

    @Override
    public EncryptionKeyResponseMessage decode(ByteBuf buffer) throws IOException {

        byte[] sharedSecret = new byte[ByteBufUtils.readVarInt(buffer)];
        buffer.readBytes(sharedSecret);

        byte[] verifyToken = new byte[ByteBufUtils.readVarInt(buffer)];
        buffer.readBytes(verifyToken);

        return new EncryptionKeyResponseMessage(sharedSecret, verifyToken);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, EncryptionKeyResponseMessage message) {

        ByteBufUtils.writeVarInt(buffer, message.getSharedSecret().length);
        buffer.writeBytes(message.getSharedSecret());

        ByteBufUtils.writeVarInt(buffer, message.getVerifyToken().length);
        buffer.writeBytes(message.getVerifyToken());
        return buffer;
    }
}

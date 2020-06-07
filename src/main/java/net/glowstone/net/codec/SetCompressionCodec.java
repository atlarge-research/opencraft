package net.glowstone.net.codec;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.SetCompressionMessage;

public final class SetCompressionCodec implements Codec<SetCompressionMessage> {

    @Override
    public SetCompressionMessage decode(ByteBuf buffer) throws IOException {
        int threshold = ByteBufUtils.readVarInt(buffer);
        return new SetCompressionMessage(threshold);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, SetCompressionMessage message) {
        ByteBufUtils.writeVarInt(buffer, message.getThreshold());
        return buffer;
    }
}

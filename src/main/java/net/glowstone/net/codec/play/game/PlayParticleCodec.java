package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.play.game.PlayParticleMessage;

public final class PlayParticleCodec implements Codec<PlayParticleMessage> {

    @Override
    public PlayParticleMessage decode(ByteBuf buffer) throws IOException {

        int particle = buffer.readInt();
        boolean longDistance = buffer.readBoolean();
        float x = buffer.readFloat();
        float y = buffer.readFloat();
        float z = buffer.readFloat();
        float ofsX = buffer.readFloat();
        float ofsY = buffer.readFloat();
        float ofsZ = buffer.readFloat();
        float data = buffer.readFloat();

        int count = buffer.readInt();
        int[] extData = new int[count];
        for (int index = 0; index < count; index++) {
            extData[index] = ByteBufUtils.readVarInt(buffer);
        }

        return new PlayParticleMessage(particle, longDistance, x, y, z, ofsX, ofsY, ofsZ, data, count, extData);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, PlayParticleMessage message) {

        buffer.writeInt(message.getParticle());
        buffer.writeBoolean(message.isLongDistance());
        buffer.writeFloat(message.getX());
        buffer.writeFloat(message.getY());
        buffer.writeFloat(message.getZ());
        buffer.writeFloat(message.getOfsX());
        buffer.writeFloat(message.getOfsY());
        buffer.writeFloat(message.getOfsZ());
        buffer.writeFloat(message.getData());

        buffer.writeInt(message.getCount());
        for (int extData : message.getExtData()) {
            ByteBufUtils.writeVarInt(buffer, extData);
        }

        return buffer;
    }
}

package net.glowstone.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.message.play.game.SoundEffectMessage;
import org.bukkit.SoundCategory;

public class SoundEffectCodec implements Codec<SoundEffectMessage> {

    @Override
    public SoundEffectMessage decode(ByteBuf buffer) throws IOException {
        int sound = ByteBufUtils.readVarInt(buffer);
        int categoryIndex = ByteBufUtils.readVarInt(buffer);
        SoundCategory category = SoundCategory.values()[categoryIndex];
        double x = buffer.readInt() / 8.0;
        double y = buffer.readInt() / 8.0;
        double z = buffer.readInt() / 8.0;
        float volume = buffer.readFloat();
        float pitch = buffer.readFloat();
        return new SoundEffectMessage(sound, category, x, y, z, volume, pitch);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, SoundEffectMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buffer, message.getSound());
        ByteBufUtils.writeVarInt(buffer, message.getCategory().ordinal());
        buffer.writeInt((int) (8 * message.getX()));
        buffer.writeInt((int) (8 * message.getY()));
        buffer.writeInt((int) (8 * message.getZ()));
        buffer.writeFloat(message.getVolume());
        buffer.writeFloat(message.getPitch());
        return buffer;
    }
}

package science.atlarge.opencraft.opencraft.net.codec.play.game;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import science.atlarge.opencraft.opencraft.net.message.play.game.NamedSoundEffectMessage;
import org.bukkit.SoundCategory;

public final class NamedSoundEffectCodec implements Codec<NamedSoundEffectMessage> {

    @Override
    public NamedSoundEffectMessage decode(ByteBuf buffer) throws IOException {
        String sound = ByteBufUtils.readUTF8(buffer);
        int categoryIndex = ByteBufUtils.readVarInt(buffer);
        SoundCategory category = SoundCategory.values()[categoryIndex];
        double x = buffer.readInt() / 8.0;
        double y = buffer.readInt() / 8.0;
        double z = buffer.readInt() / 8.0;
        float volume = buffer.readFloat();
        float pitch = buffer.readFloat();
        return new NamedSoundEffectMessage(sound, category, x, y, z, volume, pitch);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, NamedSoundEffectMessage message) throws IOException {
        ByteBufUtils.writeUTF8(buffer, message.getSound());
        ByteBufUtils.writeVarInt(buffer, message.getSoundCategory().ordinal());
        buffer.writeInt((int) (8 * message.getX()));
        buffer.writeInt((int) (8 * message.getY()));
        buffer.writeInt((int) (8 * message.getZ()));
        buffer.writeFloat(message.getVolume());
        buffer.writeFloat(message.getPitch());
        return buffer;
    }
}

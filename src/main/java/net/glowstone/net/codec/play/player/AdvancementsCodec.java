package net.glowstone.net.codec.play.player;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import java.io.IOException;
import net.glowstone.advancement.GlowAdvancement;
import net.glowstone.net.message.play.player.AdvancementsMessage;
import org.bukkit.NamespacedKey;

public class AdvancementsCodec implements Codec<AdvancementsMessage> {

    @Override
    public AdvancementsMessage decode(ByteBuf buffer) throws IOException {
        throw new DecoderException("Cannot decode AdvancementsMessage");
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, AdvancementsMessage message) throws IOException {

        buffer.writeBoolean(message.isClear());

        ByteBufUtils.writeVarInt(buffer, message.getAdvancements().size());
        for (NamespacedKey key : message.getAdvancements().keySet()) {
            ByteBufUtils.writeUTF8(buffer, key.toString());
            GlowAdvancement advancement = (GlowAdvancement) message.getAdvancements().get(key);
            advancement.encode(buffer);
        }

        ByteBufUtils.writeVarInt(buffer, message.getRemoveAdvancements().size());
        for (NamespacedKey key : message.getRemoveAdvancements()) {
            ByteBufUtils.writeUTF8(buffer, key.toString());
        }

        ByteBufUtils.writeVarInt(buffer, 0);

        return buffer;
    }
}

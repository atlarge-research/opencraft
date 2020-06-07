package net.glowstone.net.codec.play.inv;

import com.flowpowered.network.Codec;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.glowstone.net.GlowBufUtils;
import net.glowstone.net.message.play.inv.OpenWindowMessage;
import net.glowstone.util.TextMessage;

public final class OpenWindowCodec implements Codec<OpenWindowMessage> {

    @Override
    public OpenWindowMessage decode(ByteBuf buffer) throws IOException {
        byte id = buffer.readByte();
        String type = ByteBufUtils.readUTF8(buffer);
        TextMessage title = GlowBufUtils.readChat(buffer);
        byte slots = buffer.readByte();
        int entityId = 0;
        if (buffer.readableBytes() >= Integer.BYTES) {
            entityId = buffer.getInt(buffer.readerIndex());
        }
        return new OpenWindowMessage(id, type, title, slots, entityId);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, OpenWindowMessage message) throws IOException {
        buffer.writeByte(message.getId());
        ByteBufUtils.writeUTF8(buffer, message.getType());
        GlowBufUtils.writeChat(buffer, message.getTitle());
        buffer.writeByte(message.getSlots());
        if (message.getEntityId() != 0) {
            // magic number 11 for AnimalChest type which Bukkit doesn't seem to know about
            buffer.writeInt(message.getEntityId());
        }
        return buffer;
    }
}

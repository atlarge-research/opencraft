package science.atlarge.opencraft.opencraft.net.codec.play.inv;

import com.flowpowered.network.Codec;
import io.netty.buffer.ByteBuf;
import science.atlarge.opencraft.opencraft.net.message.play.inv.TransactionMessage;

public final class TransactionCodec implements Codec<TransactionMessage> {

    @Override
    public TransactionMessage decode(ByteBuf buffer) {
        int id = buffer.readUnsignedByte();
        int action = buffer.readShort();
        boolean accepted = buffer.readBoolean();
        return new TransactionMessage(id, action, accepted);
    }

    @Override
    public ByteBuf encode(ByteBuf buffer, TransactionMessage message) {
        buffer.writeByte(message.getId());
        buffer.writeShort(message.getTransaction());
        buffer.writeBoolean(message.isAccepted());
        return buffer;
    }
}

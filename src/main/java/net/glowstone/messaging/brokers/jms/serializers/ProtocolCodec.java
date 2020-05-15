package net.glowstone.messaging.brokers.jms.serializers;

import com.flowpowered.network.Codec;
import com.flowpowered.network.Message;
import com.flowpowered.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.handler.codec.EncoderException;
import java.io.IOException;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;
import lombok.SneakyThrows;
import net.glowstone.messaging.brokers.jms.JmsCodec;
import net.glowstone.net.protocol.GlowProtocol;

public class ProtocolCodec implements JmsCodec<Message> {

    private final GlowProtocol protocol;
    private final ByteBufAllocator allocator;

    public ProtocolCodec(GlowProtocol protocol) {
        this.protocol = protocol;
        allocator = UnpooledByteBufAllocator.DEFAULT;
    }

    @SneakyThrows
    @Override
    public javax.jms.Message encode(Session session, Message message) throws JMSException {

        Class<? extends Message> clazz = message.getClass();
        Codec.CodecRegistration reg = protocol.getCodecRegistration(clazz);
        if (reg == null) {
            throw new EncoderException("Unknown message type: " + clazz + ".");
        }

        ByteBuf headerBuf = allocator.buffer(8);
        ByteBufUtils.writeVarInt(headerBuf, reg.getOpcode());

        ByteBuf messageBuf = allocator.buffer();
        messageBuf = reg.getCodec().encode(messageBuf, message);

        ByteBuf buffer = Unpooled.wrappedBuffer(headerBuf, messageBuf);
        int length = buffer.readableBytes();
        int index = buffer.readerIndex();
        byte[] bytes = new byte[length];
        buffer.getBytes(index, bytes);

        BytesMessage bytesMessage = session.createBytesMessage();
        bytesMessage.writeBytes(bytes);
        return bytesMessage;
    }

    @SneakyThrows
    @Override
    public Message decode(Session session, javax.jms.Message message) throws JMSException {

        if (!(message instanceof BytesMessage)) {
            throw new RuntimeException("Unsupported JMS message type");
        }

        BytesMessage bytesMessage = (BytesMessage) message;
        int length = (int) bytesMessage.getBodyLength();

        byte[] bytes = new byte[length];
        int read = bytesMessage.readBytes(bytes, length);

        if (read == -1) {
            throw new IOException("Reached end of stream");
        }

        if (read != length) {
            throw new IOException("Did not read enough bytes");
        }

        ByteBuf buffer = allocator.buffer(length);
        buffer.writeBytes(bytes);
        Codec<?> codec = protocol.newReadHeader(buffer);

        Message decoded = codec.decode(buffer);
        if (buffer.readableBytes() > 0) {
            throw new IOException("Received too many bytes");
        }

        return decoded;
    }
}

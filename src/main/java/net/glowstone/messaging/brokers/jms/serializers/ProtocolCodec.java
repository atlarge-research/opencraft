package net.glowstone.messaging.brokers.jms.serializers;

import com.flowpowered.network.Codec;
import com.flowpowered.network.Message;
import com.flowpowered.network.exception.IllegalOpcodeException;
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
import net.glowstone.messaging.brokers.jms.JmsCodec;
import net.glowstone.net.protocol.GlowProtocol;

/**
 * Codec used for encoding and decoding messages with the use of the GlowProtocol.
 */
public class ProtocolCodec implements JmsCodec<Message> {

    private final GlowProtocol protocol;
    private final ByteBufAllocator allocator;

    public ProtocolCodec(GlowProtocol protocol) {
        this.protocol = protocol;
        allocator = UnpooledByteBufAllocator.DEFAULT;
    }

    @Override
    public javax.jms.Message encode(Session session, Message message) throws JMSException {

        Class<? extends Message> clazz = message.getClass();
        Codec.CodecRegistration registration = protocol.getCodecRegistration(clazz);
        if (registration == null) {
            throw new EncoderException("Unknown message type: " + clazz + ".");
        }

        ByteBuf headerBuf = allocator.buffer(8);
        ByteBufUtils.writeVarInt(headerBuf, registration.getOpcode());

        ByteBuf messageBuf = allocator.buffer();
        try {
            Codec codec = registration.getCodec();
            messageBuf = codec.encode(messageBuf, message);
        } catch (IOException e) {
            throw new RuntimeException("Could not encode the message", e);
        }

        ByteBuf buffer = Unpooled.wrappedBuffer(headerBuf, messageBuf);
        int length = buffer.readableBytes();
        int index = buffer.readerIndex();
        byte[] bytes = new byte[length];
        buffer.getBytes(index, bytes);

        BytesMessage bytesMessage = session.createBytesMessage();
        bytesMessage.writeBytes(bytes);
        return bytesMessage;
    }

    @Override
    public Message decode(Session session, javax.jms.Message message) throws JMSException {

        if (!(message instanceof BytesMessage)) {
            throw new RuntimeException("Unsupported JMS message type");
        }

        BytesMessage bytesMessage = (BytesMessage) message;
        int length = (int) bytesMessage.getBodyLength();

        byte[] bytes = new byte[length];
        int read = bytesMessage.readBytes(bytes);

        if (read == -1) {
            throw new RuntimeException("Reached end of stream");
        }

        if (read != length) {
            throw new RuntimeException("Did not read enough bytes");
        }

        ByteBuf buffer = Unpooled.wrappedBuffer(bytes);

        Codec<?> codec;
        Message decoded;
        try {
            codec = protocol.newReadHeader(buffer);
            decoded = codec.decode(buffer);
        } catch (IOException | IllegalOpcodeException e) {
            throw new RuntimeException("Failed to retrieve codec and decode message", e);
        }

        return decoded;
    }
}

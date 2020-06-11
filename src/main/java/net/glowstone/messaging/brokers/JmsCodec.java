package net.glowstone.messaging.brokers;

import javax.jms.JMSException;
import javax.jms.Session;

/**
 * The codec that handles encoding and decoding of generic type messages. Jms requires its own type of messages, the
 * generic type messages that are provided have to be encoded to a jms message. When receiving a message, the jms
 * message has to be decoded back to the original message.
 *
 * @param <Message> The generic type of message to be encoded.
 */
public interface JmsCodec<Message> {

    /**
     * Encode a generic type message to a jms message.
     *
     * @param session The jms session used to generate a jms message.
     * @param message The message to be encoded.
     * @return The encoded jms message.
     */
    javax.jms.Message encode(Session session, Message message) throws JMSException;

    /**
     * Decode the jms message to the original generic message.
     *
     * @param session The jms session used to generate the jms message.
     * @param message The encoded jms message.
     * @return The original message.
     */
    Message decode(Session session, javax.jms.Message message) throws JMSException;
}

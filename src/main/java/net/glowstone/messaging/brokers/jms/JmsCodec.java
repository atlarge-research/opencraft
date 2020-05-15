package net.glowstone.messaging.brokers.jms;

import javax.jms.JMSException;
import javax.jms.Session;

public interface JmsCodec<Message> {

    javax.jms.Message encode(Session session, Message message) throws JMSException;

    Message decode(Session session, javax.jms.Message message) throws JMSException;
}

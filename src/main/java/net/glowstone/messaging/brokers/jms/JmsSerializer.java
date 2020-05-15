package net.glowstone.messaging.brokers.jms;

import javax.jms.Session;

public interface JmsSerializer<Message> {

    javax.jms.Message serialize(Session session, Message message);

    Message deserialize(Session session, javax.jms.Message message);
}

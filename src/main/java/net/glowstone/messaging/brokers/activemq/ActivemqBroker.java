package net.glowstone.messaging.brokers.activemq;

import javax.jms.JMSException;
import net.glowstone.messaging.brokers.jms.JmsBroker;
import net.glowstone.messaging.brokers.jms.JmsSerializer;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ActivemqBroker<Topic, Subscriber, Message> extends JmsBroker<Topic, Subscriber, Message> {

    public ActivemqBroker(String uri, JmsSerializer<Message> serializer) throws JMSException {
        super(new ActiveMQConnectionFactory(uri), serializer);
    }
}

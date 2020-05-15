package net.glowstone.messaging.brokers.activemq;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.jms.JMSException;
import net.glowstone.messaging.Broker;
import net.glowstone.messaging.brokers.jms.JmsBroker;
import net.glowstone.messaging.brokers.jms.JmsCodec;
import net.glowstone.messaging.brokers.jms.serializers.ProtocolCodec;
import net.glowstone.net.message.play.game.ChatMessage;
import net.glowstone.net.protocol.GlowProtocol;
import net.glowstone.net.protocol.PlayProtocol;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ActivemqBroker<Topic, Subscriber, Message> extends JmsBroker<Topic, Subscriber, Message> {

    public ActivemqBroker(String uri, JmsCodec<Message> serializer) throws JMSException {
        super(new ActiveMQConnectionFactory(uri), serializer);
    }

    public static void main(String[] args) throws JMSException, ExecutionException, InterruptedException {

        System.out.println("start setup broker");

        String uri = ActiveMQConnection.DEFAULT_BROKER_URL;
        GlowProtocol protocol = new PlayProtocol();
        ProtocolCodec serializer = new ProtocolCodec(protocol);
        Broker<String, String, com.flowpowered.network.Message> broker = new ActivemqBroker<>(uri, serializer);
        System.out.println("finish setup broker");

        CompletableFuture<String> future = new CompletableFuture<>();

        broker.subscribe("topic", "subscriber", message -> future.complete("complete"));
        System.out.println("subscribed");

        ChatMessage message = new ChatMessage("Hello, Subscriber!");
        broker.publish("topic", message);
        System.out.println("published");

        System.out.println(future.get());
    }

}

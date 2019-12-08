package by.deniskruglik.queuemessagingreceiver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receiver {
    private final static String HOST = "localhost";
    private final static String QUEUE_NAME = "test";

    private Logger logger = LoggerFactory.getLogger(Receiver.class);

    public void listen() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            logger.info("Waiting for messages.");
            DeliverCallback deliverCallback = new SendToLambdaDeliveryCallback();
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
        } catch (TimeoutException | IOException e) {
            logger.error("Failed to establish connection to RabbitMQ", e);
            throw new IllegalStateException("Failed to establish connection to RabbitMQ", e);
        }
    }
}

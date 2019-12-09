package by.deniskruglik.queuemessagingsender.services;

import by.deniskruglik.queuemessagingsender.exception.MessageServiceException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MessageService {
    private final static String HOST = "localhost";
    private final static String QUEUE_NAME = "test";

    private Logger logger = LogManager.getLogger(MessageService.class);

    public void sendMessage(String message) throws MessageServiceException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        } catch (TimeoutException | IOException e) {
            logger.error("Failed to establish connection to RabbitMQ", e);
            throw new MessageServiceException("Failed to establish connection to RabbitMQ", e);
        }
    }
}

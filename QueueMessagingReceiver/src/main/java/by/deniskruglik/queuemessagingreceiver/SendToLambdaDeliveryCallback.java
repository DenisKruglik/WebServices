package by.deniskruglik.queuemessagingreceiver;

import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendToLambdaDeliveryCallback implements DeliverCallback {
    private final static String LAMBDA_URL = "https://xceergiax4.execute-api.us-east-1.amazonaws.com/dev/wrap-links";
    private final static String FROM = "den4ik6113@gmail.com";
    private final static String TO = "d.kruglik11011@gmail.com";
    private final static String HOST = "localhost";
    private final static String SUBJECT = "Message from RabbitMQ";

    private Logger logger = LoggerFactory.getLogger(SendToLambdaDeliveryCallback.class);

    @Override
    public void handle(String s, Delivery delivery) {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        String processedMessage;
        try {
            processedMessage = requestProcessedMessage(message);
        } catch (IOException e) {
            logger.error("Failed to get processed message from lambda", e);
            return;
        }
        try {
            sendEmail(processedMessage);
        } catch (MessagingException e) {
            logger.error("Failed to send email", e);
        }
    }

    private String requestProcessedMessage(String message) throws IOException {
        HttpPost post = new HttpPost(LAMBDA_URL);
        StringEntity myEntity = new StringEntity(message, ContentType.create("text/plain", "UTF-8"));
        post.setEntity(myEntity);
        String result;
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post)){
            result = EntityUtils.toString(response.getEntity());
        }

        return result;
    }

    private void sendEmail(String text) throws MessagingException {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", HOST);
        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        message.setSubject(SUBJECT);
        message.setText(text);
        Transport.send(message);
        logger.info("Message sent successfully");
    }
}

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SendToLambdaDeliveryCallback implements DeliverCallback {
    private final static String LAMBDA_URL = "https://uqrrxk46ak.execute-api.us-east-1.amazonaws.com/dev/process";
    private final static String FROM = "den4ik6113@gmail.com";
    private final static String TO = "d.kruglik11011@gmail.com";
    private final static String SUBJECT = "Message from RabbitMQ";
    private final static String SMTP_HOST = "email-smtp.eu-central-1.amazonaws.com";
    private final static String SMTP_USERNAME = "AKIAW2CC2ZAEGI2FYBOF";
    private final static String SMTP_PASSWORD = "BAndXe2jzt02Lw73ORFlHvkywlqEIc7m0jyVZIRUL7h3";
    private final static String INSERT_SQL = "INSERT INTO messages (content) VALUES (?);";

    private Logger logger = LogManager.getLogger(SendToLambdaDeliveryCallback.class);

    private Connection connection = Connector.getConnection();

    @Override
    public void handle(String s, Delivery delivery) {
        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        logger.info("Received message: " + message);
        String processedMessage;
        try {
            processedMessage = requestProcessedMessage(message).replaceAll("^\"(.*)\"$", "$1");
        } catch (IOException e) {
            logger.error("Failed to get processed message from lambda", e);
            return;
        }
        try {
            pushToDatabase(processedMessage);
        } catch (SQLException e) {
            logger.error("Failed to push to database", e);
        }
        sendEmail(processedMessage);
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

    private void sendEmail(String text) {
        logger.info("Sending email");
        Email email = EmailBuilder.startingBlank()
                .from(FROM)
                .to(TO)
                .withSubject(SUBJECT)
                .withHTMLText(text)
                .buildEmail();

        Mailer mailer = MailerBuilder
                .withSMTPServer(SMTP_HOST, 587, SMTP_USERNAME, SMTP_PASSWORD)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withSessionTimeout(10 * 1000)
                .clearEmailAddressCriteria() // turns off email validation
                .withDebugLogging(true)
                .buildMailer();

        mailer.sendMail(email);
        logger.info("Message sent successfully");
    }

    private boolean pushToDatabase(String message) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(INSERT_SQL);
        statement.setString(1, message);
        return statement.execute();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        connection.close();
    }
}

package by.deniskruglik.queuemessagingsender;

import by.deniskruglik.queuemessagingsender.controllers.MessageController;
import by.deniskruglik.queuemessagingsender.jobs.MessageSendJob;
import by.deniskruglik.queuemessagingsender.utils.JsonTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static spark.Spark.port;
import static spark.Spark.post;

public class App {
    private final static String SEND_MESSAGE_URL = "/message/send/";

    private MessageController messageController = new MessageController();

    private JsonTransformer jsonTransformer = new JsonTransformer();

    private Logger logger = LogManager.getLogger(App.class);

    private final static String CRON_EXPRESSION = "0 0/1 * * * ?";

    public void run() {
        port(9000);

        post(SEND_MESSAGE_URL, messageController.sendMessage, jsonTransformer);

        scheduleAutomaticMessageSending();
    }

    private void scheduleAutomaticMessageSending() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobDetail jobDetail = JobBuilder.newJob()
                    .withIdentity("MessageSendJob", "MessageSendJob")
                    .ofType(MessageSendJob.class)
                    .build();
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withSchedule(CronScheduleBuilder.cronSchedule(CRON_EXPRESSION))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            logger.error("Failed to schedule a cron job", e);
            throw new IllegalStateException("Failed to schedule a cron job", e);
        }
    }
}

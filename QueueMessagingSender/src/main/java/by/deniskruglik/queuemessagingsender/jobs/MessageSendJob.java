package by.deniskruglik.queuemessagingsender.jobs;

import by.deniskruglik.queuemessagingsender.exception.MessageServiceException;
import by.deniskruglik.queuemessagingsender.services.MessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MessageSendJob implements Job {
    private MessageService messageService = new MessageService();

    private Logger logger = LogManager.getLogger(MessageSendJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Sending message with cron job");
        try {
            messageService.sendMessage("Message sent automatically, current timestamp: " + System.currentTimeMillis());
        } catch (MessageServiceException e) {
            logger.catching(e);
        }
    }
}

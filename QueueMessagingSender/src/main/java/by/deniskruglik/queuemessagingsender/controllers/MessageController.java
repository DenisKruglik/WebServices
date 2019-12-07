package by.deniskruglik.queuemessagingsender.controllers;

import by.deniskruglik.queuemessagingsender.exception.MessageServiceException;
import by.deniskruglik.queuemessagingsender.services.MessageService;
import by.deniskruglik.queuemessagingsender.utils.JsonBodyConverter;
import by.deniskruglik.queuemessagingsender.utils.ResponseUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

public class MessageController {
    private final static String MESSAGE_KEY = "message";

    private MessageService messageService = new MessageService();

    public final Route sendMessage = (Request request, Response response) -> {
        Map<String, String> bodyParams = JsonBodyConverter.convertJsonBodyToMap(request);
        if (!bodyParams.containsKey(MESSAGE_KEY)) {
            return ResponseUtils.errorMessage(response, "Invalid request body");
        }
        String message = bodyParams.get(MESSAGE_KEY);
        try {
            messageService.sendMessage(message);
            return ResponseUtils.successMessage(response, "Message sent successfully");
        } catch (MessageServiceException e) {
            return ResponseUtils.errorMessage(response, e.getMessage());
        }
    };
}

package com.qorvia.blogfeedbackservice.clients;

import com.qorvia.blogfeedbackservice.dto.message.ValidateEventMessage;
import com.qorvia.blogfeedbackservice.dto.message.response.ValidateEventMessageResponse;
import com.qorvia.blogfeedbackservice.messaging.RabbitMQSender;
import com.qorvia.blogfeedbackservice.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventManagementClient {

    private final RabbitMQSender rabbitMQSender;

    public Boolean validateEvent(String eventId){
        ValidateEventMessage message = new ValidateEventMessage();
        message.setEventId(eventId);
        try {
            ValidateEventMessageResponse response = rabbitMQSender.sendRpcMessage(
                    AppConstants.EVENT_MANAGEMENT_SERVICE_RPC_QUEUE,
                    AppConstants.EVENT_MANAGEMENT_SERVICE_RPC_EXCHANGE,
                    AppConstants.EVENT_MANAGEMENT_SERVICE_RPC_ROUTING_KEY,
                    message,
                    ValidateEventMessageResponse.class);
            return response.getIsValid();
        } catch (TimeoutException | IOException e) {
            log.error("Error generating account link for onboarding", e);
            return null;
        }
    }




}

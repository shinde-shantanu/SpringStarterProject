package com.example.demoCustomer.customer;

import com.example.demoCustomer.SolaceService;
import com.solace.messaging.publisher.OutboundMessage;
import com.solace.messaging.resources.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {

    @Autowired
    private SolaceService solaceService;

    private static final Logger logger = LogManager.getLogger(LoggingService.class);

    public void makeInfoLog(String message) {
        logger.info(message);
        OutboundMessage outboundMessage = solaceService.getMessageBuilder().build("INFO: " + message);
        solaceService.getPublisher().publish(outboundMessage, Topic.of(solaceService.getQueue()));
    }

    public void makeDebugLog(String message) {
        logger.debug(message);
        OutboundMessage outboundMessage = solaceService.getMessageBuilder().build("DEBUG: " + message);
        solaceService.getPublisher().publish(outboundMessage, Topic.of(solaceService.getQueue()));
    }

    public void makeErrorLog(String message) {
        logger.error(message);
        OutboundMessage outboundMessage = solaceService.getMessageBuilder().build("ERROR: " + message);
        solaceService.getPublisher().publish(outboundMessage, Topic.of(solaceService.getQueue()));
    }

}

package com.example.demoCustomer.customer;

import com.example.demoCustomer.SolaceService;
import com.solace.messaging.PubSubPlusClientException;
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
        OutboundMessage outboundMessage = solaceService.getMessageBuilder().build(String.format("INFO: "));
        try{
            solaceService.getPublisher().publish(outboundMessage, Topic.of("loggingQueue"));
        } catch (PubSubPlusClientException e) {
            throw new RuntimeException(e);
        }
    }

    public void makeDebugLog(String message) {
        logger.debug(message);
        OutboundMessage outboundMessage = solaceService.getMessageBuilder().build(String.format("DEBUG: " + message));
        try{
            solaceService.getPublisher().publish(outboundMessage, Topic.of(solaceService.getQueue()));
        } catch (PubSubPlusClientException e) {
            throw new RuntimeException(e);
        }
    }

    public void makeErrorLog(String message) {
        logger.error(message);
        OutboundMessage outboundMessage = solaceService.getMessageBuilder().build(String.format("ERROR: " + message));
        try{
            solaceService.getPublisher().publish(outboundMessage, Topic.of(solaceService.getQueue()));
        } catch (PubSubPlusClientException e) {
            throw new RuntimeException(e);
        }
    }

}

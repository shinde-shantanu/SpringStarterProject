package com.example.demoCustomer.Logging;

import com.example.demoCustomer.SolaceService;
import com.solace.messaging.PubSubPlusClientException;
import com.solace.messaging.publisher.OutboundMessage;
import com.solace.messaging.resources.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LogService {

    private final LogRepository logRepository;
    @Autowired
    private SolaceService solaceService;
    private static final Logger logger = LogManager.getLogger(LogService.class);

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void writeLog(String level, String message) {
        writeLocalLog(level, message);
        writeSolaceLog(message);
        writeCassandraLog(level, message);
    }

    public void writeLocalLog(String level, String message) {
        if(level == "INFO") {
            logger.info(message);
        } else if (level == "ERROR") {
            logger.error(message);
        }
        else {
            logger.debug(message);
        }
    }

    public void writeSolaceLog(String message) {
        OutboundMessage outboundMessage = solaceService.getMessageBuilder().build(String.format("DEBUG: " + message));
        try{
            solaceService.getPublisher().publish(outboundMessage, Topic.of(solaceService.getQueue()));
        } catch (PubSubPlusClientException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeCassandraLog(String level, String message) {
        Log log = new Log();
        log.setId(UUID.randomUUID());
        log.setTimestamp(LocalDateTime.now());
        log.setLevel(level);
        log.setMessage(message);

        logRepository.save(log);
    }

}
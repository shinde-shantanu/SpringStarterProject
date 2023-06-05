package com.example.demoCustomer;

import com.solace.messaging.MessagingService;
import com.solace.messaging.config.SolaceProperties;
import com.solace.messaging.config.profile.ConfigurationProfile;
import com.solace.messaging.publisher.DirectMessagePublisher;
import com.solace.messaging.publisher.OutboundMessageBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class SolaceService {

    @Value("${solace.host}")
    private String host;

    @Value("${solace.port}")
    private String port;

    @Value("${solace.vpnName}")
    private String vpnName;

    @Value("${solace.username}")
    private String username;

    @Value("${solace.queue}")
    private String queue;

    @Value("${solace.password}")
    private String password;

    private Properties properties;

    private MessagingService messagingService;

    private DirectMessagePublisher publisher;

    private OutboundMessageBuilder messageBuilder;

    public SolaceService() {
    }

    @PostConstruct
    public void initialize() {
        properties = new Properties();
        properties.setProperty(SolaceProperties.TransportLayerProperties.HOST, host + ":" + port ); //host:port
        properties.setProperty(SolaceProperties.ServiceProperties.VPN_NAME, vpnName);
        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_USER_NAME, username);
        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_PASSWORD, password);
        properties.setProperty(SolaceProperties.ServiceProperties.RECEIVER_DIRECT_SUBSCRIPTION_REAPPLY, "true");

        messagingService = MessagingService.builder(ConfigurationProfile.V1)
                .fromProperties(properties).build().connect();

        publisher = messagingService.createDirectMessagePublisherBuilder().onBackPressureWait(1).build().start();

        messageBuilder = messagingService.messageBuilder();
    }

    public DirectMessagePublisher getPublisher() {
        return publisher;
    }

    public void setPublisher(DirectMessagePublisher publisher) {
        this.publisher = publisher;
    }

    public OutboundMessageBuilder getMessageBuilder() {
        return messageBuilder;
    }

    public void setMessageBuilder(OutboundMessageBuilder messageBuilder) {
        this.messageBuilder = messageBuilder;
    }

    public String getQueue() {
        return queue;
    }
}

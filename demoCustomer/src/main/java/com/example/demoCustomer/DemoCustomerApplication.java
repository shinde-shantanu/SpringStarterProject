package com.example.demoCustomer;

import com.solace.messaging.MessagingService;
import com.solace.messaging.PubSubPlusClientException;
import com.solace.messaging.config.SolaceProperties;
import com.solace.messaging.config.profile.ConfigurationProfile;
import com.solace.messaging.publisher.DirectMessagePublisher;
import com.solace.messaging.publisher.OutboundMessage;
import com.solace.messaging.publisher.OutboundMessageBuilder;
import com.solace.messaging.publisher.PersistentMessagePublisher;
import com.solace.messaging.resources.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@SpringBootApplication
@RestController
public class DemoCustomerApplication {

	@Autowired
	private SolaceService solaceService;

	public static void main(String[] args) {
		SpringApplication.run(DemoCustomerApplication.class, args);
	}

	@Bean
	public void makeMessage() throws InterruptedException {
		final Properties properties = new Properties();
		properties.setProperty(SolaceProperties.TransportLayerProperties.HOST, "localhost:55554"); //host:port
		properties.setProperty(SolaceProperties.ServiceProperties.VPN_NAME, "CustomerApplicationMessages");
		properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_USER_NAME, "admin");
		properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_PASSWORD, "admin");
		properties.setProperty(SolaceProperties.ServiceProperties.RECEIVER_DIRECT_SUBSCRIPTION_REAPPLY, "true");

		final MessagingService messagingService = MessagingService.builder(ConfigurationProfile.V1)
				.fromProperties(properties).build().connect();

		final PersistentMessagePublisher publisher1 = messagingService.createPersistentMessagePublisherBuilder().build().start();
		final DirectMessagePublisher publisher = messagingService.createDirectMessagePublisherBuilder().onBackPressureWait(1).build().start();

		OutboundMessageBuilder messageBuilder = messagingService.messageBuilder();
		OutboundMessage message = messageBuilder.build(String.format("Hello world!"));
		String topicString = "solace/samples" + "Java".toLowerCase() + "/hello/" + "uni";
		try {
			publisher.publish(message, Topic.of("loggingQueue"));
		} catch (PubSubPlusClientException e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public void makeMessage2() throws InterruptedException {

		OutboundMessage message = solaceService.getMessageBuilder().build(String.format("Hello world! 2"));
		try {
			solaceService.getPublisher().publish(message, Topic.of("loggingQueue"));
		} catch (PubSubPlusClientException e) {
			throw new RuntimeException(e);
		}
	}

}

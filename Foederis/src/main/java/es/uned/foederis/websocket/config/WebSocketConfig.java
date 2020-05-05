package es.uned.foederis.websocket.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");   // Enables a simple in-memory broker


        
        //   Use this for enabling a Full featured broker like RabbitMQ

        
        //registry.enableStompBrokerRelay("/topic")
        //        .setRelayHost("localhost")
        //        .setRelayPort(61613)
        //        .setClientLogin("guest")
        //        .setClientPasscode("guest");
        
    }
    
    private final List<WebSocketMessageBrokerConfigurer> configurers = new ArrayList<WebSocketMessageBrokerConfigurer>();


	@Autowired(required = false)
	public void setConfigurers(List<WebSocketMessageBrokerConfigurer> configurers) {
		if (!CollectionUtils.isEmpty(configurers)) {
			this.configurers.addAll(configurers);
		}
	}

   @Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		boolean registerDefaults = true;
		for (WebSocketMessageBrokerConfigurer configurer : this.configurers) {
			if (!configurer.configureMessageConverters(messageConverters)) {
				registerDefaults = false;
			}
		}
		return registerDefaults;
	}
   
	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		for (WebSocketMessageBrokerConfigurer configurer : this.configurers) {
			configurer.addReturnValueHandlers(returnValueHandlers);
		}
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
		for (WebSocketMessageBrokerConfigurer configurer : this.configurers) {
			configurer.configureWebSocketTransport(registry);
		}
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registry) {
		for (WebSocketMessageBrokerConfigurer configurer : this.configurers) {
			configurer.configureClientInboundChannel(registry);
		}
	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration registry) {
		for (WebSocketMessageBrokerConfigurer configurer : this.configurers) {
			configurer.configureClientOutboundChannel(registry);
		}
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		for (WebSocketMessageBrokerConfigurer configurer : this.configurers) {
			configurer.addArgumentResolvers(argumentResolvers);
		}
	}

   
}


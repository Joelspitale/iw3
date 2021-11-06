package ar.edu.iua.iw3;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker	//habilitamos un servidor exclusivo para ver el tema de los webSocket
public class webSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/iw3");
		registry.setApplicationDestinationPrefixes("/wsin");	//para poder recibir msj
	}

	@Override	//stomp es una forma de empaquetar los ptes de http
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/api/v1/ws").setAllowedOrigins("*").withSockJS();
	}

}

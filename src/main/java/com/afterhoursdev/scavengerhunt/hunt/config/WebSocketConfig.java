package com.afterhoursdev.scavengerhunt.hunt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.afterhoursdev.scavengerhunt.hunt.service.WebSocketHandler;


/**
 * The WebSocketConfig class contains the WebSocket-related components. Classes defined
 * in a @Configuration class are created and managed by the Spring IoC container when the
 * application context is being initialized. When classes are annotated with @EnableWebSocket, 
 * Spring scans for WebSocket-related components and set up the necessary infrastructure
 * to handle WebSocket connections.
 *   
 * @author  Jim Zombek
 * @version 1.0
 * @since   12-6-2024
*/

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	private final WebSocketHandler webSocketHandler;
	
    public WebSocketConfig(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "ws/scavengerhunt").setAllowedOrigins("*");
    }
}
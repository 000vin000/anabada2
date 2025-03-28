package kr.co.anabada.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import kr.co.anabada.chat.handler.ChatHandler;

@Configuration
@EnableWebSocket
public class ChatConfig implements WebSocketConfigurer {
    private final ChatHandler chatHandler;

    public ChatConfig(ChatHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler, "/chat/{roomNo}")
                .setAllowedOrigins("*")  
                .addInterceptors(new HttpSessionHandshakeInterceptor()); 
    }
    
}


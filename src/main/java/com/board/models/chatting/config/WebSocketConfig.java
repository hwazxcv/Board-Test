package com.board.models.chatting.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

// WebSocket 핸들러를 등록하는 메서드
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig  implements WebSocketConfigurer {

    private final ChatHandler chatHandler;


    /**
     * @param registry : WebSocket 핸들러를 등록하는데 사용되는 객체
     *
     * /chat -> 경로로 들어오는 WebSocket요청을 처리
     * setAllowedOrigins : CORS 설정( * -> 모든 도메인에서 허용)
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler , "chat").setAllowedOrigins("*");
    }
}

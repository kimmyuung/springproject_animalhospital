package animalhospital.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Autowired
    private WebMsgSocketHandler webMsgSocketHandler;

    @Autowired
    private AnswerWebSocketHandler answerWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler ,"ws/message/*" ).setAllowedOrigins("*")
                .addHandler(webMsgSocketHandler, "/ws/member/shopchat").setAllowedOrigins("*")
                .addHandler(answerWebSocketHandler, "/ws/member/shopchat").setAllowedOrigins("*");

    }
}
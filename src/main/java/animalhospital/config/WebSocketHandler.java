package animalhospital.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private Map<WebSocketSession, String> list = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("서버에 접속했습니다." + session);
        String path = session.getUri().getPath();
        String mid = path.substring(path.lastIndexOf("/")+1);
        list.put(session, mid);
//        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        for(WebSocketSession socketobject : list) {
            socketobject.sendMessage(message);
            System.out.println(message);
        }
//        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("서버에서 나갔습니다."+session);
        list.remove(session);
//        super.afterConnectionClosed(session, status);
    }
}

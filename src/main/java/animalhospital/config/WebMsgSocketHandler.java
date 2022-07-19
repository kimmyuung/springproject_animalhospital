package animalhospital.config;


import animalhospital.service.MessageService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebMsgSocketHandler extends TextWebSocketHandler {

    private Map<WebSocketSession, String> list = new HashMap<>();

    @Autowired
    private MessageService memberService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String path = session.getUri().getPath();
        String mid = path.substring(path.lastIndexOf("/") + 1);
        list.put(session, mid);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        JSONObject object = new JSONObject( message.getPayload() );

        // DB 처리
        memberService.messagesend( object );
        // 현재 접속된 세션들중에 받는사람(to) 와 같은경우 소켓 메시지 전달
        for( WebSocketSession socketSession : list.keySet()  ){    // 모든 키값 호출
            if( list.get( socketSession).equals( object.get("to")  ) ){
                socketSession.sendMessage( message );
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}
package com.zrg.rtspdemo.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WsHandler extends BinaryWebSocketHandler {
    /**
     * 存放所有在线的客户端
     */
    private static Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();

    public static Map<String, WebSocketSession> getClients() {
        return clients;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> map = session.getAttributes();
        String sessionId = null == map.get("uid") ? null : map.get("uid").toString();
        log.info("-------------新的加入htSessionId:" + sessionId + "---------------------");
        if (sessionId != null) {
            clients.put(sessionId, session);
        } else {
            clients.put(session.getId(), session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("------------------退出的-----------------------------");
        Map<String, Process> processMap = ConvertVideoPakcet.getProcessMap();
        Map<String, Object> map = session.getAttributes();
        String sessionId = null == map.get("uid") ? null : map.get("uid").toString();
        if (sessionId != null) {
            clients.remove(sessionId);
            Process process = processMap.get(sessionId);
            try {
                // process.waitFor();
                process.destroy();
            } catch (Exception e) {
                // e.printStackTrace();
            }


        } else {
            clients.remove(session.getId());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        Map<String, Object> map = session.getAttributes();
        String htSessionId = map.get("HTTP.SESSION.ID") == null ? null : map.get("HTTP.SESSION.ID").toString();
        if (htSessionId != null) {
            clients.remove(htSessionId);
        } else {
            clients.remove(session.getId());
        }
    }

    /**
     * 核心代码，用来推送信息给websocket
     *
     * @param data
     */
    public void sendVideo(byte[] data) {
        try {
            BinaryMessage binaryMessage = new BinaryMessage(data);
            for (Map.Entry<String, WebSocketSession> sessionEntry : clients.entrySet()) {
                try {
                    WebSocketSession session = sessionEntry.getValue();
                    if (session.isOpen()) {
                        session.sendMessage(binaryMessage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自己改的代码，因为需要区分推给那个websocket
     *
     * @param data
     * @param session
     */
    public synchronized void sendOneVideo(byte[] data, WebSocketSession session) {
        try {
            BinaryMessage binaryMessage = new BinaryMessage(data);
            try {
                if (session.isOpen()) {
                    session.sendMessage(binaryMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 这个方法我没用到，应该是用来人脸识别返回截图的
     *
     * @param data
     * @param path
     */
    public void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals("")) {
            return;
        }
        try {
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            log.info("Make Picture success,Please find image in " + path);
        } catch (Exception ex) {
            log.error("Exception: " + ex);
            ex.printStackTrace();
        }
    }
}

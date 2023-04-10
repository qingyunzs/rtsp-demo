package com.zrg.rtspdemo.controller;

import com.zrg.rtspdemo.ws.WsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/rtsp")
public class SocketController {
    @Autowired
    private WsHandler wsHandler;

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/webSocket")
    public ModelAndView socket() {
        ModelAndView mav = new ModelAndView("/webSocket");
//        mav.addObject("userId", userId);
        return mav;
    }

    /**
     * WebSocket 接RTSP流接口
     *
     * @param request
     * @param response
     * @param id
     * @return
     */
    @RequestMapping("/receive")
    @ResponseBody
    public String receive(HttpServletRequest request, Object response, String id) {
        // 测试参数是否可以获取
        System.out.println("method:" + request.getMethod());

        Map<String, WebSocketSession> sessionMap = WsHandler.getClients();
        WebSocketSession webSocketSession = sessionMap.get(id);
        if (webSocketSession == null) {
            return "1";
        }
        try {
            ServletInputStream inputStream = request.getInputStream();
            int len = -1;
            while ((len = inputStream.available()) != -1) {

                byte[] data = new byte[len];
                inputStream.read(data);
                // 推送数据给websocket
                wsHandler.sendOneVideo(data, webSocketSession);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据接受转发出错");
        }
        System.out.println("over");
        return "1";
    }

    /**
     * 关闭websoket
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/closeWebsoket")
    @ResponseBody
    public String closeWebsoket(HttpServletRequest request, HttpServletResponse response) {
        // 测试参数是否可以获取
        String session = request.getSession().getId();
        System.out.println("http-session:" + session);
        Map<String, WebSocketSession> map = WsHandler.getClients();
        for (String s : map.keySet()) {
            System.out.println("websoket:" + s);
        }
        System.out.println("over");
        return "1";
    }
}

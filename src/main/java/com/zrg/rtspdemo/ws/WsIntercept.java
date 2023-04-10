package com.zrg.rtspdemo.ws;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class WsIntercept extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String uid = ((ServletServerHttpRequest) request).getServletRequest().getParameter("uid");
        if (StringUtils.isEmpty(uid)) {
            return false;
        } else {
            attributes.put("uid", uid);
            return true;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        HttpServletResponse httpServletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        String header = httpServletRequest.getHeader("sec-websocket-protocol");
        if (StringUtils.isNotEmpty(header)) {
            httpServletResponse.addHeader("sec-websocket-protocol", header);
        }
        super.afterHandshake(request, response, wsHandler, ex);
    }
}

package edu.example.kafkatest.web.socket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

import static edu.example.kafkatest.web.security.SecurityConstants.JWT_COOKIE_NAME;

public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    /**
     * Retrieves token from headers and puts it into session's attributes
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return If connection should be established
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest) {
            var headers = request.getHeaders();
            String token = headers.getFirst(JWT_COOKIE_NAME);
            if (token == null)
                return false;
            attributes.put(JWT_COOKIE_NAME, token);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }

}

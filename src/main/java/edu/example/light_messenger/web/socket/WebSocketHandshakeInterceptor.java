package edu.example.light_messenger.web.socket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

import static edu.example.light_messenger.web.security.SecurityConstants.JWT_COOKIE_NAME;

public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    /**
     * Retrieves token from headers and puts it into session's attributes
     * @return If connection should be established
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest) {
            var headers = request.getHeaders();
            String token = headers.getFirst("Cookie");
            if (token != null) {  // Searching for 'jwtToken=' cookie
                token = token.replaceFirst(".*jwtToken=","");
                if (token.indexOf(' ') > 0)
                    token = token.substring(0, token.indexOf(' '));
            } else {
                return false;
            }
            attributes.put(JWT_COOKIE_NAME, token);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }

}

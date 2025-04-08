package kr.co.anabada.chat.config;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.anabada.jwt.JwtTokenHelper;
import kr.co.anabada.jwt.JwtUtil;
import kr.co.anabada.jwt.UserTokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();

            // 쿼리 파라미터에서 token 직접 추출
            String token = httpServletRequest.getParameter("token");
            System.out.println("[beforeHandshake] token from param: " + token);

            if (token != null && !token.isBlank()) {
                // 토큰으로 유저 정보 추출
                UserTokenInfo userTokenInfo = jwtTokenHelper.extractUserInfoFromAccessToken(token);
                System.out.println("[beforeHandshake] userTokenInfo: " + userTokenInfo);

                if (userTokenInfo != null && userTokenInfo.getUserNo() != null) {
                    // URI에서 roomNo 추출
                    String uri = request.getURI().toString();
                    String roomNo = uri.substring(uri.lastIndexOf("/") + 1).split("\\?")[0];

                    attributes.put("roomNo", roomNo);
                    attributes.put("userNo", userTokenInfo.getUserNo());
                    return true;
                }
            }
        }

        System.out.println("[beforeHandshake] 연결 거부됨");
        return false;
    }




    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
        // 핸드셰이크 이후 추가 작업 없음
    }
}

package kr.co.anabada.chat.config;

import kr.co.anabada.jwt.JwtTokenHelper;
import kr.co.anabada.jwt.JwtUtil;
import kr.co.anabada.jwt.UserTokenInfo;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import java.util.Map;

public class ChatWebSocketInterceptor implements HandshakeInterceptor {
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request,
	                               ServerHttpResponse response,
	                               WebSocketHandler wsHandler,
	                               Map<String, Object> attributes) throws Exception {

	    if (request instanceof ServletServerHttpRequest servletRequest) {
	        var httpServletRequest = servletRequest.getServletRequest();

	        // 쿼리 파라미터에서 token 직접 추출
	        String token = httpServletRequest.getParameter("token");
	        System.out.println("[beforeHandshake] token: " + token);


	        UserTokenInfo userInfo = jwtTokenHelper.extractUserInfoFromAccessToken(token);
	        System.out.println("[beforeHandshake] userTokenInfo: " + userInfo);

	        if (userInfo == null) {
	            System.out.println("[beforeHandshake] 연결 거부됨");
	            return false;
	        }

	        Integer userNo = userInfo.getUserNo();
	        String roomNo = httpServletRequest.getRequestURI().split("/ws/chat/")[1];

	        attributes.put("roomNo", roomNo);
	        attributes.put("userNo", userNo);

	        System.out.println("[beforeHandshake] WebSocket 연결 - userNo: " + userNo + ", roomNo: " + roomNo);
	        return true;
	    }

	    return false;
	}


    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}

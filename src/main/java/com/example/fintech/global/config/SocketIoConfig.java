package com.example.fintech.global.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.fintech.domain.parentChild.repository.ParentChildRepository;
import com.example.fintech.global.security.jwt.CustomJwtUtil;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import com.corundumstudio.socketio.Configuration;

@org.springframework.context.annotation.Configuration
public class SocketIoConfig {
    private final CustomJwtUtil CustomJwtUtil;
    private final ParentChildRepository parentChildRepository;

    public SocketIoConfig(com.example.fintech.global.security.jwt.CustomJwtUtil customJwtUtil, ParentChildRepository parentChildRepository) {
        CustomJwtUtil = customJwtUtil;
        this.parentChildRepository = parentChildRepository;
    }

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);
        return new SocketIOServer(config);
    }

    @Bean
    public ApplicationRunner runner(SocketIOServer server) {
        return args -> {
            server.start();
            System.out.println("✅ Socket.IO 서버 시작됨");

            server.addConnectListener(client -> {
                String token = client.getHandshakeData().getSingleUrlParam("token");

                if (token == null || token.isEmpty()) {
                    client.disconnect();
                    return;
                }

                try {
                    // 1️⃣ 토큰에서 userId, role 추출
                    Long userId = CustomJwtUtil.getUserId(token);
                    String role = CustomJwtUtil.getRole(token); // "PARENT" or "CHILD"

                    String roomId;

                    // 2️⃣ 역할에 따라 입장할 roomId 결정
                    if ("PARENT".equals(role)) {
                        roomId = parentChildRepository.findByParentId(userId)
                                .map(pc -> pc.getChild().getId().toString())
                                .orElseThrow(() -> new RuntimeException("자녀 없음"));
                    } else if ("CHILD".equals(role)) {
                        roomId = userId.toString();
                    } else {
                        throw new RuntimeException("알 수 없는 역할");
                    }

                    // 3️⃣ 클라이언트에 유저 정보 저장 + 방 입장
                    client.set("userId", userId);
                    client.set("roomId", roomId);
                    client.joinRoom(roomId);

                    System.out.println("✅ 유저 " + userId + " → 방 " + roomId + " 입장 완료");

                } catch (Exception e) {
                    System.out.println("❌ 인증 실패: " + e.getMessage());
                    client.disconnect();
                }
            });
        };
    }
}

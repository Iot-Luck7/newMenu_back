package com.iot7.controller;

import com.iot7.dto.PushTokenRequest;
import com.iot7.entity.User;
import com.iot7.repository.PushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor // 생성자 자동으로 주입해줌
@RequestMapping("/api/push-token")
public class PushTokenController {

    private final PushTokenRepository pushTokenRepository;

    // ✅ 푸시 토큰 저장 API (프론트에서 토큰 발급 후 이 API로 전송)
    @PostMapping
    public ResponseEntity<String> savePushToken(@RequestBody PushTokenRequest request) {
        // userId로 DB에서 유저 조회
        Optional<User> optionalUser = pushTokenRepository.findById(request.getUserId());

            // 유저가 존재할 경우
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // ✅ 같은 푸시 토큰을 가진 다른 유저 찾아서 null 처리 (휴대폰 하나로 여러 계정
            List<User> sameTokenUsers = pushTokenRepository.findByPushToken(request.getPushToken());
            for (User u : sameTokenUsers) {
                if (!u.getUserId().equals(user.getUserId())) {
                    u.setPushToken(null);
                    pushTokenRepository.save(u);
                }
            }

            // ✅ 기존 토큰과 다른 경우에만 저장
            if (!request.getPushToken().equals(user.getPushToken())) {
                user.setPushToken(request.getPushToken());
                pushTokenRepository.save(user);
                return ResponseEntity.ok("✅ 푸시 토큰 저장 완료 (변경됨)");
            } else {
                return ResponseEntity.ok("⚠️ 기존 토큰과 동일 – 저장 생략");
            }

        } else {
            return ResponseEntity.badRequest().body("존재하지 않는 사용자입니다.");
        }
    }

    // ✅ 푸시 토큰 조회 API (프론트에서 사용)
    @GetMapping
    public ResponseEntity<Map<String, String>> getPushToken(@RequestParam String userId) {
        Optional<User> optionalUser = pushTokenRepository.findById(userId);
        if (optionalUser.isEmpty()) return ResponseEntity.ok(Collections.emptyMap());

        User user = optionalUser.get();

        Map<String, String> res = new HashMap<>();
        res.put("token", user.getPushToken());
        res.put("notificationYn", user.getNotificationYn());

        return ResponseEntity.ok(res);
    }

    @PatchMapping("/toggle")
    public ResponseEntity<String> toggleNotification(
            @RequestParam String userId,
            @RequestParam boolean enabled
    ) {
        Optional<User> optionalUser = pushTokenRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("유저 없음");
        }

        User user = optionalUser.get();
        user.setNotificationYn(enabled ? "Y" : "N");
        pushTokenRepository.save(user);

        return ResponseEntity.ok("알림 " + (enabled ? "ON" : "OFF") + " 설정 완료");
    }


}

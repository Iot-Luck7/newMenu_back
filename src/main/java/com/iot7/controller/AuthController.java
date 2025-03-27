package com.iot7.controller;

import com.iot7.dto.UserSignupRequest;
import com.iot7.entity.User;
import com.iot7.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 🔐회원가입 API > 콘솔 상시 확인 > 안나올경우 안되는 코드.
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserSignupRequest request) {
        System.out.println("✅ [백엔드] 회원가입 API 호출됨!"); // ← 로그 찍기
        try {
            User user = authService.registerUser(request); // uid 기반으로 저장
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> body) {
        try {
            String token = body.get("token"); // 프론트에서 넘겨준 토큰 꺼내기
            User user = authService.authenticateUser(token); // 서비스로 위임
            return ResponseEntity.ok(user); // 유저 정보 반환
        } catch (Exception e) {
            return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
        }
    }
}

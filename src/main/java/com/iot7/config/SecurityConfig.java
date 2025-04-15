package com.iot7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

//요청을 검사하고, 진짜 사용자냐 판단하는 보안 역할의 파일
//열고 들어온 사람, 진짜 주인인지 확인 하는 파일


@Configuration //설정용 파일
@EnableWebSecurity //스프링 시큐리티 활성화
public class SecurityConfig {

    @Bean
    public FirebaseAuthenticationFilter firebaseAuthenticationFilter() { // 토큰 검사 필터
      return new FirebaseAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // HttpSecurity라는 객체를 통해 보안 규칙을 설정
        http
                .cors(CorsConfigurer::disable)
                .cors(withDefaults())
                .formLogin(AbstractHttpConfigurer::disable) // ✅ 기본 로그인 폼 비활성화(우리는 리엑트 네이티브에서 로그인화면 만들었으니)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // ✅ 모든 요청 허용
                        .anyRequest().authenticated()
                        //.anyRequest().authenticated() 인증 활성화 코드
                )
                .addFilterBefore(firebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}



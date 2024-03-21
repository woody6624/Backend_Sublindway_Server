package SublindWay_server.Config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    // AuthenticationManager를 통해서 인증(Authentication)이라는 타입의 객체로
    // 작업을 하게 됨. 흥미롭게도 매니저가 가진 인증 처리 메서드는 파라미터도 Authentication
    // 으로 받고 리턴 타입 역시 Authentication임.

    // 패스워드 암호화
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Authentication Manager
    @Bean
    public InMemoryUserDetailsManager userDetailsService(){
        UserDetails user= User.builder()
                .username("user1")
                .password(passwordEncoder().encode("1111"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // 인증, 인가에 대한 처리 추가
        // filterChain()은 HttpSecurity라는 API를 활용해서 특정한 경로에 대한
        // 설정을 변경할 수 있음


        http.authorizeHttpRequests((auth)->{
            // requestMatchers를 통해 "/"와 같은 패턴을 이용해서 원하는 자원 선택 o
            // permitAll()은 말 그대로 모든 사용자에게 허락 -> 로그인하지 않아도 접근 o

            auth.requestMatchers("/login","/oauth/**","/").permitAll()
                    .anyRequest().authenticated(); // 다른 요청은 인가 부여받아야함
            auth.requestMatchers("/privatePage").hasRole("ROLE_USER"); // 일반 유저만 접근

        }).formLogin((auth)->{ // 로그인 페이지 설정
                auth.loginPage("/login");
                auth.defaultSuccessUrl("/userPage").permitAll(); // 로그인 성공 시 유저 페이지로 이동
        });


        return http.build();
    }
}

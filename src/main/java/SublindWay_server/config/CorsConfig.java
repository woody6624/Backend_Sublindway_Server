package SublindWay_server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 모든 HTTP 메소드 허용
                .allowedOrigins("http://localhost:3000")  // 구체적인 도메인 명시
                .allowCredentials(true)  // 자격 증명을 포함한 요청 허용
                .maxAge(3600);  // 사전 요청(pre-flight)의 캐시 시간 설정
    }
}
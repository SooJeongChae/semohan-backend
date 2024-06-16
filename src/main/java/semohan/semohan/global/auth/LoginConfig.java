package semohan.semohan.global.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class LoginConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns(
                        "/member/**",
                        "/auth/sign-out",
                        "/menu/pin",
                        "/review/my-reviews",
                        "/review/member/{memberId}",
                        "review/{restaurantId}/{menuId}/write",
                        "review/{id}"
                        // 추가적인 로그인 필요한 패턴들
                );
    }
}
package semohan.semohan.global.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import semohan.semohan.global.exception.CustomException;

import static semohan.semohan.global.exception.ErrorCode.UNAUTHORIZED_USER;

public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestURI = request.getRequestURI();
        System.out.println("[interceptor] : " + requestURI);
        HttpSession session = request.getSession(false);

        // 로그인 X
        if(session == null || session.getAttribute("id") == null) {
            throw new CustomException(UNAUTHORIZED_USER);
        }

        // 로그인 O
        return true;
    }
}

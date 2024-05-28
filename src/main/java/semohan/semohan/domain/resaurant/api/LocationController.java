package semohan.semohan.domain.resaurant.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semohan.semohan.global.exception.CustomException;
import semohan.semohan.global.exception.ErrorCode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationController {
    
    // TODO: exception 처리
    @GetMapping("/set/{region}")
    public ResponseEntity<Boolean> updateLocation(@PathVariable String region, HttpServletResponse response)  {
        Cookie cookie = null;
        try {
            cookie = new Cookie("location", URLEncoder.encode(region,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new CustomException(ErrorCode.ENCODING_ERROR);
        }
        response.addCookie(cookie);

        log.info(region);

        return ResponseEntity.ok(true);
    }

}

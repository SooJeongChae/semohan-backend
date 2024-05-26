package semohan.semohan.domain.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 사용자 연락처를 key로, 인증코드 value
    public void setDataExpire(String key, String value, long duration) {
        stringRedisTemplate.opsForValue().set(key, value, duration, TimeUnit.SECONDS);
    }

    // 사용자 연락처로 인증코드 가져오기
    public String getData(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // 인증이 완료된 정보 삭제
    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }
}
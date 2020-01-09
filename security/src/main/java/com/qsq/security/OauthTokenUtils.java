package com.qsq.security;

import com.qsq.util.JsonUtil;
import com.qsq.util.RedisUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author QSQ
 * @create 2019/10/18 10:55
 * No, again
 * 〈用来保存token信息〉
 */
@Component
public class OauthTokenUtils {

    @Autowired
    private RedisUtils redisUtils;

    public String get(String oldToken) {
        return redisUtils.get(oldToken);
    }

    public void put(TokenInfo tokenInfo) {
        redisUtils.set(tokenInfo.getToken(), tokenInfo.getRefreshToken());
    }

    public void clear(String oldToken) {
        redisUtils.delete(oldToken);
    }


}
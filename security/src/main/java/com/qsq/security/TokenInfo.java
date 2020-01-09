package com.qsq.security;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author QSQ
 * @create 2019/10/18 10:57
 * No, again
 * 〈token信息〉
 */
@Data
@Builder
public class TokenInfo implements Serializable {

    private String token;
    private String refreshToken;
    private int expiresIn;

}
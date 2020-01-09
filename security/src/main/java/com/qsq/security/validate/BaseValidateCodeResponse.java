package com.qsq.security.validate;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author QSQ
 * @create 2019/10/1 9:41
 * No, again
 * 〈验证码的控制层〉
 */
@Data
public class BaseValidateCodeResponse implements Serializable {

    /**
     * 验证码
     */
    private String code;
    /**
     * 有效期时间
     */
    private int expireSeconds;
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;


    public BaseValidateCodeResponse(String code, int expireSeconds) {
        this.code = code;
        this.expireSeconds = expireSeconds;
        this.expireTime = LocalDateTime.now().plusSeconds(expireSeconds);
    }

    public BaseValidateCodeResponse() {
    }

    /**
     * 过期返回true , 没过期返回false
     *
     * @return
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }


}
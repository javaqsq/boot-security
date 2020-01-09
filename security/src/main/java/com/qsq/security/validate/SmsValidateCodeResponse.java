package com.qsq.security.validate;

/**
 * @author QSQ
 * @create 2019/10/1 10:11
 * No, again
 * 〈短信验证码〉
 */
public class SmsValidateCodeResponse extends BaseValidateCodeResponse {

    public SmsValidateCodeResponse() {
        super();
    }

    public SmsValidateCodeResponse(String code, int expireSeconds) {
        super(code, expireSeconds);
    }

}
package com.qsq.exception;

import lombok.Data;
import org.springframework.security.core.AuthenticationException;

/**
 * @author QSQ
 * @create 2019/4/22 15:00
 * No, again
 * 〈〉
 */
@Data
public class LoginSecurityException extends AuthenticationException {

    private static final long serialVersionUID = -6039260641125669163L;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 错误代码
     */
    private Integer errorCode;


    public LoginSecurityException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public LoginSecurityException(String errorMsg, Integer errorCode) {
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }



}
package com.qsq.exception;

import lombok.Data;

/**
 * @author QSQ
 * @create 2019/10/16 20:11
 * No, again
 * 〈业务异常〉
 */
@Data
public class BusinessRuntimeException extends RuntimeException {


    /**
     * 错误代码
     */
    private String errorCode;
    /**
     * 错误信息
     */
    private String errorMsg;


    public BusinessRuntimeException() {
        super();
    }

    public BusinessRuntimeException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

}
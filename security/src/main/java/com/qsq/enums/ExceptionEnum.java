package com.qsq.enums;

import com.qsq.exception.BusinessRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author qsq
 * @date 2019年10月16日20:08:25
 * < 业务异常 >
 */
@Getter
@AllArgsConstructor
public enum ExceptionEnum {


    /**
     * 系统相关
     */
    SYS_TOO_BUSY("1006", "系统异常，请稍后"),

    //跟外部系统有关的
    OUT_SYSTEM_SMS_ERROR("OUT5000", "短信服务商系统异常"),
    /**
     * 验证码相关枚举
     */
    CODE_VALIDATE_NOT_EXPIRED("100001", "验证码还未过期,稍后再试"),
    CODE_CREATE_VALIDATE_ERROR("100002", "生成验证码错误"),
    CODE_SMS_SEND_ERROR("100003", "短信发送失败"),
    CODE_VALIDATE_EXPIRED("100004", "验证码过期"),
    CODE_VALIDATE_NOT_SAME("100005", "验证码不一致"),
    CODE_VALIDATE_NONE("100006", "验证码不存在"),
    CODE_VALIDATE_ERROR_LOGIN("100007", "loginId没有获取到"),
    VALIDATE_TYPE_MISS("100008", "validateType验证码类型不存在"),
    CODE_VALUE_EMPTY("100009", "code值为空"),


    LOGIN_ERROR_CLIENT_INFO_MISS("200001", "请求头中无client信息"),
    LOGIN_ERROR_CLIENT_ID_MISS("200002", "ClientId对应的配置信息不存在"),
    LOGIN_ERROR_CLIENT_NOT_MATCH("200003", "clientSecret不匹配"),

    /**
     * 业务相关
     */
    REQUEST_ID_MISS("300001", "主键缺失"),
    EXIST_SAME_USERNAME("300002", "用户名已被使用"),
    LOGIN_USERNAME_EMPTY("300003", "用户名不能为空"),
    LOGIN_USER_NOT_EXIST("300004", "用户不存在"),
    LOGIN_PASSWORD_ERROR("300005", "密码错误"),;
    private String code;
    private String value;

    public BusinessRuntimeException getException() {
        return new BusinessRuntimeException(this.getCode(), this.getValue());
    }

}

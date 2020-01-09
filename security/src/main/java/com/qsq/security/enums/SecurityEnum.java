package com.qsq.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author qsq
 * security相关
 */
public interface SecurityEnum {

    @Getter
    @AllArgsConstructor
    enum LoginType {
        /**
         * 登录类型
         */
        MOBILE("mobile", "手机短信登录"),
        PWD("pwd", "用户名密码登录");

        private String type;
        private String description;

        public static LoginType instance(String loginType) {
            for (LoginType type : LoginType.values()) {
                if (StringUtils.equals(type.getType(), loginType)) {
                    return type;
                }
            }
            return null;
        }

    }

    @Getter
    @AllArgsConstructor
    enum ValidateType {
        /**
         * 验证类型
         */
        SMS("sms", "smsValidateCodeGenerate", "短信验证码"),
        IMAGE("image", "imageValidateCodeGenerate", "图片验证码"),;
        private String type;
        private String validateCodeGenerateKey;
        private String description;

        public static ValidateType instance(String type) {

            for (ValidateType validateType : ValidateType.values()) {
                if (StringUtils.equals(validateType.getType(), type)) {
                    return validateType;
                }
            }
            return ValidateType.IMAGE;
        }

    }


}

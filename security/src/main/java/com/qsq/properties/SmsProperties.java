package com.qsq.properties;


import lombok.Data;

/**
 * @author QSQ
 * @create 2019/10/16 14:50
 * No, again
 * 〈短信验证码配置文件〉
 */
@Data
public class SmsProperties extends SecurityProperties {

    private String savePrefix;
    private String expireTime;
    private String defaultCode;

}
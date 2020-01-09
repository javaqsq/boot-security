package com.qsq.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author QSQ
 * @create 2019/10/16 11:21
 * No, again
 * 〈security相关的配置〉
 */

@Data
@Component
@PropertySource(value = {"classpath:security.properties"})
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private String loginDefaultUrl;
    private String mobileLoginUrl;

    private ImageProperties image;
    private SmsProperties sms;


}
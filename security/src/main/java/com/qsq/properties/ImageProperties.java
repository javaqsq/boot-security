package com.qsq.properties;

import lombok.Data;

/**
 * @author QSQ
 * @create 2019/10/16 14:49
 * No, again
 * 〈图片验证码配置文件〉
 */
@Data
public class ImageProperties extends SecurityProperties {

    private String savePrefix;
    private String expireTime;
    private String defaultCode;

}
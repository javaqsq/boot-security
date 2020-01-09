package com.qsq.security.validate;

import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * @author QSQ
 * @create 2019/10/1 10:07
 * No, again
 * 〈 图片验证码  图片验证码需要返回一个图片给前端 〉
 */
@Data
public class ImageValidateCodeResponse extends BaseValidateCodeResponse {

    private BufferedImage image;

    public ImageValidateCodeResponse(BufferedImage image, String code, int seconds) {
        super(code, seconds);
        this.image = image;
    }

    public ImageValidateCodeResponse() {
    }

}
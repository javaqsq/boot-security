package com.qsq.security.validate;


import com.qsq.constant.EnvironmentConstant;
import com.qsq.enums.ExceptionEnum;
import com.qsq.properties.SecurityProperties;
import com.qsq.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author QSQ
 * @create 2019/10/1 17:50
 * No, again
 * 〈图片生成器〉
 */
@Component
public class ImageValidateCodeGenerate implements ValidateCodeGenerate<ImageValidateCodeResponse> {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SecurityProperties securityProperties;

    @Value("${environment}")
    private String environment;

    /**
     * 验证码生成器
     *
     * @return
     */
    @Override
    public ImageValidateCodeResponse createCode(String loginId) {
        return createImage();
    }

    /**
     * 发送验证码
     *
     * @param imageValidateCodeResponse
     * @param loginId
     */
    @Override
    public void saveCode(ImageValidateCodeResponse imageValidateCodeResponse, String loginId) {
        redisUtils.set(securityProperties.getImage().getSavePrefix() + loginId,
                imageValidateCodeResponse.getCode(), imageValidateCodeResponse.getExpireSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 发送验证码
     *
     * @param response
     * @param loginId
     */
    @Override
    public void send(HttpServletResponse response, String loginId) {
        try {
            ImageValidateCodeResponse code = createCode(loginId);
            saveCode(code, loginId);
            ImageIO.write(code.getImage(), "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw ExceptionEnum.CODE_CREATE_VALIDATE_ERROR.getException();
        }
    }

    /**
     * 验证图片是否过期
     *
     * @param loginId
     * @return 返回true 则是过期 , false 则是有效
     */
    @Override
    public boolean codeExpired(String loginId) {
        String image = redisUtils.get(securityProperties.getImage().getSavePrefix() + loginId);
        return StringUtils.isEmpty(image);
    }

    /**
     * 清除验证码
     *
     * @param loginId 用户的唯一标识
     */
    @Override
    public void clearCode(String loginId) {
        redisUtils.delete(securityProperties.getImage().getSavePrefix() + loginId);
    }

    /**
     * 根据类型去校验是否过期
     *
     * @param loginId
     * @param code
     * @return
     */
    @Override
    public void validateByType(String loginId, String code) {
        if (StringUtils.isEmpty(loginId)) {
            throw ExceptionEnum.CODE_VALIDATE_ERROR_LOGIN.getException();
        }
        boolean codeExpired = codeExpired(loginId);
        boolean flag = EnvironmentConstant.isDevEnv(environment) && Objects.equals(code, securityProperties.getImage().getDefaultCode());
        //如果是开发环境不需要验证
        if (flag) {
            return;
        }
        if (codeExpired) {
            throw ExceptionEnum.CODE_VALIDATE_EXPIRED.getException();
        }
        String saveCode = redisUtils.get(securityProperties.getImage().getSavePrefix() + loginId);
        if (!Objects.equals(saveCode, code)) {
            throw ExceptionEnum.CODE_VALIDATE_NOT_SAME.getException();
        }
    }





    /* =================================================== 生成图片的工具 ====================================================*/


    /**
     * 生成图片
     *
     * @return
     */
    private ImageValidateCodeResponse createImage() {
        int width = 60;
        int height = 20;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Random random = new Random();
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        //随机数
        String code = "";
        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(random.nextInt(10));
            code += rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }
        g.dispose();
        return new ImageValidateCodeResponse(image, code, Integer.parseInt(securityProperties.getImage().getExpireTime()));
    }

    /**
     * 生成随机背景条纹
     *
     * @param fc
     * @param bc
     * @return
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}



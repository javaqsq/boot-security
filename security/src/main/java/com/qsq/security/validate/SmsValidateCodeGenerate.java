package com.qsq.security.validate;

import com.qsq.common.alisms.AliSendSmsResponse;
import com.qsq.common.alisms.AliTemplateParams;
import com.qsq.common.alisms.AliYunSmsUtil;
import com.qsq.constant.EnvironmentConstant;
import com.qsq.enums.ExceptionEnum;
import com.qsq.properties.SecurityProperties;
import com.qsq.util.IDGenerator;
import com.qsq.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author QSQ
 * @create 2019/10/1 21:19
 * No, again
 * 〈短信验证码生成器〉
 */
@Component
public class SmsValidateCodeGenerate implements ValidateCodeGenerate<SmsValidateCodeResponse> {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SecurityProperties securityProperties;


    @Value("${environment}")
    private String environment;

    /**
     * 验证码生成器
     *
     * @param loginId
     * @return
     */
    @Override
    public SmsValidateCodeResponse createCode(String loginId) {
        boolean codeExpired = codeExpired(loginId);
        if (codeExpired) {
            String code = IDGenerator.getNumUUID(4);
            AliSendSmsResponse sendSmsResponse = AliYunSmsUtil.sendSmsCode(loginId, new AliTemplateParams(code));
            if (sendSmsResponse.isSuccess()) {
                return new SmsValidateCodeResponse(code, Integer.parseInt(securityProperties.getSms().getExpireTime()));
            } else {
                throw ExceptionEnum.CODE_SMS_SEND_ERROR.getException();
            }
        } else {
            throw ExceptionEnum.CODE_VALIDATE_NOT_EXPIRED.getException();
        }
    }

    /**
     * 保存验证码
     *
     * @param smsValidateCodeResponse
     * @param loginId                 用户的唯一标识
     */
    @Override
    public void saveCode(SmsValidateCodeResponse smsValidateCodeResponse, String loginId) {
        redisUtils.set(securityProperties.getSms().getSavePrefix() + loginId,
                smsValidateCodeResponse.getCode(), smsValidateCodeResponse.getExpireSeconds(), TimeUnit.SECONDS);
    }

    /**
     * 发送验证码
     *
     * @param response
     * @param loginId  用户的唯一标识
     */
    @Override
    public void send(HttpServletResponse response, String loginId) {
        SmsValidateCodeResponse smsValidateCodeResponse = createCode(loginId);
        saveCode(smsValidateCodeResponse, loginId);
    }

    /**
     * 验证验证码是否过期
     *
     * @param loginId 用户的唯一标识
     * @return
     */
    @Override
    public boolean codeExpired(String loginId) {
        String image = redisUtils.get(securityProperties.getSms().getSavePrefix() + loginId);
        return StringUtils.isEmpty(image);
    }

    /**
     * 清除验证码
     *
     * @param loginId 用户的唯一标识
     */
    @Override
    public void clearCode(String loginId) {
        redisUtils.delete(securityProperties.getSms().getSavePrefix() + loginId);
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
        boolean codeExpired = codeExpired(loginId);
        boolean flag = EnvironmentConstant.isDevEnv(environment) && Objects.equals(code, securityProperties.getSms().getDefaultCode());
        //如果是开发环境不需要验证
        if (flag) {
            return;
        }
        if (codeExpired) {
            throw ExceptionEnum.CODE_VALIDATE_EXPIRED.getException();
        }
        String saveCode = redisUtils.get(securityProperties.getSms().getSavePrefix() + loginId);
        if (!Objects.equals(saveCode, code)) {
            throw ExceptionEnum.CODE_VALIDATE_NOT_SAME.getException();
        }
    }
}
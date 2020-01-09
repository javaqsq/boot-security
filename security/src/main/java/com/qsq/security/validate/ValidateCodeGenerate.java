package com.qsq.security.validate;

import javax.servlet.http.HttpServletResponse;

/**
 * @author QSQ
 * @create 2019/10/1 17:03
 * No, again
 * 〈验证码生成器〉
 */
public interface ValidateCodeGenerate<T extends BaseValidateCodeResponse> {


    /**
     * 验证码生成器
     *
     * @param loginId
     * @return
     */
    T createCode(String loginId);

    /**
     * 保存验证码
     *
     * @param t
     * @param loginId 用户的唯一标识
     */
    void saveCode(T t, String loginId);

    /**
     * 发送验证码
     *
     * @param response
     * @param loginId  用户的唯一标识
     */
    void send(HttpServletResponse response, String loginId);

    /**
     * 验证验证码是否过期
     *
     * @param loginId 用户的唯一标识
     * @return
     */
    boolean codeExpired(String loginId);

    /**
     * 清除验证码
     *
     * @param loginId 用户的唯一标识
     */
    void clearCode(String loginId);

    /**
     * 根据类型去校验是否过期
     *
     * @param loginId
     * @param code
     * @return
     */
    void validateByType(String loginId, String code);

}

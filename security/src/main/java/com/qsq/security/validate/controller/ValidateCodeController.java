package com.qsq.security.validate.controller;

import com.qsq.model.ResultResponse;
import com.qsq.security.enums.SecurityEnum;
import com.qsq.security.validate.ValidateCodeGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author QSQ
 * @create 2019/10/17 20:43
 * No, again
 * 〈验证码获取〉
 */
@RestController
@RequestMapping("/code")
public class ValidateCodeController {

    @Autowired
    private Map<String, ValidateCodeGenerate> validateCodeGenerateMap = new HashMap<>(2);

    /**
     * 验证码生成器
     *
     * @param validateType 验证码类型
     * @param loginId  用户唯一ID
     * @param response
     */
    @GetMapping("/login/{validateType}/{loginId}")
    public ResultResponse sendCode(@PathVariable("validateType") String validateType, @PathVariable("loginId") String loginId, HttpServletResponse response) {
        ValidateCodeGenerate validateCodeGenerate = validateCodeGenerateMap.get(SecurityEnum.ValidateType.instance(validateType).getValidateCodeGenerateKey());
        validateCodeGenerate.send(response, loginId);
        return ResultResponse.success("200", "验证码发送成功");
    }


}
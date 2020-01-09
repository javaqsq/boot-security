package com.qsq.security;

import com.qsq.enums.ExceptionEnum;
import com.qsq.exception.BusinessRuntimeException;
import com.qsq.exception.LoginSecurityException;
import com.qsq.properties.SecurityProperties;
import com.qsq.security.constant.LoginParamsConstant;
import com.qsq.security.enums.SecurityEnum;
import com.qsq.security.handle.CustomLoginAuthFailureHandler;
import com.qsq.security.validate.ValidateCodeGenerate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author QSQ
 * @create 2019/10/17 22:38
 * No, again
 * 〈登录用的验证码过滤器〉
 */
@Component
@Slf4j
public class LoginValidateFilter extends OncePerRequestFilter {

    @Autowired
    private CustomLoginAuthFailureHandler customLoginAuthFailureHandler;

    @Autowired
    private Map<String, ValidateCodeGenerate> validateCodeGenerateMap = new HashMap<>(2);

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 只过滤登录请求
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.contains(securityProperties.getMobileLoginUrl()) || requestURI.contains(securityProperties.getLoginDefaultUrl())) {
            try {
                String validateType = request.getParameter(LoginParamsConstant.VALIDATE_TYPE_PARAMS);
                if (StringUtils.isEmpty(validateType)) {
                    throw ExceptionEnum.VALIDATE_TYPE_MISS.getException();
                }
                String loginId = "";
                if (requestURI.contains(securityProperties.getMobileLoginUrl())) {
                    loginId = request.getParameter(LoginParamsConstant.MOBILE_LOGIN_PARAMS);
                } else {
                    loginId = request.getParameter("username");
                }
                String code = request.getParameter(LoginParamsConstant.VALIDATE_CODE_PARAMS);
                if (StringUtils.isEmpty(code)) {
                    throw ExceptionEnum.CODE_VALUE_EMPTY.getException();
                }
                ValidateCodeGenerate validateCodeGenerate = validateCodeGenerateMap.get(SecurityEnum.ValidateType.instance(validateType).getValidateCodeGenerateKey());
                validateCodeGenerate.validateByType(loginId, code);
                validateCodeGenerate.clearCode(loginId);
            } catch (BusinessRuntimeException e) {
                customLoginAuthFailureHandler.onAuthenticationFailure(request, response, new LoginSecurityException(e.getErrorMsg()));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
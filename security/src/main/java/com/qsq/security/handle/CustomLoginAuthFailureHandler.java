package com.qsq.security.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qsq.enums.ResultEnum;
import com.qsq.model.ResultResponse;
import com.qsq.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author QSQ
 * @create 2019/4/23 10:51
 * No, again
 * 〈认证(登录)失败的逻辑处理〉
 * 需要处理的逻辑
 */
@Component
@Slf4j
public class CustomLoginAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper = JsonUtil.getObjectMapper();


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("登录失败");
        ResultResponse fail = ResultResponse.fail(ResultEnum.OPERATE_FAIL.getCode(), exception.getMessage());
        log.error(exception.toString());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(fail));
    }
}
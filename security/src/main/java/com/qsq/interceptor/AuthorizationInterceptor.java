package com.qsq.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qsq.model.ResultResponse;
import com.qsq.util.JsonUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author QSQ
 * @create 2019/4/12 23:46
 * No, again
 * 〈〉
 */
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final static String ERROR_URI = "/error";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.endsWith(ERROR_URI)) {
            ResultResponse resultResponse = ResultResponse.fail("404", "无效访问地址");
            response.setContentType("application/json;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.print(JsonUtil.getObjectMapper().writeValueAsString(resultResponse));
            writer.flush();
            writer.close();
            return false;
        }
        return true;
    }

}
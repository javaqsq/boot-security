package com.qsq.security.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qsq.enums.ResultEnum;
import com.qsq.model.ResultResponse;
import com.qsq.security.OauthTokenUtils;
import com.qsq.security.TokenInfo;
import com.qsq.util.HttpClientUtil;
import com.qsq.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 关于oauth的异常捕获
 *
 * @author qsq
 * @date 2019年10月17日19:22:21
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper = JsonUtil.getObjectMapper();

    @Autowired
    private OauthTokenUtils oauthTokenUtils;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        String message = StringUtils.split(authException.getMessage(), ":")[0];
        switch (message) {
            case ("Invalid access token"):
                responseWriter(response, HttpStatus.UNAUTHORIZED.value(), "登陆已失效");
                break;
            case ("Access token expired"):
//                String oauthServiceUrl = "http://localhost:8080/oauth/token";
//                String oldToken = StringUtils.split(authException.getMessage(), ":")[1].trim();
//                String tokenRefresh = oauthTokenUtils.get(oldToken);
//                // 这边可以手动刷新token
//                HttpHeaders httpHeaders = new HttpHeaders();
//                httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//                httpHeaders.setBasicAuth("qsq", "secret");
//                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//                params.add("grant_type", "refresh_token");
//                params.add("refresh_token", tokenRefresh);
//                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
//                ResponseEntity<Map> responseEntity = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity,Map.class);
//                System.out.println(responseEntity);
//                System.out.println(responseEntity.getBody());
//                oauthTokenUtils.clear(oldToken);

                responseWriter(response, HttpStatus.UNAUTHORIZED.value(), "登陆已过期");
                break;
            case ("Full authentication is required to access this resource"):
                responseWriter(response, HttpStatus.UNAUTHORIZED.value(), "header Authorization 属性出错");
                break;
            default:
                responseWriter(response, HttpStatus.UNAUTHORIZED.value(), message);
        }
    }

    private void responseWriter(HttpServletResponse response, int status, String message) throws IOException {
        ResultResponse fail = ResultResponse.fail(ResultEnum.OPERATE_FAIL.getCode(), message);
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(fail));

    }


}

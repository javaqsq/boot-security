package com.qsq.security.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.qsq.enums.ExceptionEnum;
import com.qsq.enums.ResultEnum;
import com.qsq.exception.BusinessRuntimeException;
import com.qsq.model.ResultResponse;
import com.qsq.properties.SecurityProperties;
import com.qsq.security.OauthTokenUtils;
import com.qsq.security.TokenInfo;
import com.qsq.util.JsonUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author QSQ
 * @create 2019/10/3 22:17
 * No, again
 * 〈登录成功后的处理〉
 */
@Component
@Slf4j
public class CustomLoginAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String BASIC_ = "Basic ";

    @Autowired
    private ObjectMapper objectMapper = JsonUtil.getObjectMapper();

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private OauthTokenUtils oauthTokenUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        //    把用户信息保存在线程中
        User principal = (User) authentication.getPrincipal();
        try {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith(BASIC_)) {
                throw ExceptionEnum.LOGIN_ERROR_CLIENT_INFO_MISS.getException();
            }
            String[] tokens = extractAndDecodeHeader(header, request);
            assert tokens.length == 2;
            String clientId = tokens[0];
            String clientSecret = tokens[1];
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
            if (clientDetails == null) {
                throw ExceptionEnum.LOGIN_ERROR_CLIENT_ID_MISS.getException();
            } else if (!bCryptPasswordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
                throw ExceptionEnum.LOGIN_ERROR_CLIENT_NOT_MATCH.getException();
            }

            //自己构建一个token返回前端 , 保持后期可以用token进行会话
            TokenRequest tokenRequest = new TokenRequest(Maps.newHashMap(), clientId, clientDetails.getScope(), "mobile");
            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
            OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

            log.info("用户 {} 通过 {} 登录成功 ! 获取token {} 信息", principal.getUsername(), request.getRequestURI().contains(securityProperties.getMobileLoginUrl()) ? "手机方式" : "用户名密码", token.toString());
            converterResponse(response, HttpStatus.OK.value(), ResultResponse.returnByEnum(ResultEnum.OPERATE_SUCCESS, token));
        } catch (BusinessRuntimeException e) {
            log.error(" businessRuntimeException error " + e);
            converterResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), ResultResponse.fail(ResultEnum.OPERATE_FAIL.getCode(), e.getErrorMsg()));
        } catch (Exception e) {
            log.error(" system error " + e);
            converterResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), ResultResponse.fail(ResultEnum.OPERATE_FAIL.getCode(), e.getMessage()));
        }


    }

    private void converterResponse(HttpServletResponse response, int status, ResultResponse resultResponse) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(resultResponse));
    }


    private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {
        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64Utils.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }
        String token = new String(decoded, "UTF-8");
        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }


    public static void main(String[] args) throws Exception {
        String token = "\n" +
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzA5MzAxNDQsInVzZXJfbmFtZSI6ImFkbWluIiwianRpIjoiMTljYjQ2MzYtYjYxZC00NGQ0LTg5YzItMTIzNjIwZmNmOGM1IiwiY2xpZW50X2lkIjoicXNxIiwic2NvcGUiOlsiYWxsIl19.GGvTFKj16h4KvzwViDQfUhUZM49G-rT6OGTdQRYtp8o";

        Claims body = Jwts.parser().setSigningKey("sss".getBytes("UTF-8")).parseClaimsJws(token).getBody();
        System.out.println(body);

    }


}
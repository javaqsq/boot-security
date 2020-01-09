package com.qsq.security.mobile.filter;

import com.qsq.security.constant.LoginParamsConstant;
import com.qsq.security.mobile.MobileAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author QSQ
 * @create 2019/10/3 22:35
 * No, again
 * 〈 拦截 手机号码登录方式〉
 */
public class MobileLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private String mobileParameter = LoginParamsConstant.MOBILE_LOGIN_PARAMS;
    private boolean postOnly = true;


    public MobileLoginAuthenticationFilter() {
        super(new AntPathRequestMatcher("/mobile/login", "POST"));
    }


    /**
     * AbstractAuthenticationProcessingFilter 在过滤器之内调用这个方法进行尝试认证 , 然后调用getAuthenticationManager()进行认证
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String mobile = obtainMobile(request);

        if (mobile == null) {
            mobile = "";
        }

        mobile = mobile.trim();

        MobileAuthenticationToken authRequest = new MobileAuthenticationToken(mobile);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }


    /**
     * 获取手机号
     */
    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }


    protected void setDetails(HttpServletRequest request, MobileAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }


    public void setMobileParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.mobileParameter = usernameParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return mobileParameter;
    }

}
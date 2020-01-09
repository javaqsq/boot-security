package com.qsq.security.mobile.provider;

import com.qsq.exception.BusinessRuntimeException;
import com.qsq.security.mobile.MobileAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author QSQ
 * @create 2019/10/3 22:45
 * No, again
 * 〈  手机登录用的provider 获取认证 〉
 */
public class MobileLoginAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsServiceImpl;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            MobileAuthenticationToken token = (MobileAuthenticationToken) authentication;
            UserDetails user = userDetailsServiceImpl.loadUserByUsername((String) token.getPrincipal());
            if (user == null) {
                throw new InternalAuthenticationServiceException("无法获取用户信息");
            }
            MobileAuthenticationToken authenticationResult = new MobileAuthenticationToken(user, user.getAuthorities());
            authenticationResult.setDetails(token.getDetails());
            return authenticationResult;
        } catch (BusinessRuntimeException e) {
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsServiceImpl;
    }

    public void setUserDetailsService(UserDetailsService userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

}
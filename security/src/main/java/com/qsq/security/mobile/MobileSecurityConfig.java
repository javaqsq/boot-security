package com.qsq.security.mobile;

import com.qsq.security.mobile.filter.MobileLoginAuthenticationFilter;
import com.qsq.security.mobile.provider.MobileLoginAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author QSQ
 * @create 2019/10/3 22:48
 * No, again
 * 〈手机的配置文件〉
 */
@Component
public class MobileSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {


    @Autowired
    private UserDetailsService userDetailsServiceImpl;

    @Autowired
    private AuthenticationFailureHandler customLoginAuthFailureHandler;

    @Autowired
    private AuthenticationSuccessHandler customLoginAuthSuccessHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        MobileLoginAuthenticationFilter mobileLoginAuthenticationFilter = new MobileLoginAuthenticationFilter();
        mobileLoginAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        mobileLoginAuthenticationFilter.setAuthenticationSuccessHandler(customLoginAuthSuccessHandler);
        mobileLoginAuthenticationFilter.setAuthenticationFailureHandler(customLoginAuthFailureHandler);
        MobileLoginAuthenticationProvider smsCodeAuthenticationProvider = new MobileLoginAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        http.authenticationProvider(smsCodeAuthenticationProvider)
                .addFilterAfter(mobileLoginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }

}
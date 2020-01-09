package com.qsq.security.oauth;

import com.qsq.properties.SecurityProperties;
import com.qsq.security.LoginValidateFilter;
import com.qsq.security.handle.CustomLoginAuthFailureHandler;
import com.qsq.security.handle.CustomLoginAuthSuccessHandler;
import com.qsq.security.mobile.MobileSecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * @author QSQ
 * @create 2019/10/16 22:54
 * No, again
 * 〈资源服务器   加入oauth后  认证的逻辑都要在这边  〉
 */
@Configuration
@EnableResourceServer
@Slf4j
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private CustomLoginAuthSuccessHandler customLoginAuthSuccessHandler;

    @Autowired
    private CustomLoginAuthFailureHandler customLoginAuthFailureHandler;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private MobileSecurityConfig mobileSecurityConfig;

    @Autowired
    private AuthenticationEntryPoint myAuthenticationEntryPoint;

    @Autowired
    private LoginValidateFilter loginValidateFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginProcessingUrl(securityProperties.getLoginDefaultUrl())
                .successHandler(customLoginAuthSuccessHandler)
                .failureHandler(customLoginAuthFailureHandler)
                .and()
                .authorizeRequests()
                .antMatchers(securityProperties.getMobileLoginUrl(), "/code/**", "/demo").permitAll()
                //这行必须写,不然会全部都过滤 , 说明上面的地址是可以过滤的, 其他都需要认证
                .anyRequest().authenticated()
                //加入短信登录功能
                .and().apply(mobileSecurityConfig)
                .and().csrf().disable()
                .addFilterBefore(loginValidateFilter, AbstractPreAuthenticatedProcessingFilter.class)
        ;
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.authenticationEntryPoint(myAuthenticationEntryPoint);
    }
}
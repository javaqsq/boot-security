package com.qsq.config;

import com.qsq.interceptor.AuthorizationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author QSQ
 * @create 2019/10/17 20:14
 * No, again
 * 〈〉
 */
@Configuration
public class MyWebConfig implements WebMvcConfigurer {


    @Bean
    public AuthorizationInterceptor authorizationInterceptor() {
        return new AuthorizationInterceptor();
    }

    /**
     * 自定义拦截器 , 需要在这里注册
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor()).addPathPatterns("/**");
    }


    @Bean
    public RestTemplate restTemplate (){
        return new RestTemplate();
    }

}
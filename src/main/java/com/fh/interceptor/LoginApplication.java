package com.fh.interceptor;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Lenovo
 * @title: Application
 * @projectName shop_admin_web
 * @description: TODO
 * @date 2019/12/2423:01
 */
@SpringBootApplication
@EnableCaching
@Configuration
public class LoginApplication implements WebMvcConfigurer {
    public void addInterceptors(InterceptorRegistry registry){
        InterceptorRegistration ir=registry.addInterceptor(new LoginInterceptors());
    }
}

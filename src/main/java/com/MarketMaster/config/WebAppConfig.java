package com.MarketMaster.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.support.OpenSessionInViewInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
//import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.MarketMaster.controller.employee.AuthenticationInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.MarketMaster.controller"})
public class WebAppConfig implements WebMvcConfigurer {

    @Autowired
    private SessionFactory sessionFactory;
    
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
        registry.addResourceHandler("/img/**")
                .addResourceLocations("/img/");
    }

    @Bean
    public OpenSessionInViewInterceptor openSessionInViewInterceptor() {
        OpenSessionInViewInterceptor osivi = new OpenSessionInViewInterceptor();
        osivi.setSessionFactory(sessionFactory);
        return osivi;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/resources/**", "/changePassword");
        
        registry.addInterceptor(new WebRequestHandlerInterceptorAdapter(openSessionInViewInterceptor()))
                .addPathPatterns("/**")
                .excludePathPatterns("/resources/**");
    }
    
//    @Bean
//    public CommonsMultipartResolver multipartResolver() {
//        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//        resolver.setDefaultEncoding("UTF-8");
//        resolver.setMaxUploadSize(5242880); // 5MB
//        return resolver;
//    }
}
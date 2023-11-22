package com.board.configs;

import com.board.commons.interceptors.CommonInterceptor;
import com.board.commons.interceptors.SiteConfigInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaAuditing
@EnableScheduling
@EnableConfigurationProperties(FileUploadConfig.class)
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private FileUploadConfig fileUploadConfig;
    @Autowired
    private CommonInterceptor commonInterceptor;

    @Autowired
    private SiteConfigInterceptor siteConfigInterceptor;

    //컨트롤러 없이 주소와 연결해주는 용도
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/")
//                .setViewName("front/main/index");
//
//        registry.addViewController("/mypage")
//                .setViewName("front/main/index");
//
//        registry.addViewController("/admin")
//                .setViewName("front/main/index");
//    }

    @Override
    //모든 인터셉터 경로
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonInterceptor).addPathPatterns("/**");

        registry.addInterceptor(siteConfigInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("fileUploadConfig :"+fileUploadConfig);

        registry.addResourceHandler(fileUploadConfig.getUrl()+"**")
                .addResourceLocations("file:///"+fileUploadConfig.getPath());
    }
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        return new HiddenHttpMethodFilter();
    }

    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setDefaultEncoding("UTF-8");
        ms.addBasenames("messages.commons","messages.validations","message.errors");
        return ms;
    }
}

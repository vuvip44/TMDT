package com.vuviet.ThuongMai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class StaticResourcesWebConfiguration implements WebMvcConfigurer {
    @Value("${vuviet.upload-file.base-uri}")
    private String baseURI;



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/storage/images/**")
//                .addResourceLocations(baseURI+"images/");
//        registry.addResourceHandler("/css/**")
//                .addResourceLocations(baseURI+"shop/css/");

        registry.addResourceHandler("/image/**").addResourceLocations("classpath:/static/image/")
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
        registry.addResourceHandler("/vendor/**").addResourceLocations("classpath:/static/vendor/")
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
        registry.addResourceHandler("/script/**").addResourceLocations("classpath:/static/script/")
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/")
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
        registry.addResourceHandler("/adminlte/**").addResourceLocations("classpath:/static/adminlte/")
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
        registry.addResourceHandler("/shop/**").addResourceLocations("classpath:/static/shop/")
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());

    }
}

package com.lyf.scm.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
public class FreemarkerConfig {

    @Value("${file.template}")
    private String templatePath;

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(){
        FreeMarkerConfigurer fc=new FreeMarkerConfigurer();
        //设置模板的获取路径(配置路径，若路径为非项目路径,前面需添加file:,若为项目路径,需添加)
        fc.setTemplateLoaderPath(templatePath);
        fc.setDefaultEncoding("UTF-8");
        return fc;
    }

    @Bean
    public ViewResolver viewResolver() {//设置读取模板的格式
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        resolver.setContentType("text/html; charset=UTF-8");
        return resolver;
    }
}
package org.beesden.commerce.web;

import freemarker.template.TemplateException;
import org.beesden.commerce.common.client.CategoryClient;
import org.beesden.commerce.common.client.ProductClient;
import org.beesden.commerce.common.client.SearchClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.io.IOException;

@ComponentScan(basePackages = {"org.beesden.commerce.common", "org.beesden.commerce.web"})
@EnableFeignClients(clients = {
        SearchClient.class,
        CategoryClient.class,
        ProductClient.class
})
@SpringBootApplication
public class PublicAPIApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(PublicAPIApplication.class, args);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedOrigins("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/");
    }

    @Bean
    public ViewResolver viewResolver() {

        final FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();

        resolver.setCache(false);
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        resolver.setContentType("text/html; charset=UTF-8");

        return resolver;

    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() throws IOException, TemplateException {

        final FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        final FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();

        factory.setTemplateLoaderPath("classpath:templates");
        factory.setDefaultEncoding("UTF-8");
        configurer.setConfiguration(factory.createConfiguration());

        return configurer;
    }

}


package org.beesden.commerce.web;

import org.beesden.commerce.common.client.CategoryClient;
import org.beesden.commerce.common.client.ProductClient;
import org.beesden.commerce.common.client.SearchClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

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
        registry.addMapping("/**").allowedOrigins("*");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.tiles();
    }

    @Configuration
    public class BeanConfig {

        @Bean
        public TilesConfigurer tilesConfigurer() {
            TilesConfigurer tiles = new TilesConfigurer();
            tiles.setDefinitions("classpath:templates/tail.xml");
            return tiles;

        }
    }

}


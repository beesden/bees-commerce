package org.beesden.commerce.api;

import org.beesden.commerce.common.client.ProductClient;
import org.beesden.commerce.common.client.SearchClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan(basePackages={"org.beesden.commerce.common", "org.beesden.commerce.api"})
@EnableFeignClients(clients = {
        SearchClient.class,
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

}


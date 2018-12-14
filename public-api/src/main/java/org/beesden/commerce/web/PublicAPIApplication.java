package org.beesden.commerce.web;

import freemarker.cache.ClassTemplateLoader;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.Configuration;
import org.beesden.commerce.common.client.CategoryClient;
import org.beesden.commerce.common.client.ProductClient;
import org.beesden.commerce.common.client.SearchClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;

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


//    @Bean
//    public ServletRegistrationBean<FreemarkerServlet> dispatcherServletRegistration() {
//        ServletRegistrationBean<FreemarkerServlet> registration = new ServletRegistrationBean<>(new FreemarkerServlet(), "/templates/*");
//        return registration;
//    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {

//        TilesViewResolver resolver = new TilesViewResolver();
//        resolver.setPrefix( "" );
//        resolver.setCache( false );
//        resolver.setSuffix( ".ftl" );
//        resolver.setContentType( "text/html" );
//
//        registry.viewResolver(resolver);
        registry.tiles();

    }

//    @Override
//    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//        configurer.enable();
//    }
//
//    @Bean
//    public ServletRegistrationBean<FreemarkerServlet> freemarkerServlet() {
//        return new ServletRegistrationBean<>(new FreemarkerServlet(), "/templates");
//    }

    @Bean
    public ServletRegistrationBean<FreemarkerServlet> servletRegistrationBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean<>(new FreemarkerServlet(), "/templated/*");
        bean.setLoadOnStartup(5);
        return bean;
    }

    @Bean
    FreeMarkerConfigurer freeMarkerConfigurer() {

        Configuration config = new Configuration(Configuration.VERSION_2_3_28);
        config.setTemplateLoader(new ClassTemplateLoader(getClass(), "/templated/"));

        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setConfiguration(config);
        return freeMarkerConfigurer;

    }

//    @Bean
//    public TilesViewResolver tilesViewResolver() {
//        TilesViewResolver tilesViewResolver = new TilesViewResolver();
//        tilesViewResolver.setRenderer(FreemarkerRendererBuilder.createInstance().build());
//        return tilesViewResolver;
//    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions("classpath:templated/tiles-def.xml");
        tilesConfigurer.setCheckRefresh(true);
        return tilesConfigurer;
    }

}


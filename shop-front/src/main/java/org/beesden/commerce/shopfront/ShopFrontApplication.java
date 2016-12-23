package org.beesden.commerce.shopfront;

import org.beesden.commerce.common.client.SearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication(scanBasePackages = "org.beesden.commerce")
@EntityScan(basePackages = "org.beesden.commerce")
@EnableJpaRepositories("org.beesden.commerce")
@EnableDiscoveryClient
@EnableFeignClients(clients = { SearchClient.class })
public class ShopFrontApplication extends WebMvcConfigurerAdapter {

	@Autowired
	private ResourceProperties resourceProperties = new ResourceProperties();

	public static void main(String[] args) {
		SpringApplication.run(ShopFrontApplication.class, args);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Integer cachePeriod = resourceProperties.getCachePeriod();

		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("static/")
				.addResourceLocations("classpath:/static/")
				.setCachePeriod(cachePeriod);

	}

}
package org.beesden.commerce.catalogue;

import org.beesden.commerce.common.client.SearchClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(clients = {
        SearchClient.class
})
@SpringBootApplication()
public class CatalogueApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogueApplication.class, args);
    }

}

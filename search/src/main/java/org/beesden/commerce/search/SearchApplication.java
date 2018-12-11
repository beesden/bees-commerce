package org.beesden.commerce.search;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;

@EnableFeignClients
@SpringBootApplication
public class SearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }

    @Value("${lucene.directory:'./lucene}")
    String indexDirectory;

    @Bean(name = "index")
    public Directory indexDirectory() throws IOException {
        return FSDirectory.open(new File(indexDirectory + "/index").toPath());
    }

    @Bean(name = "taxonomy")
    public Directory facetDirectory() throws IOException {
        return FSDirectory.open(new File(indexDirectory + "/taxonomy").toPath());
    }

}
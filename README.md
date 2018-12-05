# bees-commerce

Suite of eCommerce microservices.

## Installation Steps

### Docker
```
# Build packages and dependencies
$ docker run -it --name bees-commerce-builder -v "$PWD":/usr/src/app -v bees-commerce_m2:/root/.m2 -w /usr/src/app --rm maven mvn clean install

# Run applications
docker-compose up [services]

```

## Available services
 
## commerce-shop

*Spring Boot / Spring Data.*

 Main e-commerce API for managing products, categories and orders.
 
## search
*Spring Boot / Lucene.*
Simple search indexing application.
 

* Index by type / id
* Faceted search
* Fuzzy search terms

## shop-admin
*Spring Boot / AngularJS.*

CMS API  /UI application for managing content from across the enabled services.

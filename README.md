# bees-commerce

Suite of eCommerce microservices.

## Installation Steps

### Docker
```
# Build packages and dependencies
docker run -v $(pwd):/build -v ~/.m2:/root/.m2 --name bees-commerce-builder maven:3.6.0-jdk-8-slim mvn -f /build clean install

# Run applications
docker-compose up [services]

```

## Available services
 
### Catalogue

This service provides catalogue management, including adding / removing products, categories, pricing and stock information.
 
### Search

Simple search indexing service.
 

* Index by type / id
* Faceted search
* Fuzzy search terms

### Public API

RESTful API used to provide front-end eCommerce functionality, linking the catalogue, commerce, account and search services.

## TODO

The remaining services still need to be added n no specific order:

* Content (content-managable pages)
* Account (user / payment info)
* Commerce (basket / payment hub / promotions / orders)
* Ratings (ratings / reviews)
* Recommendations (maybe?)
* PWS (public facing ecommerce website)
* Admin (manage the website)

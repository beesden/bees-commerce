namespace java org.beesden.commerce.common

enum EntityType {
    PRODUCT = 1,
    CATEGORY = 2
}

struct PagedRequest
{
	1: required EntityType type,
	2: required string id
}

struct PagedResponse {}
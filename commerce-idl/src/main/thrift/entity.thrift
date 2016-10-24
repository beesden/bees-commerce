namespace java org.beesden.commerce.common

enum EntityType {
    PRODUCT = 1,
    CATEGORY = 2,
    REVIEW = 5
}

struct EntityReference
{
	1: required EntityType type,
	2: required string id
}
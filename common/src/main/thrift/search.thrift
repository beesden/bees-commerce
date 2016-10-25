namespace java org.beesden.commerce.search

include "entity.thrift"

struct SearchDocument {
	1: required entity.EntityReference entity
	2: required string title
	3: required map<string, set<string>> facets
}

struct SearchForm {
	1: string term;
	2: set<string> ids;
	3: set<entity.EntityType> types;
	4: set<string> facets;
	5: i32 page;
	6: i32 results
	7: string sort;
}

struct SearchResult {
	1: string id;
	2: string title;
	3: map<string, list<string>> metadata;
}

struct SearchResultWrapper {
	1: list<SearchResult> results;
	2: map<string,map<string, i32>> facets;
	3: i32 total;
}

service Search {

    void clearIndex()

	SearchResultWrapper performSearch( 1: optional SearchForm searchForm )

	void removeFromIndex( 1: required entity.EntityReference entity )

	void submitToIndex( 1: required SearchDocument searchDocument )

}
package org.beesden.commerce.search;

import org.apache.lucene.store.RAMDirectory;
import org.beesden.commerce.common.model.EntityReference;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.resource.SearchDocument;
import org.beesden.commerce.common.model.SearchRequest;
import org.beesden.commerce.common.model.resource.SearchResource;
import org.beesden.commerce.search.exception.SearchEntityException;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class SearchControllerTest {

    private final SearchController searchController = new SearchController(new RAMDirectory(), new RAMDirectory());
    private SearchRequest searchRequest;
    private SearchResource results;

    @Before
    public void before() {

        searchRequest = new SearchRequest();
        searchController.clearIndex();
        SearchDocument document = new SearchDocument();

        document.setEntity(new EntityReference(EntityType.CATEGORY, "womens_clothes"));
        document.setTitle("Women's Clothes");
        document.setFacets(new HashMap<>());
        searchController.submitToIndex(document);

        document.setEntity(new EntityReference(EntityType.CATEGORY, "womens_kimonos"));
        document.setTitle("Women's Kimonos");
        document.setFacets(new HashMap<>());
        searchController.submitToIndex(document);

        document.setEntity(new EntityReference(EntityType.PRODUCT, "p1109"));
        document.setTitle("Sequin kimono");
        document.getFacets().put("colour", Set.of("red", "blue"));
        document.getFacets().put("size", Set.of("XS", "SM", "M", "LG"));
        searchController.submitToIndex(document);

        document.setEntity(new EntityReference(EntityType.PRODUCT, "p1455"));
        document.setTitle("Fancy Kimono Hat");
        document.getFacets().put("colour", Set.of("pink", "blue"));
        document.getFacets().put("size", Set.of("XS", "M"));
        searchController.submitToIndex(document);

        document.setEntity(new EntityReference(EntityType.PRODUCT, "p1955"));
        document.setTitle("Massive Pointy hat");
        document.getFacets().put("colour", Set.of("red"));
        document.getFacets().put("size", Set.of("XL", "LG"));
        searchController.submitToIndex(document);

    }

    @Test
    public void clearIndex() {

        searchRequest.setTerm("*");

        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(5);

        searchController.clearIndex();

        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(0);

    }

    @Test
    public void performSearchWithIDs() {

        // Search for invalid results
        searchRequest.setIds(Collections.singleton("p1100"));
        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(0);

        // Search for single id
        searchRequest.setIds(Collections.singleton("p1109"));
        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(1);
        assertThat(results.getResults().get(0).getTitle()).isEqualTo("Sequin kimono");

        // Search for multiple results
        searchRequest.setIds(Set.of("p1455", "test", "p1109"));
        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(2);
        assertThat(results.getResults().get(0).getTitle()).isEqualTo("Sequin kimono");
        assertThat(results.getResults().get(1).getTitle()).isEqualTo("Fancy Kimono Hat");

    }

    @Test
    public void performSearchWithKeywords() {

        // Search for empty results
        searchRequest.setTerm("flower");
        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(0);

        // Search for fuzzy term
        searchRequest.setTerm("kimon");
        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(3);

        // Search for weighted term
        searchRequest.setTerm("fancy hat");
        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(2);
        assertThat(results.getResults().get(0).getId()).isEqualTo("p1455");

        // Misspelled search
        searchRequest.setTerm("kimoo");
        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(3);

        // Case insensitive search
        searchRequest.setTerm("KiMOnO");
        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(3);
    }


    @Test
    public void performSearchWithFacets() {

        searchRequest.setTypes(Set.of(EntityType.PRODUCT));

        // Facet by type
        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(3);
        assertThat(results.getFacets().get(0).getName()).isEqualTo("colour");
        assertThat(results.getFacets().get(0).getFacets().size()).isEqualTo(3);
        assertThat(results.getFacets().get(1).getName()).isEqualTo("size");
        assertThat(results.getFacets().get(1).getFacets().size()).isEqualTo(5);

        // Facet by multiple types
        searchRequest.setTypes(Set.of(EntityType.PRODUCT, EntityType.CATEGORY));
        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(5);

        // Facet by colour
        searchRequest.setFacets(Set.of("colour:red"));
        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(2);
        assertThat(results.getFacets().size()).isEqualTo(2);
        assertThat(results.getFacets().get(0).getName()).isEqualTo("colour");
        assertThat(results.getFacets().get(0).getFacets().size()).isEqualTo(3);

        // Facet by invalid colour
        searchRequest.setFacets(Set.of("colour:orange"));
        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(0);
        assertThat(results.getFacets().size()).isEqualTo(1);

        // Facet by colour and size
        searchRequest.setFacets(Set.of("colour:red", "size:SM"));
        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(1);

    }

    @Test
    public void performSearchWithPagination() {

        searchRequest.setTerm("*");

        // List all results
        results = searchController.performSearch(searchRequest);
        assertThat(results.getResults().size()).isEqualTo(5);
        assertThat(results.getResults().get(0).getTitle()).isEqualTo("Women's Clothes");
        assertThat(results.getTotal()).isEqualTo(5);
        assertThat(results.getRequest().getPage()).isEqualTo(1);

        // Limit results
        searchRequest.setResults(2);
        results = searchController.performSearch(searchRequest);
        assertThat(results.getResults().size()).isEqualTo(2);
        assertThat(results.getResults().get(0).getTitle()).isEqualTo("Women's Clothes");
        assertThat(results.getTotal()).isEqualTo(5);
        assertThat(results.getRequest().getPage()).isEqualTo(1);

        // Paginate results
        searchRequest.setPage(3);
        results = searchController.performSearch(searchRequest);
        assertThat(results.getResults().size()).isEqualTo(1);
        assertThat(results.getResults().get(0).getTitle()).isEqualTo("Massive Pointy hat");
        assertThat(results.getTotal()).isEqualTo(5);
        assertThat(results.getRequest().getPage()).isEqualTo(3);

    }

    @Test
    public void performSearchWithFacetsAndPagination() {

        searchRequest.setTypes(Set.of(EntityType.PRODUCT));
        searchRequest.setFacets(Set.of("colour:red"));

        // Fetch all results
        results = searchController.performSearch(searchRequest);
        assertThat(results.getResults().size()).isEqualTo(2);
        assertThat(results.getTotal()).isEqualTo(2);
        assertThat(results.getRequest().getPage()).isEqualTo(1);

        // Limit results
        searchRequest.setResults(1);
        results = searchController.performSearch(searchRequest);
        assertThat(results.getResults().size()).isEqualTo(1);
        assertThat(results.getTotal()).isEqualTo(2);
        assertThat(results.getResults().get(0).getTitle()).isEqualTo("Sequin kimono");
        assertThat(results.getRequest().getPage()).isEqualTo(1);

        // Paginate results
        searchRequest.setPage(2);
        results = searchController.performSearch(searchRequest);
        assertThat(results.getResults().size()).isEqualTo(1);
        assertThat(results.getTotal()).isEqualTo(2);
        assertThat(results.getResults().get(0).getTitle()).isEqualTo("Massive Pointy hat");
        assertThat(results.getRequest().getPage()).isEqualTo(2);

    }

    @Test
    public void removeFromIndex() {

        searchRequest.setTerm("*");

        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(5);

        searchController.removeFromIndex(new EntityReference(EntityType.PRODUCT, "p1455"));

        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(4);

    }

    @Test
    public void removeFromIndexIgnoresInvalid() {

        searchRequest.setTerm("*");

        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(5);

        searchController.removeFromIndex(new EntityReference(EntityType.PRODUCT, "does-not-exist"));

        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(5);

    }

    @Test
    public void submitToIndex() {

        searchRequest.setTypes(Set.of(EntityType.PRODUCT));

        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(3);

        SearchDocument document = SearchDocument.builder()
                .title("Test Submit")
                .entity(new EntityReference(EntityType.PRODUCT, "testSubmit"))
                .build();

        document.setTitle("Test Submit");
        searchController.submitToIndex(document);

        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(4);
        assertThat(results.getResults().get(3).getTitle()).isEqualTo("Test Submit");

    }

    @Test
    public void submitToIndexWithoutTitle() {

        searchRequest.setTypes(Set.of(EntityType.PRODUCT));

        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(3);

        SearchDocument document = SearchDocument.builder()
                .entity(new EntityReference(EntityType.PRODUCT, "testSubmit"))
                .build();

        try {
            searchController.submitToIndex(document);
            fail("Should not be able to create entity without title");
        } catch (SearchEntityException ignored) {
        }

        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(3);

    }

    @Test
    public void submitToIndexWithoutEntity() {

        searchRequest.setTypes(Set.of(EntityType.PRODUCT));

        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(3);

        SearchDocument document = SearchDocument.builder()
                .title("Test Submit")
                .build();

        try {
            searchController.submitToIndex(document);
            fail("Should not be able to create entity without entity");
        } catch (SearchEntityException ignore) {
        }

        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(3);

    }

    @Test
    public void submitToIndexExistingDocument() {

        searchRequest.setIds(Set.of("p1455"));

        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(1);
        assertThat(results.getResults().get(0).getTitle()).isEqualTo("Fancy Kimono Hat");

        SearchDocument document = SearchDocument.builder()
                .title("Test Update")
                .entity(new EntityReference(EntityType.PRODUCT, "p1455"))
                .build();

        searchController.submitToIndex(document);

        results = searchController.performSearch(searchRequest);
        assertThat(results.getTotal()).isEqualTo(1);
        assertThat(results.getResults().get(0).getTitle()).isEqualTo("Test Update");

    }

}
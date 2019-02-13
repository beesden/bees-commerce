<#include "../layout/default.ftl">

<@layoutDefault>

    <div id="search-wrapper" class="row overlay" data-js="search-filters">

        <div class="column search-title">
            <div class="category-summary">
                <h1>${category.data.title}</h1>
                <div class="description">${category.data.description}</div>
                <img src="http://lorempixel.com/354/200/" width="354" height="200"/>
            </div>
        </div>

        <div class="column side-menu search-filters">
            <#--
            <jsp:include page="../partials/resultsFilters.jsp" />
            -->
        </div>

        <div class="column search-info">
            <#--
            <jsp:include page="../partials/resultsSort.jsp" />
            <nav:pagination pageData="${pageInfo}" />
            -->
        </div>

        <div class="column search-results">
            <ol class="row product-grid" start="1">
                <#list category.products.results as product>
                    ${product.title}
                </#list>
                <#--
                <c:forEach items="${results}" var="product">
                    <search:price product="${product}"/>
                    <search:product imageSize="medium" product="${product}"/>
                </c:forEach>
                -->
            </ol>
        </div>

        <div class="column catalogue-info">
            <#--<nav:pagination pageData="${pageInfo}" />-->
        </div>

    </div>


</@layoutDefault>
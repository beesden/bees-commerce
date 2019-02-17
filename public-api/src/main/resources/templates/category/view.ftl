<#include "../layout/default.ftl">

<@layoutDefault>

    <div class="category" data-js="search-filters">

        <aside class="category__info">

            <header class="category__details">
                <h1>${category.title}</h1>
                <div class="description">${category.description}</div>
            </header>

            <dl class="category__filters">
                <#list products.facets as group>

                    <dt>${group.name}</dt>
                    <#list group.facets?sort_by('name') as facet>
                        <dd>
                            <a href="?facets=${group.name}:${facet.name}">
                                <span class="facet-name">${facet.name}</span>
                                <span class="facet-count">${facet.count}</span>
                            </a>
                        </dd>
                    </#list>

                </#list>
            </dl>

        </aside>

        <header class="category__results">
            <p>Showing ${products.request.startIndex + 1} - ${products.request.startIndex + products.request.results}
                of ${products.total}.</p>
        </header>

        <section class="category__products">
            <#list products.results as product>
                <a class="product-link" href="/categories/${category.id}/${product.id}">
                    <img class="product-image" src="/assets/catalogue/${product.id}_1.jpg" alt="${product.title}"/>
                    <span class="title">${product.title}</span>
                    <span class="price">£25.00</span>
                </a>
            </#list>
        </section>

        <nav class="pagination">

            <#assign paginationStart = (products.request.page > 2)?then(products.request.page - 2, 1) />
            <#if (paginationStart + 4) gt products.totalPages>
                <#assign paginationEnd = products.totalPages paginationStart = (paginationEnd - 4 < 1)?then(1, paginationEnd - 4) />
            <#else>
                <#assign paginationEnd = paginationStart + 4 />
            </#if>

            <a href="?page=${products.request.page - 1}">&lsaquo;</a>
            <#list paginationStart..paginationEnd as page>
                <a class="${(products.request.page = page)?then('pagination__current', '') }"
                   href="?page=${page}">${page}</a>
            </#list>
            <a href="?page=${products.request.page + 1}">&rsaquo;</a>
        </nav>
    </div>

</@layoutDefault>
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

                    <#if group.name == 'category'>
                        <#list group.facets?sort_by('name') as facet>
                            <#assign filterCategory = session.categoryMap[facet.name] />
                            <#if filterCategory?? && filterCategory.parents?seq_contains(category.id)>
                                <dd>
                                    <a href="/categories/${facet.name}">
                                        <span class="facet-name">${session.categoryMap[facet.name].title}</span>
                                        <span class="facet-count">${facet.count}</span>
                                    </a>
                                </dd>
                            </#if>
                        </#list>
                    <#else>
                        <#list group.facets?sort_by('name') as facet>
                            <dd>
                                <a href="?facets=${group.name}:${facet.name}">
                                    <span class="facet-name">${facet.name}</span>
                                    <span class="facet-count">${facet.count}</span>
                                </a>
                            </dd>
                        </#list>
                    </#if>
                </#list>
            </dl>

        </aside>

        <header class="category__results">
            <p>Showing ${request.startIndex + 1} - ${request.startIndex + request.results}
                of ${products.total}.</p>

            <form action="" class="category__sort">

                <input type="hidden" name="results" value="${request.results}"/>
                <#list request.facets as facet>
                    <input type="hidden" name="facets" value="${facet}"/>
                </#list>

                <select name="sort" onchange="this.form.submit()">
                    <option>Sort by...</option>
                    <option value="title" ${(request.sort == 'title')?then('selected', '')}>Name</option>
                    <option value="updated" ${(request.sort == 'updated')?then('selected', '')}>New in</option>
                    <option value="price" ${(request.sort == 'price')?then('selected', '')}>Price (low to high)</option>
                    <option value="price" ${(request.sort == 'price')?then('selected', '')}>Price (high to low)</option>
                    <option value="popular" ${(request.sort == 'popular')?then('selected', '')}>Popular</option>
                </select>

            </form>
        </header>

        <section class="category__products">
            <#list products.results as product>
                <a class="product-link" href="/categories/${category.id}/${product.id}">
                    <img class="product-image" src="/assets/catalogue/${product.id}_1.jpg" alt="${product.title}"/>
                    <span class="title">${product.title}</span>
                    <span class="price">${product.value?string.currency}</span>
                </a>
            </#list>
        </section>

        <nav class="pagination">

            <#assign paginationStart = (request.page > 2)?then(request.page - 2, 1) />
            <#if (paginationStart + 4) gt products.totalPages>
                <#assign paginationEnd = products.totalPages paginationStart = (paginationEnd - 4 < 1)?then(1, paginationEnd - 4) />
            <#else>
                <#assign paginationEnd = paginationStart + 4 />
            </#if>

            <a href="?page=${request.page - 1}">&lsaquo;</a>
            <#list paginationStart..paginationEnd as page>
                <a class="${(request.page = page)?then('pagination__current', '') }"
                   href="?page=${page}">${page}</a>
            </#list>
            <a href="?page=${request.page + 1}">&rsaquo;</a>
        </nav>
    </div>

</@layoutDefault>
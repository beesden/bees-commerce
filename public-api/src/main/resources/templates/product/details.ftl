<#include "../layout/default.ftl">

<@layoutDefault>

    <nav class="header-breadcrumbs">
        <ol>
            <li><a href="/">Home</a></li>
            <#if currentCategory??>
                <li><a href="/categories/${currentCategory.id}">${currentCategory.title}</a></li>
            <#else>
                <li><a href="/categories">View all</a></li>
            </#if>
            <li>${product.title}</a></li>
        </ol>
    </nav>

    <article class="product" data-product-id="${product.id}" itemscope itemtype="http://schema.org/Product">

        <header class="product__details">
            <h1 class="product__title" itemprop="name">${product.title}</h1>

            <p class="product__price" itemprop="offers" itemscope itemtype="http://schema.org/Offer">
                <#if product.maxPrice gt product.minPrice>
                    <span>from </span>${product.minPrice?string.currency}
                    <span>to </span>${product.maxPrice?string.currency}
                <#else>
                    ${product.minPrice?string.currency}
                </#if>
            </p>

            <div class="product__colour">${product.colours[0]}</div>

            <form class="product__form" action="/basket" method="POST">

                <#if product.variants?size gt 1>
                    <select name="colour">
                        <option>Size</option>
                        <#list product.variants as variant>
                            <option value="${variant.sku}">${variant.size}</option>
                        </#list>
                    </select>
                </#if>

                <button class="product__add">Add to bag</button>

            </form>

            <h2 class="product__subtitle">Description</h2>
            <div class="product__info">${product.description}</div>

            <#--
            <c:if test="${!empty param.reviewResponse}">
                <jsp:include page="../reviews/rating.jsp">
                    <jsp:param name="reviewResponse" value="${param.reviewResponse}" />
                    <jsp:param name="metaLinks" value="true" />
                </jsp:include>
            </c:if>
            -->
        </header>

        <figure class="product__image">

            <a class="product__large" data-js="productImages-zoom" href="/assets/catalogue/${product.id}_1.jpg">
                <img class="product__medium" src="/assets/catalogue/${product.id}_1.jpg" alt="${product.title}"/>
            </a>

        </figure>

        <nav class="product__alt-images">
            <ol>
                <li><img src="/assets/catalogue/${product.id}.jpg" alt="${product.title}"/></li>
                <#list 1..4 as variant>
                    <li><img src="/assets/catalogue/${product.id}_${variant}.jpg" alt="${product.title}"/></li>
                </#list>
            </ol>
        </nav>

        <#--
        <%-- Add to bag / EMWBIS / out of stock || wishlist / emailfriend || social links --%>
        <jsp:include page="../partials/productAction.jsp" />
        <jsp:include page="../partials/productShare.jsp" />
        -->

        <#--
            <jsp:include page="../partials/productDescription.jsp" />
            <jsp:include page="../partials/socialShare.jsp" />
            <jsp:include page="../partials/productRatings.jsp" />
            -->

    </article>

    <#if relatedProducts??>
        <aside class="related-products">
            <h2>Related products</h2>

            <section class="product-scroll">
                <#list relatedProducts.results as related>
                    <a class="product-link" href="/categories/${currentCategory.id}/${related.id}">
                        <img class="product-image" src="/assets/catalogue/${related.id}_1.jpg" alt="${related.title}"/>
                        <span class="title">${related.title}</span>
                        <span class="price">${related.value?string.currency}</span>
                    </a>
                </#list>
            </section>
        </aside>
    </#if>

</@layoutDefault>
<#include "../layout/default.ftl">

<@layoutDefault>

    <nav class="header-breadcrumbs">
        <ol>
            <li><a href="/">Home</a></li>
            <#if category??>
                <li><a href="/categories/${category.id}">${category.title}</a></li>
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
                £25.00
                <#--
                <fmt:message key="products.product.price${productPrices.maxPrice > productPrices.minPrice ? '.fromto' : productPrices.wasPrice > productPrices.minPrice ? '.wasnow' : ''}">
                    <fmt:param><fmt:formatNumber type="currency" value="${productPrices.minPrice}" currencySymbol="${pageRequest.currency}" /></fmt:param>
                    <fmt:param><fmt:formatNumber type="currency" value="${productPrices.maxPrice}" currencySymbol="${pageRequest.currency}" /></fmt:param>
                    <fmt:param><fmt:formatNumber type="currency" value="${productPrices.wasPrice}" currencySymbol="${pageRequest.currency}" /></fmt:param>
                </fmt:message>
                -->
            </p>

            <button class="product__add">Add to bag</button>

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


    <aside class="related-products" data-js="product__recent" data-product__id="${product.id}">
        <h2>Related products</h2>

        <section class="product-scroll">
            <#list relatedProducts.results as related>
                <a class="product-link" href="/categories/${category.id}/${related.id}">
                    <img class="product-image" src="/assets/catalogue/${related.id}_1.jpg" alt="${related.title}"/>
                    <span class="title">${related.title}</span>
                    <span class="price">£25.00</span>
                </a>
            </#list>
        </section>
    </aside>

</@layoutDefault>
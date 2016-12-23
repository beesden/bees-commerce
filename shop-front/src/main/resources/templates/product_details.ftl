<div class="row product-details" data-product-id="${product.id}" itemscope itemtype="http://schema.org/Product">

    <div class="column product-title product-info">

    <#-- Product title -->
        <h1 itemprop="name">${product.title}</h1>

    <#-- Product pricing
        <product:message message="price" product="${product}" var="productPrice"/>-->
        <p class="product-price" itemprop="offers" itemscope itemtype="http://schema.org/Offer">${productPrice!}</p>

    <#-- Product id -->
        <p class="product-id">
            <fmt:message key="catalogue.product.id">
                <fmt:param>${product.id}</fmt:param>
            </fmt:message>
        </p>
    </div>

    <div class="column product-display product-images">

    <#-- Zoom product image -->
        <product:image productId="${product.id}" size="zoom" var="productImage"/>
        <a class="product-large" data-js="productImages-zoom" href="${productImage!}">
        <#-- Normal product image -->
            <product:image productId="${product.id}" size="large" var="productImage"/>
            <img class="product-medium" src="${productImage!}" alt="${product.title!}"/>
        </a>

    <#-- Ideally this would be done via a product attribute instead of head requests... -->
        <ul class="product-alternate-images" data-js="productImages-altImages">
            <product:image productId="${product.id}" size="small" var="productImage"
                           variant="${variant > 0 ? variant : null}"/>
            <li><img src="${productImage}" alt="${productTitle}"/></li>
        </ul>

    </div>

    <div class="column product-info">
    <#-- Add to bag / EMWBIS / out of stock || wishlist / emailfriend || social links -->
        <jsp:include page="../partials/productAction.jsp"/>
        <jsp:include page="../partials/productShare.jsp"/>
        <jsp:include page="../partials/socialShare.jsp"/>
    </div>

    <div class="column product-display">
    <#-- Product Information -->
        <jsp:include page="../partials/productDescription.jsp"/>
    </div>

    <div class="column product-related" data-js="productDetails-recent" data-product-id="${product.id}">
        <jsp:include page="../partials/productRelations.jsp">
            <jsp:param name="VC_type" value="RP_YOU_MAY_ALSO_LIKE"/>
        </jsp:include>
    </div>

</div>
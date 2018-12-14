<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${category.data.title}!</title>
</head>
<body>
<h2 class="hello-title">Hello ${category.data.title}!</h2>

<p>${category.products.total} products.</p>
<ul>
    <#list category.products.results as product>
        <li>${product.title}</li>
    </#list>
</ul>

</body>
</html>
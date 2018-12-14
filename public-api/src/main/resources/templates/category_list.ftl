<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Categories!</title>
</head>
<body>
<h2 class="hello-title">Hello!</h2>

<p>${categories.total} products.</p>
<ul>
    <#list categories.results as category>
        <li><a href="/categories/${category.id}">${category.title}</a></li>
    </#list>
</ul>
</body>
</html>
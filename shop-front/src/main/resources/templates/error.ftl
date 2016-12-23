<!DOCTYPE html>

<html lang="">

<head>
    <title>${error.status} - ${error.error}</title>

    <meta charset="utf-8">
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href="${ctx}/resources/images/favicon.ico" rel="icon" type="image/x-icon"/>
    <link href="${ctx}/resources/styles/style.css" rel="stylesheet"/>
</head>

<body class="body fixed-mast">

<div class="masthead">
    <header>
        <h1 class="logo"><a data-cms-href="dashboard" href="${ctx}/"></a></h1>
    </header>
</div>

<main class="container" data-ng-if="currentUser">

    <nav class="breadcrumbs" data-breadcrumbs="">
        <ul>
            <li><a data-icon="dashboard" href="${ctx}/"></a></li>
            <li><span>${error.status} - ${error.error}</span></li>
        </ul>
    </nav>

    <div data-ng-view="">
        <header class="page-header">
            <h1>Application error
                <small>${error.status} - ${error.error}</small>
            </h1>
        </header>

        <section class="card">
        <#switch error.status>
            <#case 404>
                <p>That page cannot be found.</p>
                <#break />
            <#default>
                <pre>${error.trace!error.message}</pre>
                <#break />
        </#switch>
        </section>
    </div>

</main>

<footer>
    <span class="copyright">&copy; Pulselive ${.now?date?string( 'yyyy' )}</span>
</footer>

</body>

</html>
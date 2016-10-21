<!DOCTYPE html>

<html lang="" data-ng-app="cms">

<head>
    <title data-ng-bind="( pageTitle || 'Welcome' ) + ' | Pulse CMS'">Pulse CMS</title>

    <meta charset="utf-8">
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <base href="${ctx}/"/>

    <link href="${ctx}/resources/images/favicon.png" rel="icon" type="image/x-icon"/>
    <link href="${ctx}/resources/styles/style.css" rel="stylesheet"/>

<#if debug>
    <script src="${ctx}/resources/scripts/vendor.js"></script>
    <script src="${ctx}/resources/scripts/scripts.js"></script>
    <script src="${ctx}/resources/scripts/templates.js"></script>
<#else>
    <script src="${ctx}/resources/scripts/vendor.min.js"></script>
    <script src="${ctx}/resources/scripts/app.min.js"></script>
</#if>

    <script>
        (function (ng) {
            ng.module('cms.common').service('API_SETTINGS', function () {
                return {
                    apiEndpoint: '${apiEndpoint!}',
                    appVersion: '${appVersion!}',
                    debug: ${debug?c},
                    googleMapsKey: '${googleMapsKey!}',
                    id: '${environment!}'
                }
            });
        })(angular);
    </script>
</head>

<body class="body fixed-mast fixed-nav" data-ng-class="'co-' + ( pageLoading ? 'loading' : 'loaded' )">

<div class="masthead" data-ng-if="currentUser"></div>

<main class="container" data-ng-if="currentUser">

    <nav data-breadcrumbs></nav>

    <div data-message-box></div>
    <div data-ng-view>
        <noscript><p>This website requires JavaScript to run.</p></noscript>
    </div>

    <footer>
        <span class="copyright">&copy; Beesden ${.now?date?string( 'yyyy' )}</span>
    </footer>

</main>

</body>

</html>
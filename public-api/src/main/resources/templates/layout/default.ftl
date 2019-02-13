<#macro layoutDefault>
    <!DOCTYPE html>
    <html lang="en">

    <head>

        <title>Beesden Commerce</title>
        <link rel="shortcut icon" type="image/x-icon" href="/assets/images/favicon.ico"/>

        <script src="/assets/javascript/polyfill.js"></script>
        <script src="/assets/javascript/scripts.js"></script>

        <link rel="stylesheet" type="text/css" href="/assets/style/global.css"/>
        <link rel="stylesheet" type="text/css" href="/assets/style/checkout.css"/>
        <link rel="stylesheet" type="text/css" href="/assets/style/account.css"/>
        <link rel="stylesheet" type="text/css" href="/assets/style/content.css"/>
        <link rel="stylesheet" type="text/css" href="/assets/style/home.css"/>
        <link rel="stylesheet" type="text/css" href="/assets/style/product.css"/>
        <link rel="stylesheet" type="text/css" href="/assets/style/search.css"/>
        <link rel="stylesheet" type="text/css" href="/assets/style/stores.css"/>

    </head>

    <body>

    <div class="header-links">
        <div class="container">

            <a href="/" class="brand-logo">
                <img src="/pws/client/images/logo.png" alt=""/>
            </a>

            <ul>
                <li class="stores">
                    <a href="/pws/StoreFinder.ice">Stores</a>
                </li>
                <li class="account">
                    <a href="/pws/secure/ManageAccount.ice">My Account</a>
                </li>
                <li class="basket" data-quantity="0">
                    <a href="/pws/ViewBasket.ice">Minibasket</a>
                </li>
            </ul>
        </div>
    </div>

    <div class="header-nav">
        <div class="container">
            <nav class="nav-wrap" role="navigation">
                <input class="nav-toggle" id="nav-toggle" type="checkbox"/>
                <label class="nav-link" for="nav-toggle">Menu</label>
                <form action="/search" method="GET" role="search">
                    <fieldset>
                        <legend>Search</legend>
                        <input name="keywords" required="true" type="search" placeholder="Search..."/>
                    </fieldset>

                    <button action="search" cssClass="light" method="search">Search</button>
                </form>
            </nav>
        </div>
    </div>

    <div class="header-breadcrumbs">
        <div class="container">
        </div>
    </div>


    <main class="content" role="main">
        <div class="container">
            <#nested />
        </div>
    </main>

    </body>

    </html>
</#macro>
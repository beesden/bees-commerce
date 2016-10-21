(function (ng) {
    'use strict';

    var PAGE_SECTIONS = ['commerce', 'admin'];

    ng.module('cms', [
        'cms.common',
        'ngRoute'
    ]);

    ng.module('cms.common', [
        'cms.common.service',
        'cms.common.controller',
        'cms.common.filter',
        'cms.common.model',
        'cms.common.widget',
        'cms.templates',
        'cms.service',
        'cms.controller',
        'cms.filter',
        'cms.model',
        'ngRoute',
        'ngSanitize',
        'dndLists',
        'as.sortable'
    ]).config(function ($compileProvider, $httpProvider, $locationProvider, cmsRouteProvider) {

        // Hide debug data
        $compileProvider.debugInfoEnabled(false);

        // Enable the use of cookies
        $httpProvider.defaults.withCredentials = true;

        // use the HTML5 History API
        $locationProvider.html5Mode(true);

        // Home route
        cmsRouteProvider.when('dashboard', {
            abstract: true,
            mapping: '',
            template: '/common/templates/dashboard.ng.html'
        });

        // Section parent routes
        PAGE_SECTIONS.map(function (section) {

            cmsRouteProvider.when(section, {
                icon: section,
                mapping: section,
                template: '/common/templates/sectionHome.ng.html'
            });

        });

    }).run(function ($rootScope, $anchorScroll, $location, $route, $timeout, loginService, modalService, messageService) {

        // Fetch initial login information before routes can resolve
//		loginService.login().then( function() {
//			$route.reload();
//		} );

        // Add loading and scroll to top
        $rootScope.$on('$routeChangeStart', function ($event, next) {

            // Validate a user has logged in
//			if ( !loginService.getUserId() ) {
//				$event.preventDefault();
//				return;
//			}

            // Validate permissions
            if (next.data && !loginService.hasRoles(next.data.permissions).result) {
                $event.preventDefault();
                $location.path('forbidden', false);
                return;
            }
        });

        // Perform new page actions
        $rootScope.$on('$routeChangeSuccess', function (event, next) {
            $anchorScroll(0);
            modalService.closeAllModals();
            if (next.data) {
                $rootScope.currentPage = next.data.pageSection;
                $rootScope.pageTitle = messageService.getTranslation('title.' + next.data.pageSection, next.params);
            }
        });

        // Display an error message
        $rootScope.$on('$routeChangeError', function ($event, current, previous, rejection) {
            // If logged out - note this is only for route changes.
            // Form submissions will not force a route reload (we don't want to lose the data)
            if (rejection.status === 401) {
                loginService.logout().then(function () {
                    $route.reload();
                });
            }
        });

        // todo - add to account config
        $rootScope.languages = ['en', 'es', 'fr', 'it', 'ja', 'ru', 'zh'];

        $rootScope.currentUser = {};
    });

    // Load global modules
    ng.module('cms.common.model', []);
    ng.module('cms.common.controller', []);
    ng.module('cms.common.service', [
        'ngCookies',
        'ngRoute',
        'ngStorage',
        'ngDialog'
    ]);
    ng.module('cms.common.widget', []);
    ng.module('cms.common.filter', []);

    ng.module('cms.controller', []);
    ng.module('cms.filter', []);
    ng.module('cms.model', []);
    ng.module('cms.service', []);

})(angular);
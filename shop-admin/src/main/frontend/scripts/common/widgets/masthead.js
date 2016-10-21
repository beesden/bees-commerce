(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'masthead', function( $location, $route, $timeout, $window, cmsRoute, loginService, UserSession ) {
		return {
			restrict: 'C',
			templateUrl: '/common/templates/masthead.ng.html',
			transclude: true,

			link: function( scope ) {

				scope.navMenu = cmsRoute.hierarchy();
				scope.language = UserSession.get( UserSession.LANGUAGE, 'en' );

				/**
				 * Completely logout of the CMS
				 */
				scope.logout = function() {
					$location.path( '' );
					loginService.logout();
				};

				/**
				 * Toggle between accounts
				 *
				 * @param account
				 */
				scope.setAccount = function( account ) {
					delete scope.navMenu;
					loginService.changeAccount( account );

					$timeout( function() {
						scope.navMenu = cmsRoute.hierarchy();
					} );

					$route.reload();
				};

				/**
				 * Change the current navmenu parent
				 *
				 * @param $event
				 * @param route
				 */
				scope.setParent = function( $event, route ) {
					if ( !route || route.children.length ) {
						scope.parentId = route ? route.id : null;
						$event.preventDefault();
					}
				};

				/**
				 * Reset menu on route change
				 */
				scope.$on( '$routeChangeStart', function() {
					scope.menu = null;
					scope.parentId = null;
				} );

			}
		};
	} );

})( angular );
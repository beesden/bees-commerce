(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'breadcrumbs', function( $route, cmsRoute ) {
		return {
			replace: true,
			restrict: 'A',
			templateUrl: '/common/templates/_breadcrumbs.ng.html',
			transclude: true,

			link: function( scope ) {

				var routeMap = cmsRoute.lookup();

				var getBreadcrumbs = function( _, next ) {
					var breadcrumbs = [];

					if ( next && next.data ) {
						scope.currentRoute = next.data.pageSection;
						var currentRoute = routeMap[ scope.currentRoute ];
						scope.pageParams = next.params;

						while ( currentRoute && breadcrumbs.indexOf( currentRoute ) === -1 ) {
							breadcrumbs.push( currentRoute );
							currentRoute = routeMap[ currentRoute.parent ];
						}
					}
					scope.breadcrumbs = breadcrumbs.reverse();
				};

				getBreadcrumbs( null, $route.current );
				scope.$on( '$routeChangeSuccess', getBreadcrumbs );

			}
		};
	} );

})( angular );
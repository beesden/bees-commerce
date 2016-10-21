(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'pageMenu', function( $route, cmsRoute ) {

		return {

			replace: true,
			restrict: 'A',
			templateUrl: '/common/templates/_pageMenu.ng.html',

			link: function( scope ) {
				var section = $route.current.data.pageSection;

				if ( section && section != 'dashboard' ) {
					scope.current = cmsRoute.hierarchy( $route.current.data.pageSection );
				} else {
					scope.current = cmsRoute.hierarchy();
				}
			}
		};
	} );

})( angular );
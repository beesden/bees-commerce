(function( ng ) {
	'use strict';

	var UrlGen = function( lookup, attr ) {
		return function( $compile, $location, cmsRoute ) {

			return {
				restrict: 'A',
				scope: {
					params: '='
				},

				link: function( scope, element, attrs ) {
					attrs.$observe( lookup, function() {
						attrs.$set( attr, cmsRoute.getUrl( attrs[ lookup ], scope.params ) || '.' );
					} );
					scope.$watch( 'params', function() {
						attrs.$set( attr, cmsRoute.getUrl( attrs[ lookup ], scope.params ) || '.' );
					} );
				}
			};

		};
	};

	ng.module( 'cms.common.widget' )
		.directive( 'cmsAction', new UrlGen( 'cmsAction', 'action' ) )
		.directive( 'cmsHref', new UrlGen( 'cmsHref', 'href' ) );

})( angular );
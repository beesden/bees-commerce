(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'formDelete', function( $animate, $compile, $route, loginService, cmsRoute ) {

		return {
			priority: 400,
			replace: true,
			restrict: 'A',
			transclude: 'element',

			link: function( scope, element, attrs, ctrl, $transclude ) {

				// Get the route permission and change to 'DELETE'
				var route = cmsRoute.lookup( $route.current.data.pageSection );

				if ( $route.current.params.id && ( ( !route.permissions || !route.permissions.length ) ||
					loginService.hasRoles( [ {
						type: route.permissions[ 0 ].type,
						action: 'DELETE'
					} ] ).result ) ) {

					$transclude( scope, function( clone, newScope ) {

						clone.addClass( 'btn reject' );
						clone.attr( 'data-i18n', 'action.delete' );
						clone.attr( 'data-icon', 'delete' );
						clone.removeAttr( 'data-form-delete' );

						$compile( clone )( newScope );

						$animate.enter( clone, element.parent(), element );

						// This acts as a polyfill for HTML5 form attributes
						clone.on( 'click', function() {
							scope.$eval( attrs.formDelete );
						} );
					} );
				}

			}
		};

	} );

})( angular );


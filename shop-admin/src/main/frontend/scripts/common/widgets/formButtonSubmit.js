(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'formSubmit', function( $animate, $compile, $route, loginService, cmsRoute ) {

		return {
			priority: 400,
			replace: true,
			restrict: 'A',
			transclude: 'element',

			link: function( scope, element, attrs, ctrl, $transclude ) {

				// Get the route permission and change to 'UPDATE'
				var route = cmsRoute.lookup( $route.current.data.pageSection );
				if ( ( !route.permissions || !route.permissions.length ) ||
					loginService.hasRoles( [ {
						type: route.permissions[ 0 ].type,
						action: ( route.permissions[ 0 ].action === 'CREATE' ? 'CREATE' : 'UPDATE' )
					} ] ).result ) {

					$transclude( scope, function( clone, newScope ) {

						clone.addClass( 'btn primary' );
						clone.attr( 'data-i18n', 'action.save' );
						clone.attr( 'data-icon', 'save' );
						clone.attr( 'data-ng-disabled', 'formCtrl.$pristine' );
						clone.attr( 'form', attrs.formSubmit );
						clone.removeAttr( 'data-form-submit' );

						$compile( clone )( newScope );

						$animate.enter( clone, element.parent(), element );

						// This acts as a polyfill for HTML5 form attributes
						clone.on( 'click', function() {
							if ( !clone[ 0 ].form ) {
								ng.element( document.forms[ attrs.formSubmit ] ).triggerHandler( 'submit' );
							}
						} );
					} );
				}

			}
		};

	} );

})( angular );


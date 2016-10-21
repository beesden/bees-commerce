(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'hasRole', function( $animate, loginService ) {

		return {
			priority: 600,
			restrict: 'A',
			transclude: 'element',

			link: function( scope, element, attr, ctrl, $transclude ) {

				var roles = scope.$eval( attr.hasRole ) || [];
				var block;
				var childScope;
				var previousElements;

				// Below simply mimics the ng-if functionality (with some tweaks)
				var permissions = loginService.hasRoles( roles || [] );
				if ( permissions.result ) {
					if ( !childScope ) {
						$transclude( function( clone, newScope ) {
							childScope = newScope;
							block = {
								clone: clone
							};
							$animate.enter( clone, element.parent(), element );
						} );
					}
				} else {
					if ( previousElements ) {
						previousElements.remove();
						previousElements = null;
					}
					if ( childScope ) {
						childScope.$destroy();
						childScope = null;
					}
					if ( block ) {
						previousElements = getBlockNodes( block.clone );
						$animate.leave( previousElements ).then( function() {
							previousElements = null;
						} );
						block = null;
					}
				}

			}
		};
	} );

})( angular );
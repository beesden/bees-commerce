(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'pageHeader', function( $route, messageService ) {

		return {

			restrict: 'C',

			link: function( scope, element ) {
				var title = ng.element( '<h1>' );
				title.html( messageService.getTranslation( 'title.' + $route.current.data.pageSection, $route.current.params ) );
				element.prepend( title );
			}
		};
	} );

})( angular );
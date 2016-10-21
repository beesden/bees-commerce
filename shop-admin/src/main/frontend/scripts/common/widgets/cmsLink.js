(function( ng ) {
	'use strict';

	/**
	 * Dynamically generate a link from the site routing.
	 */
	ng.module( 'cms.common.widget' ).directive( 'cmsLink', function( $compile, cmsRoute ) {

		return {
			replace: true,
			restrict: 'A',
			link: function( scope, element, attrs ) {

				var route = cmsRoute.lookup( attrs.cmsLink );

				element.removeAttr( 'data-cms-link' );
				element.attr( 'data-i18n', 'action.' + route.id );
				element.attr( 'data-icon', route.icon );
				element.attr( 'data-has-role', ng.toJson( route.permissions ) );

				// Use the route service here as we may have URLs which inherit from the url params
				element.attr( 'href', cmsRoute.getUrl( route.id ) );
				$compile( element )( scope );
			},
			template: function( element, attrs ) {
				return '<a class="btn ' + ( 'secondary' in attrs ? 'secondary' : 'primary' ) + '"></a>';
			}
		};

	} );

})( angular );
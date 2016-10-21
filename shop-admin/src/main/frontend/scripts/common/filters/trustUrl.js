(function( ng ) {
	'use strict';

	ng.module( 'cms.common.filter' ).filter( 'trustUrl', function( $sce ) {

		/**
		 * Trust a resource URL
		 */
		return function( url ) {
			return $sce.trustAsResourceUrl( url );
		};

	} );

})( angular );

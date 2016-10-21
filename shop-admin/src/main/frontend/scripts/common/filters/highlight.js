(function( ng ) {
	'use strict';

	ng.module( 'cms.common.filter' ).filter( 'highlight', function( messageService ) {

		/**
		 * Highlight specific strings within
		 */
		return function( label, highlight ) {
			var regex = new RegExp( '(' + highlight + ')', 'gi' );
			return label.replace( regex, '<strong>$1</strong>' );
		};

	} );

})( angular );

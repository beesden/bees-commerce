(function( ng ) {
	'use strict';

	ng.module( 'cms.common.filter' ).filter( 'i18n', function( messageService ) {

		/**
		 * Super-amazing-awesome-wow translations
		 */
		return function( lookup, params ) {
			return messageService.getTranslation( lookup, params ) || ( lookup );
		};

	} );

})( angular );

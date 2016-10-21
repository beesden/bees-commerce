(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'i18n', function( messageService ) {
		return {

			restrict: 'A',

			link: function( scope, element, attrs ) {
				attrs.$observe( 'i18n', function() {
					element.html( messageService.getTranslation( attrs.i18n, scope.$eval( attrs.i18nParams ) ) );
				} );

				attrs.$observe( 'i18nParams', function() {
					element.html( messageService.getTranslation( attrs.i18n, scope.$eval( attrs.i18nParams ) ) );
				} );
			}
		};
	} );

})( angular );
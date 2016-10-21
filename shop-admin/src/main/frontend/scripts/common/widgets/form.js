(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'form', function( $compile, messageService ) {

		return {
			replace: false,
			restrict: 'E',
			priority: -1,

			link: function( scope, element, attrs ) {
				element.attr( 'novalidate', '' );
				$compile( element[ 0 ].form )( scope );

				scope.formCtrl = element.data().$formController;
				var submitForm = false;

				//confirm on exit when the user reloads the page
				window.onbeforeunload = function() {
					if ( attrs.ngSubmit && scope.formCtrl.$dirty && attrs.nowarn === undefined ) {
						return messageService.getTranslation( 'message.confirm.unsaved.changes' );
					}
				};

				//confirm on exit when the user navigates to another route
				scope.$on( '$routeChangeStart', function( event ) {
					if ( attrs.ngSubmit && scope.formCtrl.$dirty && !submitForm && attrs.nowarn === undefined ) {
						if ( !confirm( messageService.getTranslation( 'message.confirm.unsaved.changes' ) ) ) {
							event.preventDefault();
						}
					}
				} );

				scope.$on( '$destroy', function() {
					window.onbeforeunload = null;
				} );

				element.on( 'submit', function( e ) {
					messageService.clear();

					if ( !scope.formCtrl.$valid ) {
						messageService.addMessage( 'warn', 'error.form' );

						// display inline errors by setting each of the field
						scope.$apply( function() {
							ng.forEach( scope.formCtrl.$error, function( field ) {
								ng.forEach( field, function( errorField ) {
									errorField.$setDirty();
									errorField.$setTouched();
								} );
							} );
						} );

						e.stopImmediatePropagation();
					} else {
						submitForm = true;
					}
				} );
			}
		};
	} );

})( angular );

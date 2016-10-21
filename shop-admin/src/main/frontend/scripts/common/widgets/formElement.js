(function( ng ) {
	'use strict';

	var labelIndex = 0;

	ng.module( 'cms.common.widget' ).directive( 'formElement', function( $compile, messageService ) {

		return {
			priority: 10,
			require: [ '?ngModel', '^?tabGroup' ],
			restrict: 'C',
			replace: true,

			link: function( scope, element, attrs, ctrls ) {

				var wrapper = ng.element( '<div class="standardInput">' );
				element.after( wrapper );

				var ngModel = ctrls[ 0 ];
				var tabGroup = ctrls[ 1 ];

				// Allow alt for flexbox todo
				if ( attrs.editor ) {
					wrapper.addClass( 'block' );
				}

				// Dynamically create a label element
				if ( attrs.ngLabel || attrs.label || attrs.name ) {

					element.removeAttr( 'data-label' );

					var label = ng.element( '<label class="form-label">' );
					var labelText = ng.element( '<span class="form-label-text">' );

					if ( attrs.ngLabel ) {
						attrs.$observe( 'ngLabel', function() {
							labelText.text( attrs.ngLabel );
						} );
					} else {
						labelText.text( messageService.getTranslation( 'label.' + ( attrs.label || attrs.name || 'unknown' ) ) );
					}
					labelText = $compile( labelText )( scope );
					label.append( labelText );

					// Add a tooltip with description (shown on hover/click)
					if ( attrs.description ) {
						label.attr( 'data-tooltip', attrs.description );
					}
					label = $compile( label )( scope );

					wrapper.append( label );

					// Generate a unique ID to bind labels and inputs together
					labelIndex = labelIndex % 5000;
					var index = attrs.id || 'input-' + labelIndex++;
					label.attr( 'for', index );
					element.attr( 'id', index );
					if ( !attrs.name ) {
						element.attr( 'name', index );
					}
				} else {
					wrapper.addClass( 'no-label' );
				}

				// Generate a placeholder label if requested
				if ( attrs.placeholder ) {
					element.attr( 'placeholder', messageService.getTranslation( 'label.' + attrs.placeholder ) );
				}

				// Append a styling class
				switch ( attrs.type ) {
					case 'checkbox':
					case 'checklist':
						wrapper.addClass( 'form-' + ( attrs.displayType || 'checkbox' ) );
						wrapper.prepend( element );
						break;
					case 'radio':
						wrapper.addClass( 'form-' + ( attrs.displayType || attrs.type ) );
						wrapper.prepend( element );
						break;
					default:
						wrapper.addClass( 'form-' + ( attrs.displayType || 'input' ) );
						wrapper.append( element );
						break;
				}

				// Validation styling
				if ( attrs.required ) {
					wrapper.addClass( 'required' );
				}
				if ( attrs.formMatches ) {
					wrapper.addClass( 'matches' );
				}

				// Dynamically add validation
				if ( ngModel ) {
					var elementScope = scope.$new( true );
					elementScope.ngModel = ngModel;

					var errorElement = $compile(
						'<span class="form-error" data-ng-if="ngModel.$dirty && ngModel.$touched" data-ng-repeat="( error, value ) in ngModel.$error" data-i18n="message.error.form.' + ( attrs.name || attrs.type || 'default' ) + '.{{ error }}"></span>'
					)( elementScope );
					wrapper.append( errorElement );

					if ( tabGroup ) {
						elementScope.$watchGroup( [ 'ngModel.$valid', 'ngModel.$touched' ], function() {
							if ( ngModel.$touched && ngModel.$dirty ) {
								tabGroup.toggleError( ngModel.$valid );
							}
						} );
					}
				}
			}
		};
	} );

})( angular );

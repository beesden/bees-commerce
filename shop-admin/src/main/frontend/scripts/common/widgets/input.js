(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'input', function( $filter, $parse ) {

		return {
			restrict: 'E',
			require: '?ngModel',

			link: function( scope, element, attrs, ngModel ) {

				// Helper function to retrieve current input model
				var ngModelGet = $parse( attrs.ngModel );

				/*
				 * Custom timestamp input element
				 * Displays as a date (or datetime-local) stamp
				 * Models as a numeric timestamp value
				 */

				var dateInputConfig = function( type, dateFormat ) {
					element.attr( 'type', type );

					ngModel.$formatters.push( function( value ) {
						if ( value ) {
							return $filter( 'date' )( value || attrs.value, dateFormat );
						}
					} );
					ngModel.$parsers.push( function( value ) {
						// convert string date to a timestamp
						var date = new Date( value );
						// revert timezone offset addded by the input element so it's back in UTC
						date = new Date( date.getTime() + date.getTimezoneOffset() * 60 * 1000 );

						return value ? date.getTime() : null;
					} );
					scope.$watch( attrs.ngValue, function( value ) {
						if ( value ) {
							var model = ngModelGet( scope );
							ngModelGet.assign( scope, model || new Date( value ).getTime() );
						}
					} );
					element.attr( 'step', attrs.step || 1 );
				};

				switch ( attrs.type ) {

					// Time input
					case 'timeInput':
						dateInputConfig( 'time', 'HH:mm' );
						break;
					// Date input
					case 'datestamp':
						dateInputConfig( 'date', 'yyyy-MM-dd' );
						break;
					// Date / time input
					case 'timestamp':
						dateInputConfig( 'datetime-local', 'yyyy-MM-ddTHH:mm:ss' );
						break;

					/*
					 * Parse boolean values
					 */
					case 'checkbox':
						// Check initial states
						ngModel.$formatters.push( function( value ) {
							return value === 'true' || value === true;
						} );
						break;

					/*
					 * Parse numeric values
					 */
					case 'number':
						// Check initial states
						ngModel.$formatters.push( function( value ) {
							if ( value ) {
								return parseFloat( value );
							}
						} );
						break;

					/*
					 * Custom checklist input element
					 * Displays as a checkbox
					 * Models an array - each checklist item with same the model has an ngValue which is a value within the array
					 */
					case 'checklist':
						element.attr( 'type', 'checkbox' );
						// Convert model to string array
						var getModel = function( scope ) {
							var model = ngModelGet( scope ) || [];
							if ( model.constructor !== Array || model.length ) {
								model = model.toString().split( ',' );
							}
							return model;
						};
						// Update model array on click
						element.on( 'click', function( $event ) {
							var model = getModel( scope );
							// Prevent disabled interaction
							if ( element.attr( 'readonly' ) || element.attr( 'disabled' ) ) {
								$event.preventDefault();
								return;
							}
							// Toggle value
							if ( model.indexOf( attrs.checkValue ) === -1 ) {
								model.push( attrs.checkValue );
							} else {
								model.splice( model.indexOf( attrs.checkValue ), 1 );
							}
							// Update here if model does not already exist
							scope.$apply( function() {
								ngModelGet.assign( scope, model );
								ngModel.$setViewValue( model );
							} );
						} );
						// Check initial states
						ngModel.$formatters.push( function() {
							var model = getModel( scope );
							element.attr( 'checked', model.indexOf( attrs.checkValue ) !== -1 );
							return true;
						} );
						// Ensure model is not changed
						ngModel.$parsers.push( function() {
							return getModel( scope );
						} );
						break;

					/*
					 * Handle file inputs
					 */
					case 'file':
						element.bind( 'change', function( $event ) {
							var files = null;
							if ( $event.target.files.length ) {
								files = $event.target.files;
							}
							scope.$apply( function() {
								ngModelGet.assign( scope, files );
							} );
							if ( attrs.ngChange ) {
								scope.$eval( attrs.ngChange );
							}
						} );
						ngModel.$render = function() {
							if ( attrs.ngChange ) {
								scope.$eval( attrs.ngChange );
							}
						};
						break;
				}
			}
		};

	} );

})( angular );


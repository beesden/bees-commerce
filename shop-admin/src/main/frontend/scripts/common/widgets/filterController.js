(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'filterController', function( $location ) {

		var FilterController = function( $scope, $attrs ) {
			var filterCtrl = this;
			filterCtrl.params = {};
			if ( !$attrs.filterController ) {
				ng.merge( filterCtrl.params, $location.search() );
			}

			/**
			 * Reset all filter params
			 */
			filterCtrl.clear = function() {
				var filterCtrl = this;
				filterCtrl.params = {};
				if ( $attrs.filterController ) {
					$scope.$eval( $attrs.filterController );
				} else {
					$location.search( '' );
				}
			};

			/**
			 * Check if at least one custom param exists
			 */
			filterCtrl.hasParams = function() {
				var filterCtrl = this;

				return Object.keys( filterCtrl.params ).length > 0;
			};

			/**
			 * Update the URL with the filter parameters
			 *
			 * @param params
			 */
			filterCtrl.update = function( params ) {
				var filterCtrl = this;

				filterCtrl.params.page = null;
				ng.merge( filterCtrl.params, params );

				if ( $attrs.filterController ) {
					$scope.$eval( $attrs.filterController );
				} else {
					for ( var key in filterCtrl.params ) {
						if ( filterCtrl.params.hasOwnProperty( key ) ) {
							$location.search( key, filterCtrl.params[ key ] || null );
						}
					}
				}
			};
		};

		return {
			scope: true,
			restrict: 'A',
			controllerAs: 'filterCtrl',
			controller: FilterController
		};

	} );

})( angular );
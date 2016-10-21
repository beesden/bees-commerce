(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'filterPaging', function() {

		var calculatePagedata = function( params, total, outerPadding, innerPadding ) {
			var i;

			// Generic pagedata
			var pageData = {
				page: Number( params.page ) || 1,
				pages: [],
				outerWindow: []
			};

			// Calculate outer pages
			while ( outerPadding > 0 && total < ( 2 + outerPadding * 2 ) ) {
				outerPadding--;
			}
			for ( i = 0; i <= outerPadding; i++ ) {
				pageData.outerWindow.push( i );
			}

			// Iterate all values in between
			pageData.first = pageData.page - innerPadding;
			pageData.last = pageData.page + innerPadding;

			// Adjust if below outer values
			while ( pageData.first < ( 2 + outerPadding ) ) {
				pageData.first++;
				pageData.last++;
			}

			// Adjust if above upper values
			while ( pageData.last > ( total - outerPadding - 1 ) ) {
				pageData.last--;
			}

			// Calculate first index
			for ( i = pageData.first; i <= pageData.last; i++ ) {
				pageData.pages.push( i );
			}

			return pageData;
		};

		return {
			replace: true,
			restrict: 'A',
			require: '^filterController',
			templateUrl: '/common/templates/filterPaging.ng.html',

			scope: {
				total: '=filterPaging'
			},

			link: function( scope, element, attrs, filterController ) {

				var innerPadding = Number( attrs.inner ) || 1;
				var outerPadding = Number( attrs.outer ) || 0;

				scope.$watch( 'total', function() {
					scope.pageData = calculatePagedata( filterController.params, scope.total, outerPadding, innerPadding );
				} );

				scope.changePage = function( page ) {
					if ( page > scope.total ) {
						page = scope.total;
					} else if ( page < 2 ) {
						page = 1;
					}

					if ( scope.pageData.page !== page ) {
						filterController.update( { page: page === 1 ? null : page } );
						scope.pageData = calculatePagedata( filterController.params, scope.total, outerPadding, innerPadding );
					}
				};
			}

		};

	} );

})( angular );
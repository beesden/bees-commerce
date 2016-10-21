(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'searchForm', function() {
		return {
			replace: true,
			restrict: 'A',
			templateUrl: '/common/templates/_searchForm.ng.html',
			transclude: true,

			link: function( scope, element, attrs ) {
				scope.searchUrl = attrs.searchForm;
				scope.types = scope.$eval( attrs.contentTypes );
			}
		};
	} );

})( angular );
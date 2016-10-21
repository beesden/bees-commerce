(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'tooltip', function( $compile, $parse ) {

		return {
			restrict: 'A',

			link: function( scope, element, attrs ) {

				if ( attrs.tooltip ) {

					var tooltipScope = scope.$new( true );
					var tooltip = $compile(
						'<span class="tooltip-icon" data-icon="info" data-ng-class="{toggle: showTooltip}" data-ng-click="showtooltip = !showtooltip">' +
						'<span class="tooltip" data-i18n="' + attrs.tooltip + '"></span>' +
						'</span>'
					)( tooltipScope );

					element.append( tooltip );

				}
			}
		};
	} );
})( angular );
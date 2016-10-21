(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'tabGroup', function() {
		return {
			require: [ '^tabWrapper', 'tabGroup' ],
			restrict: 'A',
			controller: function() {
				var _self = this;

				_self.toggleError = function( valid ) {
					if ( _self.tab ) {
						_self.tab.toggleError( valid );
					}
				};
			},
			link: function( scope, element, attrs, ctrls ) {
				var tabWrapper = ctrls[0];
				var ctrl = ctrls[1];

				ctrl.tab = tabWrapper.register( attrs.tabGroup.toLowerCase(), element, attrs );
			}
		};
	} );

})( angular );
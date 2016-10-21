(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'tabWrapper', function( $location ) {

		var Tab = function( label, element, type ) {
			var _self = this;

			_self.label = label;
			_self.elements = element;
			_self.type = type;
		};

		Tab.prototype.add = function( element ) {
			var _self = this;

			_self.elements.push( element[ 0 ] );
		};

		Tab.prototype.toggleError = function( valid ) {
			var _self = this;

			_self.error = !valid;
		};

		Tab.prototype.toggle = function( label ) {
			var _self = this;

			if ( _self.label === label ) {
				_self.active = true;
				_self.elements.removeClass( 'hide' );
			} else {
				_self.active = false;
				_self.elements.addClass( 'hide' );
			}

			$location.hash( label );
			$location.replace();
		};

		return {

			restrict: 'A',
			templateUrl: '/common/templates/_tabWrapper.ng.html',
			transclude: true,

			controller: function( $scope, $element, $attrs ) {
				var tabRegistry = {};
				var defaultTab = $attrs.tabWrapper || 'main';
				$scope.tabs = [];

				$scope.toggle = function( label ) {
					if ( !$scope.tabs.length ) {
						// Can't toggle if there's nothing to toggle
						return;
					} else if ( !tabRegistry[ label ] ) {
						// No label of that type exists
						label = defaultTab;
					}
					// Loop over and toggle
					for ( var i = 0; i < $scope.tabs.length; i++ ) {
						var tab = $scope.tabs[ i ];
						tab.toggle( label );
					}
				};

				this.register = function( label, element ) {
					if ( !tabRegistry[ label ] ) {
						tabRegistry[ label ] = new Tab( label, element, $attrs.tabType );
						if ( defaultTab === label ) {
							$scope.tabs.splice( 0, 0, tabRegistry[ label ] );
						} else {
							$scope.tabs.push( tabRegistry[ label ] );
						}
					} else {
						tabRegistry[ label ].add( element );
					}

					// Check tab exists
					var currentTab;
					if ( $attrs.tabType !== 'soft' ) {
						currentTab = $location.hash();
					}
					if ( !tabRegistry[ currentTab ] ) {
						currentTab = tabRegistry[ defaultTab ] ? defaultTab : $scope.tabs[ 0 ].label;
					}
					$scope.toggle( currentTab );

					return tabRegistry[ label ];
				};
			}
		};
	} );

})( angular );
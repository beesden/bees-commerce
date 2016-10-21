(function( ng ) {
	'use strict';

	ng.module( 'cms.common.service' ).provider( 'cmsRoute', function( $routeProvider ) {

		/**
		 * CMS Route object.
		 *
		 * @param id
		 * @param routeConfig
		 * @constructor
		 */
		var Route = function( id, routeConfig ) {
			ng.merge( this, { id: id, children: [] }, routeConfig );
		};

		Route.prototype = {
			/**
			 * Check if this route or any of the children are the currently active route.
			 *
			 * @param activeId route id
			 * @returns {boolean}
			 */
			isActive: function( activeId ) {
				var _self = this;

				var currentRoute = routeNodes[ activeId ];
				var i = 0;

				while ( currentRoute && currentRoute.id && ++i < 5 ) {
					if ( _self.id === currentRoute.id ) {
						return true;
					}
					currentRoute = routeNodes[ currentRoute.parent ];
				}
			}
		};

		/**
		 * Route Permission object.
		 *
		 * @param {String} type permission type, e.g. 'CTEXT'
		 * @param {( 'CREATE' | 'READ' | 'UPDATE'| 'DELETE' )} action permission action
		 * @constructor
		 */
		var RoutePermission = function( type, action ) {
			this.type = type;
			this.action = action;
		};

		var routeNodes = {};
		var typeLookup = {};

		// Default routing
		$routeProvider.otherwise( { templateUrl: '/common/templates/404.ng.html' } );

		return {
			/**
			 * Todo - tweak
			 *
			 * @param type
			 * @param action
			 * @returns {RoutePermission}
			 */
			permission: function( type, action ) {
				return new RoutePermission( type, action );
			},

			/**
			 * Todo - tweak
			 *
			 * @param value
			 */
			when: function( id, options ) {

				// Store mapping
				routeNodes[ id ] = new Route( id, options );

				if ( options.contentType ) {
					typeLookup[ options.contentType ] = id;
				}

				$routeProvider.when( '/' + options.mapping, {

					// Controls
					controllerAs: 'ctrl',
					controller: options.controller,

					// Route info
					data: {
						pageSection: id,
						permissions: options.permissions
					},

					// If page refreshes on param change
					reloadOnSearch: !!options.search,

					// Additional data
					resolveAs: 'data',
					resolve: ng.merge( {}, options.resolve, {
						params: function( $route ) {
							return $route.current.params;
						}
					} ),

					// Template to use
					templateUrl: options.template

				} );
				return this;
			},

			$get: function( $route, loginService ) {

				return {
					/**
					 * Lookup and build a route URL from supplied parameters.
					 *
					 * @param id
					 * @param params
					 * @returns {string}
					 */
					getUrl: function( id, params ) {
						var route = routeNodes[ id.toLowerCase() ];

						params = ng.merge( {}, $route.current.params, params );
						if ( route ) {
							var url = route.mapping;

							url = url.replace( /(\/)?:(\w+)([\?\*])?/g, function( _, slash, key, option ) {
								var optional = option === '?' ? option : null;
								slash = slash || '';
								if ( !optional || params[ key ] ) {
									return slash + params[ key ];
								}
								return '';
							} );
							return url.replace( /\/\//g, '/' );
						}
					},

					/**
					 * Fetch the route for a content type.
					 *
					 * @param type content type
					 * @returns {Route} route
					 */
					lookupType: function( type ) {
						return typeLookup[ type ];
					},

					/**
					 * Lookup a specific route.
					 *
					 * @param id route ID
					 * @returns {*}
					 */
					lookup: function( id ) {
						return ng.copy( id ? routeNodes[ id ] : routeNodes );
					},

					/**
					 * Construct a visible hierarchy of CMS routes.
					 * Excludes abstract routes and routes with no permissions.
					 *
					 * @returns {Route} parent route node (w/ attached children)
					 */
					hierarchy: function( routeId ) {
						var rootRoute = new Route( 'root', { mappings: [ '/' ] } );

						var visibleRoutes = {};
						var id;
						var node;

						// Filter routes by visibility
						for ( id in routeNodes ) {
							if ( !routeNodes.hasOwnProperty( id ) ) {
								continue;
							}
							node = routeNodes[ id ];
							if ( !node.abstract && loginService.hasRoles( node.permissions ).result ) {
								visibleRoutes[ id ] = ng.copy( node );
							}
						}

						// Populate route children
						for ( id in visibleRoutes ) {

							if ( !visibleRoutes.hasOwnProperty( id ) ) {
								continue;
							}

							node = visibleRoutes[ id ];

							if ( !node.parent ) {
								rootRoute.children.push( node );
							} else if ( visibleRoutes[ node.parent ] ) {
								visibleRoutes[ node.parent ].children.push( node );
							}
						}

						// Strip empty parent routes
						rootRoute.children = rootRoute.children.filter( function( route ) {
							return route.children.length;
						} );

						return visibleRoutes[ routeId ] || rootRoute;
					}
				};
			}
		};
	} );

})( angular );

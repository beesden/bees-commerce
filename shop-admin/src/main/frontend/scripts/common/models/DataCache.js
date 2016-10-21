(function( ng ) {
	'use strict';

	ng.module( 'cms.common.model' ).factory( 'DataCache', function( $http, $q, $filter, CacheWrapper ) {

		/**
		 * Wrapper for persisting data from HTTP requests
		 *
		 * @name DataCache
		 * @param {CacheWrapper~BaseConfig} config
		 * @constructor
		 */
		var DataCache = function( config, params ) {
			var _self = this;

			_self.cache = new CacheWrapper( config );
			_self.params = params;
		};

		DataCache.prototype = {

			/**
			 * Facade a fake HTTP request if data is already cached
			 * TODO - return the same promise if already requested
			 *
			 * @param config
			 * @returns {Promise}
			 */
			$http: function( config ) {
				var _self = this;
				var cache = _self.cache && _self.cache.getData();

				var deferred = $q.defer();

				if ( cache ) {
					deferred.resolve( { data: cache } );
				} else {
					$http( config ).then( function( response ) {
						_self.cache.setData( response.data );
						deferred.resolve( { data: response.data } );
					} );
				}
				return deferred.promise;
			},

			/**
			 * Provide a paginated response
			 *
			 * @param config
			 * @returns {Promise}
			 */
			paginate: function( config ) {
				var _self = this;

				return _self.$http( config ).then( function( response ) {

					if ( config.params.showAll ) {
						return response;
					}
					var params = ng.merge( {}, _self.params, config.params );
					var pagedCache = response.data.items.slice( 0 );

					// Calculate length of query
					params.page = params.page || 1;
					var startIndex = ( params.page - 1 ) * params.limit;
					var endIndex = ( params.page * params.limit );

					// Filter by keyword
					if ( params.search ) {
						pagedCache = $filter( 'filter' )( pagedCache, _self.cache.config.filter ? _self.cache.config.filter( params.search ) : params.search );
					}

					// Sort ordering
					if ( params.sortId ) {
						pagedCache.sort( function( a, b ) {
							return a[ params.sortId ] < b[ params.sortId ] ? -1 : a[ params.sortId ] > b[ params.sortId ] ? 1 : 0;
						} );
					}
					if ( params.sortType === 'ASC' ) {
						pagedCache.reverse();
					}

					// Return sliced data
					response.data = {
						firstItem: startIndex + 1,
						lastItem: params.page >= Math.ceil( pagedCache.length / params.limit ) ? pagedCache.length : endIndex,
						items: pagedCache.slice( startIndex, endIndex ),
						limit: params.limit,
						page: params.page,
						totalItems: pagedCache.length,
						totalPages: Math.ceil( pagedCache.length / params.limit )
					};
					return response;
				} );
			},

			/**
			 * Clear the cache and send a http request
			 *
			 * @param config
			 * @returns {Promise}
			 */
			update: function( config ) {
				var _self = this;

				_self.cache.clearData();
				return $http( config );
			}
		};

		return DataCache;

	} );

})( angular );
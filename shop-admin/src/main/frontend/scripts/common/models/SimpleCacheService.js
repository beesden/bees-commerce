(function( ng ) {
	'use strict';

	/**
	 * Extendable API CRUD service with cacheing.
	 * Any requests which change content will clear the cache.
	 *
	 * @param options datacache customisation options
	 * @param type routeId for API URL construction
	 * @param params default lookup parameters
	 * @constructor
	 */
	ng.module( 'cms.common.model' ).factory( 'SimpleCacheService', function( $http, DataCache, UrlBuilder ) {

		return function( options, type, params ) {

			var _self = this;

			_self.dataCache = new DataCache( options, params );
			_self.url = new UrlBuilder( '/' + type + '/:id/:action.json', params );

			/**
			 * List items, and store in the cache.
			 * This should only be used for lookups which return all the results in one go.
			 *
			 * @param params lookup parameters, e.g. pagination
			 * @returns {HttpPromise}
			 */
			_self.getAll = function( params ) {
				var _self = this;

				return _self.dataCache.paginate( {
					url: _self.url.build(),
					params: _self.url.getParams( params, true )
				} ).then( function( response ) {
					return response.data;
				} );
			};

			/**
			 * Get an uncached item
			 *
			 * @param id item ID to retrieve
			 * @returns {HttpPromise}
			 */
			_self.get = function( id ) {
				var _self = this;

				return $http( {
					url: _self.url.build( { id: id } )
				} );
			};

			/**
			 * Delete the item, clearing the cache in the process.
			 *
			 * @param id item ID to delete
			 * @returns {HttpPromise}
			 */
			_self.delete = function( id ) {
				var _self = this;

				return _self.dataCache.update( {
					method: 'DELETE',
					url: _self.url.build( { id: id } )
				} );
			};

			/**
			 * Create an item, clearing the cache in the process.
			 *
			 * @param data item form to save
			 * @returns {HttpPromise}
			 */
			_self.create = function( data ) {
				var _self = this;

				return _self.dataCache.update( {
					data: data,
					method: 'POST',
					url: _self.url.build()
				} ).then( function( response ) {
					if ( typeof response.data !== 'object' ) {
						response.data = { id: response.data };
					}
					return response;
				} );
			};

			/**
			 * Update an item, clearing the cache in the process.
			 *
			 * @param pathParams item ID to update
			 * @param data item form to save
			 * @returns {HttpPromise}
			 */
			_self.update = function( pathParams, data ) {
				var _self = this;

				return _self.dataCache.update( {
					data: data,
					method: 'PUT',
					url: _self.url.build( pathParams )
				} ).then( function( response ) {
					response.data = pathParams;
					return response;
				} );
			};

		}

	} );

})( angular );

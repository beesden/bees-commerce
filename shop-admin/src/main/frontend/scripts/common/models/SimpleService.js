(function( ng ) {
	'use strict';

	/**
	 * Extendable API CRUD service
	 *
	 * @param type routeId for API URL construction
	 * @param params default lookup parameters
	 * @param customUrl custom URL pattern override
	 */
	ng.module( 'cms.common.model' ).factory( 'SimpleService', function( $http, UrlBuilder ) {

		return function( type, params, customUrl ) {
			var _self = this;

			_self.type = type;
			_self.url = new UrlBuilder( '/' + type + ( customUrl || '/:id/:action/:actionId.json' ), params );

			/**
			 * Delete an item
			 *
			 * @param id item ID to delete
			 * @returns {HttpPromise}
			 */
			_self.delete = function( id ) {
				var _self = this;

				return $http( {
					method: 'DELETE',
					url: _self.url.build( { id: id } )
				} );
			};

			/**
			 * List items
			 *
			 * @param params lookup parameters, e.g. pagination
			 * @returns {HttpPromise}
			 */
			_self.getAll = function( params, pathParams ) {
				var _self = this;

				return $http( {
					url: _self.url.build( pathParams ),
					params: _self.url.getParams( params )
				} ).then( function( response ) {
					return response.data;
				} );
			};

			/**
			 * Get an item
			 *
			 * @param id item ID to retrieve
			 * @returns {HttpPromise}
			 */
			_self.get = function( id ) {
				var _self = this;

				return $http( {
					url: _self.url.build( { id: id } )
				} ).then( function( response ) {
					return response.data;
				} );
			};

			/**
			 * Create an item
			 *
			 * @param data item form to save
			 * @returns {HttpPromise}
			 */
			_self.create = function( data ) {
				var _self = this;

				return $http( {
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
			 * Update an item
			 *
			 * @param pathParams item ID to update
			 * @param data item form to save
			 * @returns {HttpPromise}
			 */
			_self.update = function( pathParams, data ) {
				var _self = this;

				return $http( {
					data: data,
					method: 'PUT',
					url: _self.url.build( pathParams )
				} ).then( function( response ) {
					if ( response.data && typeof response.data !== 'object' ) {
						response.data = { id: response.data };
					}
					return response;
				} );
			};

		};

	} );

})( angular );
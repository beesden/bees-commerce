(function( ng ) {
	'use strict';

	ng.module( 'cms.common.service' ).factory( 'UrlBuilder', function( API_SETTINGS ) {

		var UrlBuilder = function( url, params ) {
			this.baseUrl = API_SETTINGS.apiEndpoint + url;
			this.baseParams = ng.merge( { limit: 10 }, params );
		};

		UrlBuilder.prototype = {

			/**
			 * Add new parameters to the map
			 * Prevent undefined keys from being addedd
			 */
			build: function( pathParams ) {
				var url = this.baseUrl;
				for ( var key in pathParams ) {
					if ( pathParams[ key ] ) {
						url = url.replace( ':' + key, encodeURIComponent( pathParams[ key ] ) );
					}
				}
				// Strip out any other path params
				return url.replace( /\/:\w+/g, '' );
			},

			/**
			 * Add new parameters to the map
			 * Prevent undefined keys from being added
			 */
			getParams: function( merge, returnAll ) {
				var params = returnAll ? ng.merge( {}, this.baseParams ) : {};
				for ( var key in merge ) {
					// Remove key if default
					if ( !returnAll && this.baseParams[ key ] == merge[ key ] ) {
						delete params[ key ];
					}
					// Parse numerical parameters
					else if ( typeof this.baseParams[ key ] === 'number' && !isNaN( parseInt( merge[ key ] ) ) ) {
						params[ key ] = Number( merge[ key ] );
					}
					// Otherwise add to params
					else {
						params[ key ] = merge[ key ];
					}
				}
				return params;
			}
		};

		return UrlBuilder;

	} );

})
( angular );

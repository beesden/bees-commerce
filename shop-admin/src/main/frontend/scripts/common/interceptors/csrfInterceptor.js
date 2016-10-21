(function( ng ) {
	'use strict';

	ng.module( 'cms.common.service' ).factory( 'csrfInterceptor', function( $cookies ) {

		return {
			/**
			 * Attach the current user session to the request headers
			 *
			 * @param config
			 * @returns {*}
			 */
			request: function( config ) {

				var token = $cookies.get( 'XSRF-TOKEN' );

				if ( token ) {
					config.headers[ 'X-CSRF-TOKEN' ] = token;
				}

				return config;
			}
		};

	} ).config( function( $httpProvider ) {
		$httpProvider.interceptors.push( 'csrfInterceptor' );
	} );

})( angular );
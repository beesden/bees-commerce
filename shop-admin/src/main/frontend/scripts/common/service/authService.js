(function( ng ) {
	'use strict';

	ng.module( 'cms.common.service' ).service( 'authService', function( $http, $httpParamSerializerJQLike, UrlBuilder ) {

		var authUrl = new UrlBuilder( '/auth/:type' );

		this.authenticate = function( loginForm ) {
			return $http( {
				method: 'POST',
				data: $httpParamSerializerJQLike( loginForm ),
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
				},
				url: authUrl.build( { type: 'login' } )
			} );
		};

		this.logout = function() {
			return $http( {
				url: authUrl.build( { type: 'logout' } )
			} );
		};

		this.getCurrentUser = function() {
			return $http( {
				url: authUrl.build( { type: 'user' } )
			} );
		};

	} );

})( angular );

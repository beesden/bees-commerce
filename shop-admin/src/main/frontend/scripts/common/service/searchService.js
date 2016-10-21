(function( ng ) {
	'use strict';

	ng.module( 'cms.common.service' ).service( 'searchService', SearchService );

	function SearchService( $http, UrlBuilder ) {

		var service = this;

		var url = new UrlBuilder( '/search.json', {
			limit: 20
		} );

		service.performSearch = function( params ) {
			return $http( {
				params: url.getParams( params ),
				url: url.build()
			} ).then( function( response ) {
				return response.data;
			} );
		};

	}

})( angular );
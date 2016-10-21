(function( ng ) {
	'use strict';

	ng.module( 'cms.common.model' ).factory( 'CacheWrapper', function( $localStorage, API_SETTINGS, UserSession, loginService, messageService ) {

		/**
		 * @typedef {Object} CacheWrapper~BaseConfig
		 *
		 * @property {boolean} account - should the data be cached by account
		 * @property {int} timeout - how long should the cache be persisted for ( in hours )
		 * @property {string} persist - ID to save the cache by in local storage
		 * @property {function} filter - Custom filter options
		 */
		var baseConfig = {};

		/**
		 * Wrapper for persisting models into storage
		 */
		var CacheWrapper = function( config ) {
			var _self = this;

			_self.config = ng.merge( {}, baseConfig, config );

			if ( _self.config.persist ) {
				var cache = _self.validate( $localStorage[ _self.config.persist ] );

				if ( cache ) {
					for ( var prop in cache ) {
						if ( cache.hasOwnProperty( prop ) ) {
							_self[ prop ] = cache[ prop ];
						}
					}
				} else {
					_self.expiry = new Date();
					_self.expiry.setDate( _self.expiry.getDate() + 7 );
					_self.endpoint = btoa( API_SETTINGS.id );
					_self.userId = loginService.getUserId();
					_self.version = API_SETTINGS.appVersion + '-' + btoa( API_SETTINGS.id );
				}
			}
		};

		CacheWrapper.prototype = {

			/**
			 * Remove all data inside the cache
			 *
			 * @param clearAll completely clear all caches regardless of account
			 */
			clearData: function( clearAll ) {
				var _self = this;

				if ( _self.data && _self.config.persistType && !clearAll ) {
					var persistId = _self.getPersistId();
					delete _self.data[ persistId ];
					if ( _self.config.persist ) {
						delete $localStorage[ _self.config.persist ][ persistId ];
					}
				} else {
					delete _self.data;
					if ( _self.config.persist ) {
						delete $localStorage[ _self.config.persist ];

					}
				}

				_self.setData();
			},

			/**
			 * Get persistId
			 */
			getPersistId: function() {
				var _self = this;

				switch ( _self.config.persistType ) {
					case 'account':
						return UserSession.get( UserSession.ACCOUNT );
					case 'site':
						return UserSession.get( UserSession.SITE );
					default:
						return 0;

				}
			},

			/**
			 * Retrieve the cache
			 *
			 * @returns {*}
			 */
			getData: function() {
				var _self = this;

				if ( _self.data && _self.config.persistType ) {
					return _self.data[ _self.getPersistId() ];
				}
				return _self.data;
			},

			/**
			 * Set the cache data
			 *
			 * @param {*} data
			 */
			setData: function( data ) {
				var _self = this;

				if ( _self.config.persistType ) {
					_self.data = _self.data || {};
					_self.data [ _self.getPersistId() ] = data;
				} else {
					_self.data = data;
				}

				if ( _self.config.persist ) {
					$localStorage[ _self.config.persist ] = _self;
				}
				return _self.getData();
			},

			/**
			 * Validate the current object
			 */
			validate: function( cache ) {
				var _self = this;

				if ( !cache ) {
					return null;
				} else if ( !cache.userId || cache.userId !== loginService.getUserId() ) {
					messageService.debug( 'Clearing cache: user mismatch', _self.config.persist );
					_self.clearData( true );
				} else if ( !cache.version || cache.version !== ( API_SETTINGS.appVersion + '-' + btoa( API_SETTINGS.id ) ) ) {
					messageService.debug( 'Clearing cache: version mismatch', _self.config.persist );
					_self.clearData( true );
				} else if ( !cache.endpoint || cache.endpoint !== btoa( API_SETTINGS.id ) ) {
					messageService.debug( 'Clearing cache: API endppoint mismatch', _self.config.persist );
					_self.clearData( true );
				} else if ( !cache.expiry || cache.expiry < new Date() ) {
					messageService.debug( 'Clearing cache: timeout exceeded', _self.config.persist );
					_self.clearData( true );
				} else {
					return cache;
				}
			}
		};

		return CacheWrapper;

	} ).config( function( $localStorageProvider ) {
		$localStorageProvider.setKeyPrefix( 'beesAdmin:' );
	} );

})( angular );
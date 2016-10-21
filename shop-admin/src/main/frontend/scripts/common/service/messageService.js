(function( ng ) {
	'use strict';

	ng.module( 'cms.common.service' ).service( 'messageService', function( $rootScope, API_SETTINGS, I18N ) {

		var _self = this;
		var messages = [];
		var messageQueue = [];

		var translateWithVars = function( translation, key, value ) {
			// Insert params
			translation = translation.replace( new RegExp( '\\$\\{?' + key + '\\}?', 'gi' ), function( _, match ) {
				return value;
			} );
			// Translation languages
			translation = translation.replace( new RegExp( '%\{?' + key + '\}?', 'gi' ), function( _, language ) {
				return _self.getTranslation( 'language.' + value );
			} );
			return translation;
		};

		_self.getTranslation = function( key, params ) {
			var translation = I18N.en[ key.toLowerCase() ] || key;
			var i;

			// Get the params
			if ( params && params.constructor === Array ) {
				for ( i = 0; i < params.length; i++ ) {
					translation = translateWithVars( translation, i, params[ i ] );
				}
			} else if ( params && params.constructor === Object ) {
				for ( i in params ) {
					if ( params.hasOwnProperty( i ) ) {
						translation = translateWithVars( translation, i, params[ i ] );
					}
				}
			} else {
				translation = translateWithVars( translation, 0, params );
			}

			return translation;
		};

		_self.addMessage = function( level, key, params ) {
			messages.unshift( {
				level: level || 'info',
				text: _self.getTranslation( 'message.' + key, params )
			} );
			_self.debug( 'Logging message: ', { level: level, key: key, params: params } );
		};

		_self.getMessages = function() {
			return messages;
		};

		_self.removeMessage = function( messageIndex ) {
			messages.splice( messageIndex, 1 );
		};

		_self.clear = function() {
			messages.length = 0;
		};

		_self.debug = function() {
			if ( API_SETTINGS.debug ) {
				console.info.apply( console, arguments );
			}
		};

		_self.warn = function() {
			if ( API_SETTINGS.debug ) {
				console.warn.apply( console, arguments );
			}
		};

		_self.error = function() {
			if ( API_SETTINGS.debug ) {
				console.error.apply( console, arguments );
			}
		};

		/**
		 * Queue a message to be published following a redirect
		 *
		 * @param level
		 * @param key
		 * @param params
		 */
		_self.queueMessage = function( level, key, params ) {
			messageQueue.push( [ level, key, params ] );
		};

		/**
		 * Load all messages from the queue
		 *
		 * @param level
		 * @param key
		 * @param params
		 */
		_self.load = function() {
			_self.clear();
			for ( var i = 0; i < messageQueue.length; i++ ) {
				_self.addMessage.apply( _self, messageQueue[ i ] );
			}
			messageQueue = [];
		};

		// Update messages on route change
		$rootScope.$on( '$routeChangeSuccess', function() {
			_self.load();
		} );

	} );

})( angular );

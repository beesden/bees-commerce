(function( ng ) {
	'use strict';

	ng.module( 'cms.common.model' ).service( 'UserSession', function( $window, $localStorage ) {

		var SESSION_ID = 'Session';
		var cache = $localStorage[ SESSION_ID ] || {};

		/**
		 * Globally shared storage object.
		 * This should only be used to store session based preference, such as selected account ID or language.
		 * The storage mechanism is in no way secure.
		 *
		 * @constructor
		 */
		var Session = function() {
			this.ACCOUNT = 'account';
			this.LANGUAGE = 'language';
			this.SITE = 'site';
		};

		Session.prototype = {
			/**
			 * Removes all data from the local storage
			 */
			clear: function() {
				cache = {};
				$window.sessionStorage.clear();
				delete $localStorage[ SESSION_ID ];
			},

			/**
			 * Fetch a particular item from the session storage
			 * This does not fetch from the stored data, so that
			 *
			 * @param key
			 * @returns {*}
			 */
			get: function( key, defaultValue ) {
				if ( !cache[ key ] && defaultValue ) {
					this.set( key, defaultValue );
				}
				return $window.sessionStorage[ key ] || cache[ key ];
			},
			/**
			 * Set a specific item into the session.
			 * Note there is no validation on this.
			 *
			 * @param key
			 * @param value
			 * @param {boolean} [optional] - only add this key/value to the session if no value currently set
			 */
			set: function( key, value, optional ) {
				if ( !optional || ( !cache[ key ] && value ) ) {
					cache[ key ] = value;
					$window.sessionStorage[ key ] = value || '';
					$localStorage[ SESSION_ID ] = cache;
				}
			}
		};

		return new Session();

	} );

})( angular );
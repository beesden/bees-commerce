(function( ng ) {
	'use strict';

	ng.module( 'cms.common.service' ).factory( 'AuthUser', function( UserSession, messageService ) {

		/**
		 * AuthUser object for storing current user data
		 *
		 * @param data
		 * @constructor
		 */
		var AuthUser = function( data ) {
			var _self = this;

			for ( var key in data ) {
				if ( data.hasOwnProperty( key ) ) {
					this[ key ] = data[ key ];
				}
			}
			_self.permissions = _self.permissions || {};
			UserSession.set( UserSession.LANGUAGE, _self.defaultLanguage );

			messageService.debug( 'AuthUser created:', _self );
		};

		AuthUser.prototype = {
			/**
			 * Check if a user has a specific permission
			 *
			 * @param permission
			 * @returns {boolean}
			 */
			hasPermission: function( permission ) {
				var accountPermissions = this.permissions[ UserSession.get( UserSession.ACCOUNT ) ];

				// System user privileges (enforced by API
				if ( !this.userId ) {
					messageService.debug( 'System user access granted' );
					return true;
				}

				// No account permissions
				if ( !accountPermissions ) {
					messageService.warn( 'You do not have any permissions for the current account:', UserSession.get( UserSession.ACCOUNT ) );
					return false;
				}

				// No permissions of that type
				if ( !accountPermissions[ permission.type ] || !accountPermissions[ permission.type ].length ) {
					messageService.warn( 'You do not have any permission actions of type:', permission.type );
					return false;
				}

				// No permissions of that action
				if ( accountPermissions[ permission.type ].indexOf( permission.action ) === -1 ) {
					messageService.warn( 'You do not have the required permission action:', permission.type, permission.action );
					return false;
				}
				return true;
			}

		};

		return AuthUser;

	} );

})( angular );

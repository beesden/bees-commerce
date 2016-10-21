(function( ng ) {
	'use strict';

	ng.module( 'cms.common.service' ).service( 'loginService', function( $rootScope, $route, $q, AuthUser, $localStorage, UserSession, authService, modalService ) {

		var _self = this;
		var currentUser = new AuthUser( {} );

		var loginPopup = function() {
			return modalService.showModal( {
				template: '/common/templates/login.ng.html',
				controller: 'loginCtrl',
				cssClass: 'login-form small',
				force: true
			} ).then( function() {
				return _self.login();
			} );
		};

		/**
		 * Clear local login data and get the current user data from the API.
		 *
		 * @returns {*}
		 */
		_self.login = function() {

			// Clear local data
			currentUser = undefined;
			delete $rootScope.currentUser;

			return authService.getCurrentUser().then( function( response ) {
				$rootScope.currentUser = currentUser = new AuthUser( response.data );
				_self.changeAccount();
				return currentUser;
			}, function() {
				return loginPopup();
			} );
		};

		/**
		 * Clearing all current user data and provide login again
		 *
		 * @returns {*}
		 */
		_self.logout = function() {
			return authService.logout().then( function() {
				$localStorage.$reset();
				UserSession.clear();
				return _self.login();
			} );
		};

		/**
		 * Set an account and validate against available user accounts
		 *
		 * @param accountId
		 */
		_self.changeAccount = function( accountId ) {
			accountId = accountId || UserSession.get( UserSession.ACCOUNT );
			if ( !accountId || !currentUser.accounts[ accountId ] ) {
				accountId = Object.keys( currentUser.accounts )[ 0 ];
			}
			currentUser.currentAccount = accountId;
			UserSession.set( UserSession.ACCOUNT, accountId );
		};

		/**
		 * Retrieve the current user ID (e.g. for validating if a user exists)
		 */
		_self.getUserId = function() {
			return currentUser && currentUser.userId;
		};

		/**
		 * Discover if the current user has a specific role
		 */
		_self.hasRoles = function( permissions ) {
			var valid = [];
			var invalid = [];

			if ( permissions ) {
				for ( var i = 0; i < permissions.length; i++ ) {
					if ( !currentUser.hasPermission( permissions[ i ] ) ) {
						invalid.push( permissions[ i ] );
					} else {
						valid.push( permissions[ i ] );
					}
				}
			}
			return { result: !invalid.length, valid: valid, invalid: invalid };
		};

	} );

})( angular );

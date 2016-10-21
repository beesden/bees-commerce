(function( ng ) {
	'use strict';

	// Register app controllers
	ng.module( 'cms.common.controller' ).controller( 'loginCtrl', function( authService, messageService, modalService ) {

		/**
		 * @ngdoc controller
		 * @name cms.content.controller:LoginController
		 *
		 * @description Login form controller
		 */
		var LoginController = function() {
			var _self = this;

			_self.loginForm = {};
		};

		/**
		 * @name submit
		 * @description Submit a login request
		 *
		 * @ngdoc method
		 * @methodOf cms.content.controller:LoginController
		 */
		LoginController.prototype.submit = function() {
			var _self = this;
			_self.failed = false;

			// Submit request
			authService.authenticate( _self.loginForm ).then( function( response ) {
				modalService.closeAllModals( response );
				return response;
			}, function() {
				// Populate with an error message
				messageService.clear();
				_self.failed = true;
			} );
		};

		return new LoginController();

	} );

})
( angular );

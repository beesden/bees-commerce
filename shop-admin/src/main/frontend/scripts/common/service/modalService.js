(function( ng ) {
	'use strict';

	ng.module( 'cms.common.service' ).service( 'modalService', function( $compile, $q, $rootScope, $templateCache, ngDialog, messageService ) {

		var $body = ng.element( document.body );

		this.closeAllModals = function( response ) {
			ngDialog.closeAll( response );
		};

		/**
		 *
		 */
		this.confirm = function() {
			var _self = this;

			return _self.showModal( {
				controller: function( $scope ) {
					$scope.close = function( confirm ) {
						$scope.closeThisDialog( confirm );
					};
				},
				cssClass: 'small',
				template: '/common/templates/_confirmModal.ng.html'
			} );
		};

		/**
		 *
		 * @param options
		 * @returns {*}
		 */
		this.showModal = function( options ) {

			var deferred = $q.defer();

			messageService.clear();

			var template = options.plainTemplate || $templateCache.get( options.template );
			template = '<div data-message-box></div>' + template;

			var modal = ngDialog.open( {
				className: options.cssClass,

				// Standard modal routing
				controller: options.controller,
				controllerAs: 'ctrl',
				resolve: options.resolve,
				plain: true,
				template: template,

				// Prevent closing
				showClose: !options.force,
				closeByEscape: !options.force,
				closeByNavigation: !options.force,
				closeByDocument: !options.force
			} );

			modal.closePromise.then( function( response ) {
				var value = [ '$escape', '$closeButton', '$document' ].indexOf( response.value ) === -1 ? response.value : false;
				deferred.resolve( value );
			} );

			return deferred.promise;
		};

		/**
		 * Overlay a modal containing an iframe
		 *
		 * @param frameContent
		 */
		this.showFrame = function( frameContent ) {
			var pageWrapper = ng.element( '<div class="page-preview">' );
			var button = ng.element( '<button class="btn primary small" data-icon="close">' );
			pageWrapper.append( button );
			$body.append( pageWrapper );

			button.bind( 'click', function() {
				pageWrapper.remove();
			} );

			var iframe = document.createElement( 'iframe' );
			pageWrapper[ 0 ].appendChild( iframe );
			iframe.contentWindow.document.open();
			iframe.contentWindow.document.write( frameContent );
			iframe.contentWindow.document.close();
		};

	} );

})( angular );

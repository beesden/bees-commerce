(function( ng ) {
	'use strict';

	ng.module( 'cms.common.model' ).factory( 'SimpleFormController', function( $route, $filter, $location, messageService, modalService, cmsRoute ) {

		/**
		 * @ngdoc controller
		 * @name cms.common.controller:SimpleFormController
		 *
		 * @description Simple base form controller extended by almost all data forms.
		 */
		var SimpleFormController = function( dataService, data, routeId ) {
			var ctrl = this;
			ctrl.form = data;
			ctrl.dataService = dataService;
			ctrl.routeId = routeId;
		};

		/**
		 * @name refreshPage
		 * @description Reload the page form: if an id is specified (or detected in the current page routing) then
		 *     redirect to this page - otherwise redirect to the parent route.
		 *
		 * @ngdoc method
		 * @methodOf cms.common.controller:SimpleFormController
		 */
		SimpleFormController.prototype.refreshPage = function( params ) {

			var _self = this;

			var url;

			if ( params && typeof params !== 'object' ) {
				params = { id: params };
			}

			if ( params ) {
				url = cmsRoute.getUrl( _self.routeId + '.edit', params );
			} else {
				url = cmsRoute.getUrl( _self.routeId );
			}

			$location.path( url );
			$route.reload();
		};

		/**
		 * @name delete
		 * @description Delete an item, then load the parent route.
		 *
		 * @ngdoc method
		 * @methodOf cms.common.controller:SimpleFormController
		 */
		SimpleFormController.prototype.delete = function() {
			var ctrl = this;

			return modalService.confirm().then( function( confirm ) {
				if ( confirm ) {
					ctrl.dataService.delete( $route.current.params.id ).then( function() {
						messageService.queueMessage( 'success', ctrl.routeId + '.deleted', { id: $route.current.params.id } );
						ctrl.refreshPage();
					} );
				}
			} );
		};

		/**
		 * @name save
		 * @description Create or update a single content item, then refresh the page.
		 *
		 * @ngdoc method
		 * @methodOf cms.common.controller:SimpleFormController
		 */
		SimpleFormController.prototype.save = function() {
			var ctrl = this;

			if ( $route.current.params.id ) {
				return ctrl.dataService.update( $route.current.params, ctrl.form ).then( function( response ) {
					messageService.queueMessage( 'success', ctrl.routeId + '.updated', response.data || $route.current.params );
					ctrl.refreshPage( response.data || $route.current.params );
				} );
			} else {
				return ctrl.dataService.create( ctrl.form ).then( function( response ) {
					messageService.queueMessage( 'success', ctrl.routeId + '.created', response.data || $route.current.params );
					ctrl.refreshPage( response.data || $route.current.params );
				} );
			}
		};

		return SimpleFormController;

	} );

})( angular );
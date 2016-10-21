(function( ng ) {
	'use strict';

	ng.module( 'cms.common.widget' ).directive( 'messageBox', function( messageService ) {

		return {
			scope: true,
			restrict: 'A',
			templateUrl: '/common/templates/messageBox.ng.html',
			replace: true,

			link: function( scope ) {

				scope.messages = messageService.getMessages();

				/**
				 * closes the alert and removes it from the list of alerts.
				 * @param alertIndex
				 */
				scope.closeMessage = function( alertIndex ) {
					messageService.removeMessage( alertIndex );
				};

			}
		};
	} );

})( angular );
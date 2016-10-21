(function (ng) {
    'use strict';

    /**
     * @ngdoc controller
     * @name cms.controller:DeliveryFormController
     * @extends cms.common.controller:SimpleFormController
     * @ngInject
     *
     * @description Delivery controller
     */
    ng.module('cms.controller').controller('DeliveryController', function (SimpleFormController, deliveryService) {

        var _self = this;

        SimpleFormController.call(_self, deliveryService, 'commerce.delivery');

    }).config(function (cmsRouteProvider) {

        // List all delivery options
        cmsRouteProvider.when('commerce.delivery', {
            icon: 'delivery',
            parent: 'commerce',
            mapping: 'commerce/delivery',
            permissions: [cmsRouteProvider.permission('AACCOUNT', 'READ')],
            resolve: {
                deliveryList: function ($route, deliveryService) {
                    return deliveryService.getAll($route.current.params);
                }
            },
            search: true,
            template: '/templates/deliveryList.ng.html'
        });

        // Create a new delivery option
        cmsRouteProvider.when('admin.delivery.create', {
            abstract: true,
            icon: 'add',
            parent: 'admin.delivery',
            mapping: 'admin/delivery/add',
            controller: 'DeliveryFormController',
            permissions: [cmsRouteProvider.permission('AACCOUNT', 'CREATE')],
            resolve: {
                delivery: function ($route, deliveryService) {
                    return {data: {}};
                }
            },
            template: '/templates/deliveryForm.ng.html'
        });

        // Edit an existing delivery option
        cmsRouteProvider.when('admin.delivery.edit', {
            abstract: true,
            parent: 'admin.delivery',
            mapping: 'admin/delivery/edit/:id',
            controller: 'DeliveryFormController',
            permissions: [cmsRouteProvider.permission('AACCOUNT', 'READ')],
            resolve: {
                delivery: function ($route, deliveryService) {
                    return deliveryService.get($route.current.params.id);
                }
            },
            template: '/templates/deliveryForm.ng.html'
        });

    });

})(angular);
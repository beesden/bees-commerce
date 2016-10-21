(function (ng) {
    'use strict';

    /**
     * @ngdoc controller
     * @name cms.controller:ProductFormController
     * @extends cms.common.controller:SimpleFormController
     * @ngInject
     *
     * @description Product controller
     */
    ng.module('cms.controller').controller('ProductController', function (SimpleFormController, productService) {

        var _self = this;

        SimpleFormController.call(_self, productService, 'commerce.product');

    }).config(function (cmsRouteProvider) {

        // List all products
        cmsRouteProvider.when('commerce.product', {
            icon: 'product',
            parent: 'commerce',
            mapping: 'commerce/product',
            permissions: [cmsRouteProvider.permission('AACCOUNT', 'READ')],
            resolve: {
                productList: function ($route, productService) {
                    return productService.getAll($route.current.params);
                }
            },
            search: true,
            template: '/templates/productList.ng.html'
        });

        // Create a new product
        cmsRouteProvider.when('commerce.product.create', {
            abstract: true,
            icon: 'add',
            parent: 'commerce.product',
            mapping: 'commerce/product/add',
            controller: 'ProductController',
            permissions: [cmsRouteProvider.permission('AACCOUNT', 'CREATE')],
            resolve: {
                product: function ($route, productService) {
                    return {data: {}};
                }
            },
            template: '/templates/productForm.ng.html'
        });

        // Edit an existing product
        cmsRouteProvider.when('commerce.product.edit', {
            abstract: true,
            parent: 'commerce.product',
            mapping: 'commerce/product/edit/:id',
            controller: 'ProductController',
            permissions: [cmsRouteProvider.permission('AACCOUNT', 'READ')],
            resolve: {
                product: function ($route, productService) {
                    return productService.get($route.current.params.id);
                }
            },
            template: '/templates/productForm.ng.html'
        });

    });

})(angular);
(function (ng) {
    'use strict';

    /**
     * @ngdoc controller
     * @name cms.controller:CategoryFormController
     * @extends cms.common.controller:SimpleFormController
     * @ngInject
     *
     * @description Category controller
     */
    ng.module('cms.controller').controller('CategoryController', function (SimpleFormController, categoryService) {

        var _self = this;

        SimpleFormController.call(_self, categoryService, 'commerce.category');

    }).config(function (cmsRouteProvider) {

        // List all product categories
        cmsRouteProvider.when('commerce.category', {
            icon: 'category',
            parent: 'commerce',
            mapping: 'commerce/category',
            permissions: [cmsRouteProvider.permission('AACCOUNT', 'READ')],
            resolve: {
                categoryList: function ($route, categoryService) {
                    return categoryService.getAll($route.current.params);
                }
            },
            search: true,
            template: '/templates/categoryList.ng.html'
        });

        // Create a new product category
        cmsRouteProvider.when('admin.category.create', {
            abstract: true,
            icon: 'add',
            parent: 'admin.category',
            mapping: 'admin/category/add',
            controller: 'CategoryFormController',
            permissions: [cmsRouteProvider.permission('AACCOUNT', 'CREATE')],
            resolve: {
                category: function ($route, categoryService) {
                    return {data: {}};
                }
            },
            template: '/templates/categoryForm.ng.html'
        });

        // Edit an existing product category
        cmsRouteProvider.when('admin.category.edit', {
            abstract: true,
            parent: 'admin.category',
            mapping: 'admin/category/edit/:id',
            controller: 'CategoryFormController',
            permissions: [cmsRouteProvider.permission('AACCOUNT', 'READ')],
            resolve: {
                category: function ($route, categoryService) {
                    return categoryService.get($route.current.params.id);
                }
            },
            template: '/templates/categoryForm.ng.html'
        });

    });

})(angular);
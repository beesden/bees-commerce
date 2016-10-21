(function (ng) {
    'use strict';

    /**
     * @ngdoc controller
     * @name cms.controller:UserFormController
     * @extends cms.common.controller:SimpleFormController
     * @ngInject
     *
     * @description User controller
     */
    ng.module('cms.controller').controller('UserController', function (SimpleFormController, userService) {

        var _self = this;

        SimpleFormController.call(_self, userService, 'admin.user');

    }).config(function (cmsRouteProvider) {

        // List all users
        cmsRouteProvider.when('admin.user', {
            icon: 'user',
            parent: 'admin',
            mapping: 'admin/user',
            permissions: [cmsRouteProvider.permission('AACCOUNT', 'READ')],
            resolve: {
                userList: function ($route, userService) {
                    return userService.getAll($route.current.params);
                }
            },
            search: true,
            template: '/templates/userList.ng.html'
        });

        // Create a new user
        cmsRouteProvider.when('admin.user.create', {
            abstract: true,
            icon: 'add',
            parent: 'admin.user',
            mapping: 'admin/user/add',
            controller: 'UserFormController',
            permissions: [cmsRouteProvider.permission('AACCOUNT', 'CREATE')],
            resolve: {
                user: function ($route, userService) {
                    return {data: {}};
                }
            },
            template: '/templates/userForm.ng.html'
        });

        // Edit an existing user
        cmsRouteProvider.when('admin.user.edit', {
            abstract: true,
            parent: 'admin.user',
            mapping: 'admin/user/edit/:id',
            controller: 'UserFormController',
            permissions: [cmsRouteProvider.permission('AACCOUNT', 'READ')],
            resolve: {
                user: function ($route, userService) {
                    return userService.get($route.current.params.id);
                }
            },
            template: '/templates/userForm.ng.html'
        });

    });

})(angular);
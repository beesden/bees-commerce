(function (ng) {
    'use strict';

    /**
     * @ngdoc controller
     * @name cms.controller:UserRoleFormController
     * @extends cms.common.controller:SimpleFormController
     * @ngInject
     *
     * @description UserRole controller
     */
    ng.module('cms.controller').controller('UserRoleController', function (SimpleFormController, userRoleService) {

        var _self = this;

        SimpleFormController.call(_self, userRoleService, 'admin.userRole');

    }).config(function (cmsRouteProvider) {

        // List all userRoles
        cmsRouteProvider.when('admin.userRole', {
            icon: 'user-role',
            parent: 'admin',
            mapping: 'admin/user-role',
            permissions: [cmsRouteProvider.permission('AACCOUNT', 'READ')],
            resolve: {
                userRoleList: function ($route, userRoleService) {
                    return userRoleService.getAll($route.current.params);
                }
            },
            search: true,
            template: '/templates/userRoleList.ng.html'
        });

        // Create a new userRole
        cmsRouteProvider.when('admin.userRole.create', {
            abstract: true,
            icon: 'add',
            parent: 'admin.userRole',
            mapping: 'admin/userRole/add',
            controller: 'UserRoleFormController',
            permissions: [cmsRouteProvider.permission('AACCOUNT', 'CREATE')],
            resolve: {
                userRole: function ($route, userRoleService) {
                    return {data: {}};
                }
            },
            template: '/templates/userRoleForm.ng.html'
        });

        // Edit an existing userRole
        cmsRouteProvider.when('admin.userRole.edit', {
            abstract: true,
            parent: 'admin.userRole',
            mapping: 'admin/userRole/edit/:id',
            controller: 'UserRoleFormController',
            permissions: [cmsRouteProvider.permission('AACCOUNT', 'READ')],
            resolve: {
                userRole: function ($route, userRoleService) {
                    return userRoleService.get($route.current.params.id);
                }
            },
            template: '/templates/userRoleForm.ng.html'
        });

    });

})(angular);
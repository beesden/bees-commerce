(function (ng) {
    'use strict';

    ng.module('cms.service').service('roleService', function ($http, SimpleCacheService) {

        var RoleService = function () {
            SimpleCacheService.call(this, {persistType: 'account'}, 'roles');
        };
        RoleService.prototype = Object.create(SimpleCacheService.prototype);

        /**
         *
         * @param id
         * @returns {*}
         */
        RoleService.prototype.getUsers = function (id) {
            return $http({
                url: this.url.build({id: id, action: 'users'})
            });
        };

        return new RoleService();

    });

})(angular);

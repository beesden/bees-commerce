(function (ng) {
    'use strict';

    ng.module('cms.service').service('userService', function ($http, SimpleCacheService) {

        var _self = this;

        SimpleCacheService.call(_self, {}, 'users', {limit: 20});

    });

})(angular);

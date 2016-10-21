(function (ng) {
    'use strict';

    ng.module('cms.service').service('productService', function ($http, SimpleService) {

        var _self = this;

        SimpleService.call(_self, 'products');

    });

})(angular);

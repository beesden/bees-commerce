(function (ng) {
    'use strict';

    ng.module('cms.common.service').factory('validationInterceptor', function ($q, $rootScope, messageService) {

        var loadCount = 0;

        return {

            request: function (request) {
                if (!request.background) {
                    $rootScope.pageLoading = ++loadCount;
                }
                return request;
            },

            response: function (response) {
                $rootScope.pageLoading = --loadCount > 0;
                return response;
            },

            /**
             * Catch response errors gracefully
             *
             * @param response
             * @returns {*}
             */
            responseError: function (rejection) {
                rejection.data = rejection.data || {};
                $rootScope.pageLoading = --loadCount > 0;

                // Form validation errors
                if (rejection.data.constructor === Array) {
                    for (var i = 0; i < rejection.data.length; i++) {
                        messageService.addMessage('error', rejection.data[i].key, rejection.data[i].params);
                    }
                } else {
                    messageService.addMessage('error', rejection.data.key);
                }

                return $q.reject(rejection);
            }
        };

    }).config(function ($httpProvider) {
        $httpProvider.interceptors.push('validationInterceptor');
    });

})
(angular);
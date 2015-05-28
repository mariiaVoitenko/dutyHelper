'use strict';

angular.module('dutyhelperApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });



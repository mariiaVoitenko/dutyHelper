'use strict';

angular.module('dutyhelperApp')
    .factory('Membership', function ($resource) {
        return $resource('api/memberships/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

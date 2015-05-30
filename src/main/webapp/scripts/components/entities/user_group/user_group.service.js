'use strict';

angular.module('dutyhelperApp')
    .factory('User_group', function ($resource) {
        return $resource('api/user_groups/:id', {}, {
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

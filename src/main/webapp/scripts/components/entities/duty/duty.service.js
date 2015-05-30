'use strict';

angular.module('dutyhelperApp')
    .factory('Duty', function ($resource) {
        return $resource('api/dutys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    if (data.start_date != null) data.start_date = new Date(data.start_date);
                    if (data.end_date != null) data.end_date = new Date(data.end_date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

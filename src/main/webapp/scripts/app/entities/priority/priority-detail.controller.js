'use strict';

angular.module('dutyhelperApp')
    .controller('PriorityDetailController', function ($scope, $stateParams, Priority) {
        $scope.priority = {};
        $scope.load = function (id) {
            Priority.get({id: id}, function(result) {
              $scope.priority = result;
            });
        };
        $scope.load($stateParams.id);
    });

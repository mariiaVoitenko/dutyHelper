'use strict';

angular.module('dutyhelperApp')
    .controller('UserDutyDetailController', function ($scope, $stateParams, Duty, Priority, Category) {
        $scope.duty = {};
        $scope.load = function (id) {
            Duty.get({id: id}, function (result) {
                $scope.duty = result;
            });
        };
        $scope.load($stateParams.id);
    });

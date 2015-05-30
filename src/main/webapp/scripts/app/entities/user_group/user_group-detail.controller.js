'use strict';

angular.module('dutyhelperApp')
    .controller('User_groupDetailController', function ($scope, $stateParams, User_group) {
        $scope.user_group = {};
        $scope.load = function (id) {
            User_group.get({id: id}, function(result) {
              $scope.user_group = result;
            });
        };
        $scope.load($stateParams.id);
    });

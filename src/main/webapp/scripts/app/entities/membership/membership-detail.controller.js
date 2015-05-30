'use strict';

angular.module('dutyhelperApp')
    .controller('MembershipDetailController', function ($scope, $stateParams, Membership, Status, User, User_group) {
        $scope.membership = {};
        $scope.load = function (id) {
            Membership.get({id: id}, function(result) {
              $scope.membership = result;
            });
        };
        $scope.load($stateParams.id);
    });

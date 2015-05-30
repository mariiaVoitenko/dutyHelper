'use strict';

angular.module('dutyhelperApp')
    .controller('AppointmentDetailController', function ($scope, $stateParams, Appointment, User, Duty) {
        $scope.appointment = {};
        $scope.load = function (id) {
            Appointment.get({id: id}, function(result) {
              $scope.appointment = result;
            });
        };
        $scope.load($stateParams.id);
    });

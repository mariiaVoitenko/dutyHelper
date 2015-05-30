'use strict';

angular.module('dutyhelperApp')
    .controller('AppointmentController', function ($scope, Appointment, User, Duty, ParseLinks) {
        $scope.appointments = [];
        $scope.users = User.query();
        $scope.dutys = Duty.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Appointment.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.appointments = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Appointment.get({id: id}, function(result) {
                $scope.appointment = result;
                $('#saveAppointmentModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.appointment.id != null) {
                Appointment.update($scope.appointment,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Appointment.save($scope.appointment,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Appointment.get({id: id}, function(result) {
                $scope.appointment = result;
                $('#deleteAppointmentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Appointment.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAppointmentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveAppointmentModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.appointment = {id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

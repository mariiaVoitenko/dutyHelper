'use strict';

angular.module('dutyhelperApp')
    .controller('UserDutyController', function ($scope, Duty, Priority, Category, Appointment, ParseLinks) {
        $scope.dutys = [];
        $scope.prioritys = Priority.query();
        $scope.categorys = Category.query();
        $scope.appointments = Appointment.query();
        $scope.page = 1;
        $scope.loadAll = function () {
            Duty.query({page: $scope.page, per_page: 20}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                console.log("entered loadAll");
                var getCurrentUser = function httpGet(theUrl) {
                    var xmlHttp = new XMLHttpRequest();
                    xmlHttp.open("GET", theUrl, false);
                    xmlHttp.send(null);
                    return xmlHttp.responseText;
                };
                $scope.currentUser = JSON.parse(getCurrentUser('/api/account'));
                var currentAppointments = [];
                console.log($scope.currentUser);
                var index;
                for (index = 0; index < $scope.appointments.length; ++index) {
                    if ($scope.appointments[index].user.id === $scope.currentUser.id) {
                        currentAppointments.push($scope.appointments[index]);
                    }
                }
                ;
                console.log(currentAppointments);
                var duties = result;
                var currentDuties = [];
                var index2;
                for (index = 0; index < duties.length; ++index) {
                    for (index2 = 0; index2 < currentAppointments.length; ++index2) {
                        if (currentAppointments[index2].duty.id === duties[index].id) {
                            currentDuties.push(duties[index]);
                            break;
                        }
                    }
                }
                console.log(currentDuties);
                $scope.dutys = currentDuties;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Duty.get({id: id}, function (result) {
                $scope.duty = result;
                $('#saveDutyModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.duty.id != null) {
                var id;
                for (var i = 0; i < $scope.prioritys.length; ++i) {
                    if ($scope.prioritys[i].name === $scope.duty.priority.name) {
                        id = $scope.prioritys[i].id;
                    }
                }
                $scope.duty.priority.id = id;
                for (var i = 0; i < $scope.categorys.length; ++i) {
                    if ($scope.categorys[i].name === $scope.duty.category.name) {
                        id = $scope.categorys[i].id;
                    }
                }
                $scope.duty.category.id = id;
                Duty.update($scope.duty,
                    function () {
                        $scope.refresh();
                    });
            } else {
                var id;
                for (var i = 0; i < $scope.prioritys.length; ++i) {
                    if ($scope.prioritys[i].name === $scope.duty.priority.name) {
                        id = $scope.prioritys[i].id;
                    }
                }
                $scope.duty.priority.id = id;
                for (var i = 0; i < $scope.categorys.length; ++i) {
                    if ($scope.categorys[i].name === $scope.duty.category.name) {
                        id = $scope.categorys[i].id;
                    }
                }
                $scope.duty.category.id = id;
                Duty.save($scope.duty,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Duty.get({id: id}, function (result) {
                $scope.duty = result;
                $('#deleteDutyConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Duty.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteDutyConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveDutyModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.duty = {
                name: null,
                description: null,
                start_date: null,
                end_date: null,
                can_change: null,
                is_done: null,
                id: null
            };
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

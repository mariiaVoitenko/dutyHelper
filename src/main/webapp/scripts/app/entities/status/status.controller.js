'use strict';

angular.module('dutyhelperApp')
    .controller('StatusController', function ($scope, Status) {
        $scope.statuss = [];
        $scope.loadAll = function() {
            Status.query(function(result) {
               $scope.statuss = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Status.get({id: id}, function(result) {
                $scope.status = result;
                $('#saveStatusModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.status.id != null) {
                Status.update($scope.status,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Status.save($scope.status,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Status.get({id: id}, function(result) {
                $scope.status = result;
                $('#deleteStatusConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Status.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteStatusConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveStatusModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.status = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

'use strict';

angular.module('dutyhelperApp')
    .controller('PriorityController', function ($scope, Priority) {
        $scope.prioritys = [];
        $scope.loadAll = function() {
            Priority.query(function(result) {
               $scope.prioritys = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Priority.get({id: id}, function(result) {
                $scope.priority = result;
                $('#savePriorityModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.priority.id != null) {
                Priority.update($scope.priority,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Priority.save($scope.priority,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Priority.get({id: id}, function(result) {
                $scope.priority = result;
                $('#deletePriorityConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Priority.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePriorityConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#savePriorityModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.priority = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

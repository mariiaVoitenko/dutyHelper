'use strict';

angular.module('dutyhelperApp')
    .controller('DutyController', function ($scope, Duty, Priority, Category, ParseLinks) {
        $scope.dutys = [];
        $scope.prioritys = Priority.query();
        $scope.categorys = Category.query();
        $scope.page = 1;

        function getName(array, id) {
            for (var index = 0; index < array.length; ++index) {
                if (array[index].id === id)
                    return array[index].name;
            }
        }

        $scope.loadAll = function () {
            Duty.query({page: $scope.page, per_page: 20}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.dutys = result;
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

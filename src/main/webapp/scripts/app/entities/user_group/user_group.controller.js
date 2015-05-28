'use strict';

angular.module('dutyhelperApp')
    .controller('User_groupController', function ($scope, User_group, ParseLinks) {
        $scope.user_groups = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            User_group.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.user_groups.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.user_groups = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            User_group.get({id: id}, function(result) {
                $scope.user_group = result;
                $('#saveUser_groupModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.user_group.id != null) {
                User_group.update($scope.user_group,
                    function () {
                        $scope.refresh();
                    });
            } else {
                User_group.save($scope.user_group,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            User_group.get({id: id}, function(result) {
                $scope.user_group = result;
                $('#deleteUser_groupConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            User_group.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteUser_groupConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveUser_groupModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.user_group = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

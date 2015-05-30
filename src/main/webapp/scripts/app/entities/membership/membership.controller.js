'use strict';

angular.module('dutyhelperApp')
    .controller('MembershipController', function ($scope, Membership, Status, User, User_group, ParseLinks) {
        $scope.memberships = [];
        $scope.statuss = Status.query();
        $scope.users = User.query();
        $scope.user_groups = User_group.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Membership.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.memberships = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Membership.get({id: id}, function(result) {
                $scope.membership = result;
                $('#saveMembershipModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.membership.id != null) {
                Membership.update($scope.membership,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Membership.save($scope.membership,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Membership.get({id: id}, function(result) {
                $scope.membership = result;
                $('#deleteMembershipConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Membership.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMembershipConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveMembershipModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.membership = {id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

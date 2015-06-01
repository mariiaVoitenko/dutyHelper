'use strict';

angular.module('dutyhelperApp')
    .controller('CertainUser_groupController', function ($scope, User_group, Membership, User, ParseLinks) {
        $scope.user_groups = [];
        $scope.users = User.query();
        $scope.memberships = Membership.query();
        $scope.page = 1;
        $scope.loadAll = function () {
            User_group.query({page: $scope.page, per_page: 20}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));

                var getCurrentUser = function httpGet(theUrl) {
                    var xmlHttp = new XMLHttpRequest();
                    xmlHttp.open("GET", theUrl, false);
                    xmlHttp.send(null);
                    return xmlHttp.responseText;
                };
                $scope.currentUser = JSON.parse(getCurrentUser('/api/account'));
                console.log($scope.currentUser);

                var memberships = $scope.memberships;
                var currentMemberships = [];

                memberships.forEach(function (membership, i, memberships) {
                    if (membership.user.id === $scope.currentUser.id) {
                        currentMemberships.push(membership);
                        console.log("pushed" + membership.user.id);
                    }
                });

                var groups = result;
                var currentGroups = [];

                var index;
                var index2;
                for (index = 0; index < groups.length; ++index) {
                    console.log("entered 1");
                    for (index2 = 0; index2 < memberships.length; ++index2) {
                        console.log("entered 2");
                        if (memberships[index2].user_group.id === groups[index].id) {
                            console.log("entered if");
                            currentGroups.push(groups[index]);
                            console.log("pushed" + currentGroups[index].id);
                            break;
                        }
                    }
                }
                $scope.user_groups = currentGroups;
                currentGroups = [];
                currentMemberships = [];
                $scope.currentUser = [];
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            User_group.get({id: id}, function (result) {
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
            User_group.get({id: id}, function (result) {
                $scope.user_group = result;
                $('#deleteUser_groupConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            User_group.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteUser_groupConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveUser_groupModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.user_group = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

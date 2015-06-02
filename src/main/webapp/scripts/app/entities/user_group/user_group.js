'use strict';

angular.module('dutyhelperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('user_group', {
                parent: 'entity',
                url: '/user_group',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyhelperApp.user_group.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/user_group/user_groups.html',
                        controller: 'User_groupController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user_group');
                        return $translate.refresh();
                    }]
                }
            })
            .state('user_groupForUser', {
                parent: 'entity',
                url: '/user_group/bla',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyhelperApp.user_group.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/user_group/certain-user_groups.html',
                        controller: 'CertainUser_groupController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user_group');
                        return $translate.refresh();
                    }]
                }
            })
            .state('user_groupDetail', {
                parent: 'entity',
                url: '/user_group/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyhelperApp.user_group.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/user_group/user_group-detail.html',
                        controller: 'User_groupDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user_group');
                        return $translate.refresh();
                    }]
                }
            })
            .state('certain-user_groupDetail', {
                parent: 'entity',
                url: '/user_group/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyhelperApp.user_group.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/user_group/certain-user_group-detail.html',
                        controller: 'CertainUser_groupDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user_group');
                        return $translate.refresh();
                    }]
                }
            });
        ;

    });

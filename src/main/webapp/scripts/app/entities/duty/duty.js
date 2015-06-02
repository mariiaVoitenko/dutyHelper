'use strict';

angular.module('dutyhelperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('duty', {
                parent: 'entity',
                url: '/duty',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyhelperApp.duty.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/duty/dutys.html',
                        controller: 'DutyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('duty');
                        return $translate.refresh();
                    }]
                }
            })
            .state('dutyDetail', {
                parent: 'entity',
                url: '/duty/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyhelperApp.duty.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/duty/duty-detail.html',
                        controller: 'DutyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('duty');
                        return $translate.refresh();
                    }]
                }
            })
            .state('dutyForUser', {
                parent: 'entity',
                url: '/duty/certain',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyhelperApp.duty.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/duty/user-dutys.html',
                        controller: 'UserDutyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('duty');
                        return $translate.refresh();
                    }]
                }
            })
            .state('dutyDetailForUser', {
                parent: 'entity',
                url: '/duty/certain/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyhelperApp.duty.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/duty/user-duty-detail.html',
                        controller: 'UserDutyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('duty');
                        return $translate.refresh();
                    }]
                }
            });
    });

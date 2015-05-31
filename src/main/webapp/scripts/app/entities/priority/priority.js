'use strict';

angular.module('dutyhelperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('priority', {
                parent: 'entity',
                url: '/priority',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'dutyhelperApp.priority.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/priority/prioritys.html',
                        controller: 'PriorityController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('priority');
                        return $translate.refresh();
                    }]
                }
            })
            .state('priorityDetail', {
                parent: 'entity',
                url: '/priority/:id',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'dutyhelperApp.priority.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/priority/priority-detail.html',
                        controller: 'PriorityDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('priority');
                        return $translate.refresh();
                    }]
                }
            });
    });

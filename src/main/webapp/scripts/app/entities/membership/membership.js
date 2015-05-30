'use strict';

angular.module('dutyhelperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('membership', {
                parent: 'entity',
                url: '/membership',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyhelperApp.membership.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/membership/memberships.html',
                        controller: 'MembershipController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('membership');
                        return $translate.refresh();
                    }]
                }
            })
            .state('membershipDetail', {
                parent: 'entity',
                url: '/membership/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyhelperApp.membership.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/membership/membership-detail.html',
                        controller: 'MembershipDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('membership');
                        return $translate.refresh();
                    }]
                }
            });
    });

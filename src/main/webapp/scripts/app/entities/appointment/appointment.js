'use strict';

angular.module('dutyhelperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('appointment', {
                parent: 'entity',
                url: '/appointment',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyhelperApp.appointment.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/appointment/appointments.html',
                        controller: 'AppointmentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('appointment');
                        return $translate.refresh();
                    }]
                }
            })
            .state('appointmentDetail', {
                parent: 'entity',
                url: '/appointment/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyhelperApp.appointment.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/appointment/appointment-detail.html',
                        controller: 'AppointmentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('appointment');
                        return $translate.refresh();
                    }]
                }
            });
    });

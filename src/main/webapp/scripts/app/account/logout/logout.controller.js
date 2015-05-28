'use strict';

angular.module('dutyhelperApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });

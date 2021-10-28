//var app = angular.module('iw3',[]);
var app = angular.module('iw3',[
    'ngRoute','productos','ui.bootstrap','ngStorage','oitozero.ngSweetAlert']);

//aca se crean las cte en angular
app.constant('URL_BASE','http://localhost:8080');

app.config(function($localStorageProvider){ // le agrego iw3 a la variable de localStore
    $localStorageProvider.setKeyPrefix('iw3/');
});


//la dependencia $uibModal abre y configura los modales no los cierra
app.run(['$rootScope','$uibModal','CoreService','$location','$log','$localStorage',
    function($rootScope, $uibModal, CoreService, $location, $log, $localStorage) {

        //$rootScope.stomp=$stomp;

        //esta funcion tiene cambiar la url del lado del cliente(osea del # a la derecha cambio)
        $rootScope.relocate = function(loc) {
            $rootScope.oldLoc=$location.$$path; //guardo la url actual
            $location.path(loc);    //modifico la url a la que deseo ir, y le paso como parametro lo el directorio al que voy por ejemplo: productos
        };

        $rootScope.userData=function() {
            return $localStorage.userdata;
        };

        $rootScope.logout=function() {
            CoreService.logout();
        };

        $rootScope.openLoginForm = function(size) {
            if (!$rootScope.loginOpen) {    //si el modal no esta abierto ya
                //$rootScope.cleanLoginData();
                $rootScope.loginOpen = true;
                $uibModal.open({
                    animation : true,
                    backdrop : 'static',
                    keyboard : false,
                    templateUrl : 'ui/vistas/login.html',
                    controller : 'LoginController',
                    size : size//,
                    //resolve : {
                    //	user : function() {
                    //		return $rootScope.user;
                    //	}
                    //}
                });
            }
        };
        //ver porque no anda
        //$rootScope.openLoginForm();

        CoreService.authInfo(); //llamo al interceptor

    }
]);


/*
app.controller('controladorDiv2',function ($scope){
    $scope.titulo = "EStoy en mi controler que me devuelve una referencia y es mi controller 1"
});

angular.module('iw3').controller('controladorDiv1',function ($scope){
    $scope.titulo = "Esto en mi controller 1"
    $scope.datos = {size:10}
});*/



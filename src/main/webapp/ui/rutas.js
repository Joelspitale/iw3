angular.module('iw3').config(($routeProvider, $locationProvider)=>{
    $locationProvider.hashPrefix('!');
    $routeProvider
        .when('/main',{
            templateUrl: 'ui/vistas/main.html',
            controller: 'Main'
        })
        .when('/productos',{
            templateUrl: 'ui/modulos/productos/productos.html',
            controller: 'Productos'
        })
        .otherwise({
            redirectTo: '/main'
        });

});
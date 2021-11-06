angular.module('iw3').config(($routeProvider, $locationProvider, $httpProvider)=>{
    $locationProvider.hashPrefix('!');
    $httpProvider.interceptors.push('APIInterceptor');
    $routeProvider
        .when('/main',{
            templateUrl: 'ui/vistas/main.html',
            controller: 'Main'
        })
        .when('/productos',{
            templateUrl: 'ui/modulos/productos/productos.html',
            controller: 'Productos'
        })
        .when('/graficos',{
            templateUrl : 'ui/modulos/graficos/graficos.html',
            controller: 'GraficosController'
        })
        .otherwise({
            redirectTo: '/main'
        });

});
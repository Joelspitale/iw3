angular.module('iw3')   // q es el manejador de promesas(puedo crear promesas) , el location manejo y me da la url actual
.factory('APIInterceptor', function($q,$rootScope,$localStorage, $location) {
    return {
      request: function (config) {  ////el interceptor agrega a todos los request un token de autorizacion
          //si estoy logueado y tengo credenciales validas
            if($localStorage.logged && $localStorage.userdata){
                 var userdata=$localStorage.userdata;
                 config.headers['X-AUTH-TOKEN'] = userdata.authtoken;
            }else{
                //sino estoy logueado abro el formulario para que se loguee
            	$rootScope.openLoginForm();
            }
        	return config || $q.when(config);   // angular me exige que devuelva una promesa
                                                // config ya es una promesa o armo una promesa con when
      },
        //si hay un error le exigo que se loguee el usuario
     responseError: function(response) {
        if(response.status==401 || response.status==403){
            //abro el formulario si no esta abierto sino no hago nada y vuelvo la promesa que indica un error
        	$rootScope.openLoginForm();
        }
        return $q.reject(response); //promeso reject que lo primero que ejecuta cuando hay un error
                                    //en cambio el when ejecuta una promesa donde lo primero es es caso feliz y el segundo el error
      }
    };
  })
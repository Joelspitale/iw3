angular.module('iw3').factory('CoreService',function($http,URL_BASE,$log,$localStorage){
	return {
		//es el modelo que usa la vista, en el que el usuario escribe sus credenciales
		login: function(user) {
			//creo el request para obtener el token de autenticacion
			const config={
				method:'POST',
				url: URL_BASE+'/login-json',
				headers : { 'Content-Type': 'application/x-www-form-urlencoded' },
				data: `username=${user.name}&password=${user.password}`
				//data: 'username='+user.name+'&password'+user.password
			};
			return $http(config);		//le paso un objeto json
		},
		authInfo:function(){			// es un metodo protegido, osea me refiero que sino estoy legueado me devuelve un 403
			//$log.log()				// si estoy logueado me devuelve el usuario que esta logueado
			return $http.get(URL_BASE+"/auth-info");
		},
		logout: function() {
			delete $localStorage.userdata;
			$localStorage.logged=false;
			$http.get(URL_BASE+"/auth-info");
		}
	};
});
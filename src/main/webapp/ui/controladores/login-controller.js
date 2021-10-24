angular.module('iw3').controller('LoginController',
		function (											//la dependencia $uibModalInstance es el que cierra los modales no los abre
				$rootScope, $scope, $localStorage,
				$uibModalInstance, SweetAlert,
				CoreService,$log) {
			$scope.title="Ingreso";
			
			$scope.user={name:"",password:""};
			

			$scope.login = function() {
				CoreService.login($scope.user).then(	//promesa
					function(resp){ 		//si se loguea correctamente
						if(resp.status===200) {				//si es correcto guardo all el json que recibo del server en el localStore
							$localStorage.userdata=resp.data;
							$localStorage.logged=true;		//seteo la bandera que me indica que estoy logueado correctamente
							$rootScope.loginOpen = false;	//seteo la flag que despues me sirve para no volver a mostrar la pantalla de loguin
							$uibModalInstance.dismiss(true);	//cierro el modal del inicio de sesion
						}else{
							delete $localStorage.userdata;	//si se logueo mal borro toda la informacion de inicion de sesion en el localstore
							$localStorage.logged=false;
							SweetAlert.swal( "Problemas autenticando",resp.data, "error");
						}
					},
					function(respErr){		//si se loguea mal
						$log.log(respErr);
						SweetAlert.swal( "Problemas autenticando",respErr.data, "error");
					}
				);
			  };  
		}); //End LoginFormController
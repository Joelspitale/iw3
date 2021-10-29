angular.module('productos').controller('Productos',
    function ($scope,ProductosService,$uibModal,SweetAlert,Notification){
        $scope.titulo = 'Productos';
        //$scope.textoBuscado = 'le';

        $scope.verItemsPorPagina = 3;
        $scope.totalDeItems = 0;
        $scope.paginaActual = 1;
        $scope.itemsPorPagina = $scope.verItemsPorPagina;

        //tengo que implementar que se pueda ingresar la cantidad de items por pagina y que se llama a esta funcion que me ayuda a hacer
        //lo tengo que llamar desde productos.html
        $scope.setItemsPorPagina = function(num) {
            $scope.itemsPorPagina = num;
            $scope.paginaActual = 1;
            };//End setItemsPorPagina()
        $scope.lista= [];

        //encapsulo la funcion para listar los productos en una variable para llamarle desde cualquier parte
        $scope.refresh = function() {
               //me devuelve una promesa, entonces tengo que contemplar ambos casos en que una promesa se ejecute bien o mal
               ProductosService.lista().then(
                   function(resp){
                       if(resp.status==200) { //el servidor me devolvio un 200 osea que recibi la lista de productos
                           $scope.lista=resp.data;
                           $scope.totalDeItems = $scope.lista.length;
                       }
                       console.log(resp);
                   },
                   function(err){ //en caso de que la promesa se ejecute mal
                       console.log(err);
                   }
               );
        }; //End refresh

        $scope.refresh();

        $scope.addEdit =  function (p){
            var modalInstance = $uibModal.open({
                animation: true,
                backdrop: false,
                controllerAs: '$ctrl',
                size: 'large',
                controller: 'AgregaEditaProductoModal', //aca le tenemos que colocar el controler que manejara la logica
                templateUrl: 'ui/modulos/productos/add-edit-productos-modal.html',  //coloco la vista
                resolve: {  //le enviamos el objeto producto p al controlador, siempre como una copia y no como referencia
                    producto: angular.copy(p)
                }
            });
            //pregunto que boton presiono el usuario para ver los resultado tengo que manejarlo con promesas
            //en el que pregunto que boton apreto el usuario, recibe como parametro 2 funciones
            modalInstance.result.then(
                function(instancia){    //en caso que me presione el boton tengo que mostrar los datos del producto
                    if(!p) { //Agregar  --> si es falso entonces agrego porque en js cuando es !=0 | not(null) | !=false entonces es verdadero
                        ProductosService.add(instancia).then( //hago la promesa
                            function(resp) { //caso en que salio bien
                                $scope.refresh(); //refresco la lista que muestro
                                Notification.success({message:'El producto se agregó correctamente',title:'Operación existosa!'});
                                //SweetAlert.swal( "El producto se agregó correctamente", "Agregado Ok");
                            },
                            function(err) {
                                Notification.error({message:'No se ha podido agregar el producto',title:'Operación fallida!'});
                                //SweetAlert.swal( "No se ha podido agregar el producto",err, "error");
                            }
                        );
                    } else { //Editar
                        ProductosService.edit(instancia).then(  //la promesa recibe una variable que contenga el objeto que se va a modificar y le tengo que colocar exactamente el mismo nombre de variable en el controller y devolverla
                            function(resp) {
                                $scope.refresh();
                                Notification.success({message:'El producto se modificó correctamente',title:'Operación existosa!'});
                            },
                            function(err) {
                                Notification.error({message:'No se ha podido modificar el producto',title:'Operación fallida!'});
                            }
                        );
                    }
                },
                function(){} //funcion dismiss osea cuando no me presiona nada
            );
            } // End addEdit

            $scope.remove=function(p) {
                SweetAlert.swal({
                    title: "Eliminar producto",
                    text: "Está segur@ que desea eliminar el producto <strong>"+p.descripcion+"</strong>?",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "Si, eliminar producto!",
                    cancelButtonText: "No",
                    closeOnConfirm: true,
                    html: true //le indico que el text tiene etiquetas html
                }, function(confirm){
                    if(confirm) {
                        ProductosService.remove(p).then(
                            function(resp) {
                                $scope.refresh();
                                Notification.success({message:'El producto se ha eliminado',title:'Operación existosa!'});
                            },
                            function(err) {
                                Notification.error({message:'No se ha podido eliminar el producto',title:'Operación fallida!'});
                            }
                        );
                    }
                });
            };// End remove
        });


    //controlador que uso cuando estoy en el modal abierto
    angular.module('productos').controller('AgregaEditaProductoModal',
        function($uibModalInstance, producto, ProductosService){
            console.log(producto)
            var $ctrl=this;
            $ctrl.agregar=!producto;    //obtengo mi bandera para ver si agrego o tengo que editar

            console.log('=>',$ctrl.agregar)

            //pregunto si la variable producto es no true, sea si es false osea si tengo que crear un producto y creo uno vacio
            if(!producto) {
                producto={rubro: null ,descripcionExtendida:'', descripcion:'', precio: 0.0, enStock:false};
            }

            $ctrl.instancia=producto;   //para poder manejar objetos y variable de la vista en el controller tengo que tenerlas instanciadas en el controller

            console.log('==>',$ctrl.instancia)

            //devuelvo el objeto instancia que ya tiene cargado los datos del producto dentro suyo,
            //se cumple la promesa del edit con el caso feliz
            $ctrl.ok=function() {
                $uibModalInstance.close($ctrl.instancia);
            };
            $ctrl.volver=function() {
                $uibModalInstance.dismiss();//cierro el modal
            };

            //chequeo las codiciones necesarias para que el boton "guardar" se habilite
            $ctrl.verGuardar=function() {
                return $ctrl.instancia.descripcion.length>2 && $ctrl.instancia.precio>0;
            };

            $ctrl.rubros=[];
            ProductosService.listaRubros().then(
                function(resp){
                    if(resp.status==200) {
                        $ctrl.rubros=resp.data;
                    }
                    console.log(resp);
                },
                function(err){
                    console.log(err);
                }
            );
    });

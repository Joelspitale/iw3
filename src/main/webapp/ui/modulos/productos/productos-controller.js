angular.module('productos').controller('Productos',
    function ($scope,ProductosService,$uibModal){
    $scope.titulo = 'Productos';
    //$scope.textoBuscado = 'le';
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
    });
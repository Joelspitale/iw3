//var app = angular.module('iw3',[]);
var app = angular.module('iw3',['ngRoute','productos']);

app.controller('controladorDiv2',function ($scope){
    $scope.titulo = "EStoy en mi controler que me devuelve una referencia y es mi controller 1"
});

angular.module('iw3').controller('controladorDiv1',function ($scope){
    $scope.titulo = "Esto en mi controller 1"
    $scope.datos = {size:10}
});



angular.module('CrudApp', []).config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
        when('/', {templateUrl: '/views/lists.html', controller: ListCtrl}).
        otherwise({redirectTo: '/'});
}]);

function ListCtrl($scope, $http,$location,$routeParams) {
    $http.get('/api/imageList').success(function (data) {
        $scope.images = data;
    });
}


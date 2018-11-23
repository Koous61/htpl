let app = angular.module("app", []);

app.controller('MainController', ($scope, $http) => {

	$scope.code = null;
	$scope.result = null;

	// init function
	(() => {
	})();

	$scope.execute = () => {
		$http({
			method: 'POST',
			url: '/execute',
			data: $scope.code
		}).then((response) => {
			$scope.result = response.data;
		}, (error) => {
			// handle error
		})
	}

});
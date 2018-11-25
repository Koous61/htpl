let app = angular.module("app", ['ui.ace']);

app.controller('MainController', ($scope, $http) => {

	$scope.code = null;
	$scope.result = null;

	// init function
	(() => {
		$scope.code = "<htpl>\n\n</htpl>";
		let editor = ace.edit("editor");
		editor.setOptions({
			fontSize: "20px"
		});
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
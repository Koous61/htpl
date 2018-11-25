let app = angular.module("app", ['ui.ace']);

app.controller('MainController', ($scope, $http) => {

	$scope.code = null;
	$scope.result = null;

	$scope.execute = () => {
		$http({
			method: 'POST',
			url: '/execute',
			data: $scope.code,
			transformResponse: undefined
		}).then((response) => {
			$scope.isError = false;
			$scope.result = response.data;
		}, (error) => {
			$scope.isError = true;
			$scope.result = JSON.parse(error.data).message;
		})
	};

	// init function
	(() => {
		$scope.code = "<htpl>\n" +
			"    <call name=\"print\">\n" +
			"        <const value=\"'Hello world!'\"></const>\n" +
			"    </call>\n" +
			"</htpl>";
		let editor = ace.edit("editor");
		editor.setOptions({
			fontSize: "20px"
		});

		$scope.execute();
	})();

});
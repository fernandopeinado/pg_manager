(function() {

	var mainApp = angular.module('mainApp', [ 'ngRoute', 'cas10.components', 'cas10.pgman.dashboard', 'cas10.pgman.topqueries' ]);

	mainApp.controller('MainCtrl', function($scope, $window) {
		$window.mainScope = $scope;
		$scope.currentView = '';
		$scope.activeView = function(view) {
			if (view == $scope.currentView) {
				return "active";	
			}
			return "";
		};
	});

	mainApp.controller('HomeCtrl', function($scope, $window) {
		$window.mainScope.currentView = 'Home';
	});

	mainApp.controller('AdministrationCtrl', function($scope, $window) {
		$window.mainScope.currentView = 'Administration';
	});

	mainApp.controller('ReportsCtrl', function($scope, $window) {
		$window.mainScope.currentView = 'Reports';
	});

	mainApp.config(function($routeProvider) {
		$routeProvider.when('/Home', {
			templateUrl : 'home.html',
			controller : 'HomeCtrl'
		}).when('/Dashboard/DbSize/:database', {
			templateUrl : 'dbsize.html',
			controller : 'DbSizeCtrl'
		}).when('/Dashboard', {
			templateUrl : 'dashboard.html',
			controller : 'DashboardCtrl'
		}).when('/TopQueries', {
			templateUrl : 'topqueries.html',
			controller : 'TopQueriesCtrl'
		}).when('/Administration', {
			templateUrl : 'administration.html',
			controller : 'AdministrationCtrl'
		}).when('/Reports', {
			templateUrl : 'reports.html',
			controller : 'ReportsCtrl'
		}).otherwise({
			redirectTo : '/Home'
		});
	});
	
})();


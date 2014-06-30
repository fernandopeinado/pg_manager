(function() {

	var module = angular.module('cas10.components', []);

	module.directive('cas10Table', function() {
	return {
		restrict: 'E',
		transclude: true,
		scope: {
			title: '=title',
			matrix: '=matrix'
		},
	    templateUrl: 'directives/castable.html'
	  };
	});
	
})();


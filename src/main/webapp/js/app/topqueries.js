(function () {

  var module = angular.module('cas10.pgman.topqueries', []);

  /* Dashboard */
  module.controller('TopQueriesCtrl', function ($scope, $window, $http, $interval, $location) {
    $window.mainScope.currentView = 'TopQueries';

    $scope.refreshTopSql = function () {
      $http.get('ws/topqueries', {responseType: "json"})
          .success(function (data, status) {
            $scope.time = data;
          });

      $http.get('topsql.groovy', {responseType: "json"})
          .success(function (data, status) {
            for (prop in data) {
              $scope[prop] = data[prop];
            }
          });
    };

    $scope.refreshTopSql();

    $interval(function () {
      $scope.refreshTopSql();
    }, 10000);

  });

})();

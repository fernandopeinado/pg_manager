(function () {

  var module = angular.module('cas10.pgman.topqueries', []);

  /* Dashboard */
  module.controller('TopQueriesCtrl', function ($scope, $window, $http, $interval, $location) {

    function dateString(dateStr) {
      var date = new Date(dateStr);
      return '' + date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
    }

    $scope.$watch('graph', function (newValue) {
      framework.plotter.plot('graph', $scope.graph, plotGraphCPU);
    });

    $scope.loadHist = function () {      
      var offsetInMillis = new Date().getTimezoneOffset() * 60000;
      var start = new Date(new Date($scope.start_year + '-' + $scope.start_month + '-' + $scope.start_date + 'T' + $scope.start_hour + ':' + $scope.start_minute + ':00.000Z').getTime() + offsetInMillis);
      var end = new Date(new Date($scope.end_year + '-' + $scope.end_month + '-' + $scope.end_date + 'T' + $scope.end_hour + ':' + $scope.end_minute + ':00.000Z').getTime() + offsetInMillis);
      $scope.refreshTopSqlPeriod(start, end);
      $scope.histHoaded = true;
    }

    $scope.refreshTopSql = function () {
      var end = new Date();
      var start = new Date(end.getTime() - 3600000);
      $scope.refreshTopSqlPeriod(start, end);
    }

    $scope.refreshTopSqlPeriod = function (start, end) {
      $http.get('ws/topqueries/period/' + start.toISOString() + '/' + end.toISOString() + '.json', {responseType: "json"})
          .success(function (data, status) {
            var graph = {data: [[], [], []], labels: [{label: 'CPU'}, {label: 'READ'}, {label: 'WRITE'}], minTime: '2013-10-10 10:00:00', maxTime: '2013-10-10 10:00:00'};
            var sqls = {};
            var tableTopSQL = {matrixData: [["database", "total_time", "blk_read_time", "blk_write_time", "avg_time", "calls", "rows", "query"]], title: "Top SQLs"}
            if (data.length > 0) {
              graph.minTime = dateString(data[0].end);
              graph.maxTime = dateString(data[data.length - 1].end);
            }
            for (var i = 0; i < data.length; i++) {
              graph.data[0].push([dateString(data[i].end), data[i].normalizedTotalTime - data[i].normalizedReadTime - data[i].normalizedWriteTime]);
              graph.data[1].push([dateString(data[i].end), data[i].normalizedReadTime]);
              graph.data[2].push([dateString(data[i].end), data[i].normalizedWriteTime]);
            }
            $scope.time = data;
            $scope.graph = graph;
          });

      $http.get('ws/topqueries/consolidate/' + start.toISOString() + '/' + end.toISOString() + '.json', {responseType: "json"})
          .success(function (data, status) {
            var tableTopSQL = {
              title: "Top SQLs",
              matrixData: [["database", "total_time", "blk_read_time", "blk_write_time", "avg_time", "calls", "rows", "query"]]
            }
            for (var i in data) {
              var q = data[i];
              tableTopSQL.matrixData.push([q.database, q.totalTime, q.blkReadTime, q.blkWriteTime, q.avgTime, q.calls, q.rows, q.query]);
            }
            $scope.tableTopSQL = tableTopSQL;
          });
    };

    function plotGraphCPU(target, matrixData) {
      return $.jqplot(target, matrixData.data, {
        series: matrixData.labels,
        seriesColors: ["#33CC33", "#99CCFF", "#FF3333"],
        stackSeries: true,
        showMarker: false,
        seriesDefaults: {
          shadow: false,
          fill: true
        },
        axes: {
          yaxis: {
            min: 0
          },
          xaxis: {
            renderer: $.jqplot.DateAxisRenderer,
            labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
            tickRenderer: $.jqplot.CanvasAxisTickRenderer,
            tickOptions: {
              angle: 45,
              formatString: '%H:%M:%S',
              fontSize: '8pt'
            },
            min: matrixData.minTime,
            max: matrixData.maxTime,
            numberTicks: 10
          }
        },
        title: {
          text: 'Wait'
        },
        legend: {
          show: true,
          location: 'ne',
          placement: 'outside'
        },
        grid: {
          drawBorder: false,
          shadow: false,
          background: '#FFFFFF',
          gridLineColor: '#E5E5E5'
        }
      });
    }

    $scope.init = function () {
      $window.mainScope.currentView = 'TopQueries';
      $scope.refreshTopSql();
      $interval(function () {
        $scope.refreshTopSql();
      }, 60000);
    }

    function zeroLeftPad(val) {
      var x = val + '';
      if (x.length == 1) {
        return '0' + x;
      }
      return x;
    }

    $scope.initHist = function () {
      $window.mainScope.currentView = 'TopQueriesHist';
      var end = new Date();
      var start = new Date(end.getTime() - 3600000);
      $scope.start_year = start.getFullYear();
      $scope.start_month = zeroLeftPad(start.getMonth() + 1);
      $scope.start_date = zeroLeftPad(start.getDate());
      $scope.start_hour = zeroLeftPad(start.getHours());
      $scope.start_minute = zeroLeftPad(start.getMinutes());

      $scope.end_year = end.getFullYear();
      $scope.end_month = zeroLeftPad(end.getMonth() + 1);
      $scope.end_date = zeroLeftPad(end.getDate());
      $scope.end_hour = zeroLeftPad(end.getHours());
      $scope.end_minute = zeroLeftPad(end.getMinutes());

    };

  });

})();

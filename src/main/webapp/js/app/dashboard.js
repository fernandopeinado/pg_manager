(function() {
	
	var module = angular.module('cas10.pgman.dashboard', []);
	
	/* Dashboard */
	module.controller('DashboardCtrl', function($scope, $window, $http, $interval) {
		$window.mainScope.currentView = 'Dashboard';
		
		// CPU
		$scope.$watch('graphCPU', function(newValue, oldValue) {
			framework.plotter.plot('dashboard_cpu', $scope.graphCPU, plotGraphCPU);
		});
		
		// Memory
		$scope.$watch('graphMemory', function(newValue, oldValue) {
			framework.plotter.plot('dashboard_memory', $scope.graphMemory, plotGraphMemory);
		});
		
		// Stats Graph
		$scope.$watch('graphCommit', function(newValue, oldValue) {
			framework.plotter.plot('dashboard_stats_commit', $scope.graphCommit, plotGraphStats);
		});

		$scope.$watch('graphRollback', function(newValue, oldValue) {
			framework.plotter.plot('dashboard_stats_rollback', $scope.graphRollback, plotGraphStats);
		});

		$scope.$watch('graphBlksread', function(newValue, oldValue) {
			framework.plotter.plot('dashboard_stats_blksread', $scope.graphBlksread, plotGraphStats);
		});

		$scope.$watch('graphBlkshit', function(newValue, oldValue) {
			framework.plotter.plot('dashboard_stats_blkshit', $scope.graphBlkshit, plotGraphStats);
		});

		$scope.$watch('graphTupreturned', function(newValue, oldValue) {
			framework.plotter.plot('dashboard_stats_tupreturned', $scope.graphTupreturned, plotGraphStats);
		});	

		$scope.$watch('graphTupfetched', function(newValue, oldValue) {
			framework.plotter.plot('dashboard_stats_tupfetched', $scope.graphTupfetched, plotGraphStats);
		});	

		$scope.$watch('graphTupinserted', function(newValue, oldValue) {
			framework.plotter.plot('dashboard_stats_tupinserted', $scope.graphTupinserted, plotGraphStats);
		});	
		
		$scope.$watch('graphTupupdated', function(newValue, oldValue) {
			framework.plotter.plot('dashboard_stats_tupupdated', $scope.graphTupupdated, plotGraphStats);
		});	
		
		$scope.$watch('graphTupdeleted', function(newValue, oldValue) {
			framework.plotter.plot('dashboard_stats_tupdeleted', $scope.graphTupdeleted, plotGraphStats);
		});	
		
		$scope.refreshDashboard = function() {
			$http.get('dashboard.groovy', {responseType:"json"})
	        .success(function(data, status) {
	        	for (prop in data) {
	        		$scope[prop] = data[prop];	
	        	}
	        });
			
			$scope.refreshTopSql();
		}

		$scope.resetTopSql = function() {
			$http.get('topsqlReset.groovy', {responseType:"json"})
			.success(function(data, status) {
	        	$scope.refreshTopSql();
	        	alert(data.message);
	        });
		};

		$scope.refreshTopSql = function() {
			$http.get('topsql.groovy', {responseType:"json"})
	        .success(function(data, status) {
	        	for (prop in data) {
	        		$scope[prop] = data[prop];	
	        	}
	        });
		};
		
		$scope.refreshDashboard();
		
		$interval(function() {
			$scope.refreshDashboard();
		}, 10000);

	});

	function plotGraphCPU(target, matrixData) {
		var cpu = framework.timedSeries.decompose(matrixData);
	    return $.jqplot(target, cpu.data, {
	    	seriesColors: [ "#33CC33", "#FFFF4D", "#FF3333", "#99CCFF" ],
			stackSeries: true,
	       	showMarker: false,
	       	seriesDefaults: {
	       		shadow: false,
				fill: true
	       	},
	       	series: cpu.labels,
	       	axes: {
	    		yaxis: {
	    			min: 0, 
	            	max: 100,
	            	tickInterval: 10
	    	  	},
	           	xaxis: {
	            	renderer: $.jqplot.DateAxisRenderer,
	            	labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
	                tickRenderer: $.jqplot.CanvasAxisTickRenderer,
	                tickOptions: {
	                    angle: 45,
	                    formatString:'%H:%M:%S',
	                    fontSize: '8pt'
	                },
	            	min: cpu.minTime, 
	            	max: cpu.maxTime,
	            	numberTicks: 10
	           	}
	       	},
	       	title: {
	       		text: 'CPU'
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

	function plotGraphMemory(target, data) {
		var memory = framework.timedSeries.decompose(data.matrixData);
	    return $.jqplot(target, memory.data, {
	    	seriesColors: [ '#009900', '#1FFF00', '#B6FFC4', '#CCCCCC' ],
			stackSeries: true,
	       	showMarker: false,
	       	seriesDefaults: {
	       		shadow: false,
				fill: true
	       	},
	       	series: memory.labels,
	       	axes: {
	    		yaxis: {
	    			min: 0,
	    			max: data.totalMemory,
	    			numberTicks: 10
	    	  	},
	           	xaxis: {
	            	renderer: $.jqplot.DateAxisRenderer,
	            	labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
	                tickRenderer: $.jqplot.CanvasAxisTickRenderer,
	            	tickOptions: {
	                    angle: 45,
	                    formatString:'%H:%M:%S',
	                    fontSize: '8pt'
	                },
	            	min: memory.minTime, 
	            	max: memory.maxTime,
	            	numberTicks: 10
	           	}
	       	},
	       	title: {
	       		text: 'Memory'
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

	function plotGraphStats(target, data) {
		var root = framework.timedSeries.decompose(data.matrixData);
	    return $.jqplot(target, root.data, {
	    	stackSeries: true,
	       	showMarker: false,
	       	seriesDefaults: {
	       		shadow: false,
				fill: true
	       	},
	       	series: root.labels,
	       	axes: {
	    		yaxis: {
	    			min: 0,
	    			numberTicks: 5
	    	  	},
	           	xaxis: {
	            	renderer: $.jqplot.DateAxisRenderer,
	            	labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
	                tickRenderer: $.jqplot.CanvasAxisTickRenderer,
	            	tickOptions: {
	                    angle: 90,
	                    formatString:'%H:%M:%S',
	                    fontSize: '7pt'
	                },
	            	min: root.minTime, 
	            	max: root.maxTime,
	            	numberTicks: 10
	           	}
	       	},
	       	title: {
	       		text: data.title
	       	},
	       	legend: {
	            show: data.legend,
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
})();

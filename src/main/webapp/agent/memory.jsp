<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<t:graph-portlet title="Dashboard">
	<jsp:attribute name="content">
		<div style="padding-left: 20px; padding-right: 150px">
			<div id="mem_chart_div" style="width: 100%; height: 240px;"></div>
		</div>
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		<script type="text/javascript">
			var matrixData = [[ 'timestamp', 'Used', 'Buffer', 'Cache' ]
				<c:forEach var="snap" items="${snapshots}">
					<c:if test="${snap.deltaObs.used != null}">
						, [ '${snap.dateTime}', ${snap.observations.used}, ${snap.observations.buffer}, ${snap.observations.cache} ]
					</c:if>
				</c:forEach> ];
			var totalMemory = ${snapshots[0].observations.total};
			
			$(document).ready(function(){
				var data = [];
				var labels = [];
				var minTime = matrixData[1][0];
				var maxTime = matrixData[matrixData.length-1][0];
				console.log(maxTime);
				if (matrixData && matrixData[0]) {
					var r, c, rows = cols = matrixData.length, cols = matrixData[0].length;
					for (c = 1; c < cols; c++) {
						labels[c-1] = { label: matrixData[0][c] };						
					}
					for (r = 1; r < rows; r++) {
						for (c = 1; c < cols; c++) {
							if (!data[c-1]) {
								data[c-1] = [];
							}
							data[c-1][r-1] = [matrixData[r][0], matrixData[r][c]];
						}
					} 
				}
				console.dir(data);
			    var plot1b = $.jqplot('mem_chart_div',data,{
			    	seriesColors: [ '#009900', '#1FFF00', '#B6FFC4', '#CCCCCC' ],
					stackSeries: true,
			       	showMarker: false,
			       	seriesDefaults: {
			       		shadow: false,
						fill: true
			       	},
			       	series: labels,
			       	axes: {
			    		yaxis: {
			    			min: 0,
			    			max: totalMemory,
			    			numberTicks: 10
			    	  	},
			           	xaxis: {
			            	renderer: $.jqplot.DateAxisRenderer,
			            	tickOptions:{formatString:'%H:%M:%S'},
			            	min: minTime, 
			            	max: maxTime,
			            	numberTicks: 15
			            	//tickInterval:'2 minutes'
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
			});
		</script>
	</jsp:attribute>
</t:graph-portlet>
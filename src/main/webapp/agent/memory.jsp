<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>

<t:graph-portlet title="Dashboard">
	<jsp:attribute name="content">
		<div style="padding-left: 20px; padding-right: 150px">
			<div id="mem_chart_div" style="width: 100%; height: 280px;"></div>
		</div>
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		<script type="text/javascript">
			var matrixData = [[ 'timestamp', 'Used', 'Buffer', 'Cache' ]
				<c:forEach var="snap" items="${snapshots}">
					<c:if test="${snap.deltaObs.used != null}">
						, [ '${snap.dateTime}', ${snap.observations.used/1024}, ${snap.observations.buffer/1024}, ${snap.observations.cache/1024} ]
					</c:if>
				</c:forEach> ];
			var totalMemory = ${snapshots[0].observations.total/1024};
			
			$(document).ready(function(){
				
				var memory = framework.timedSeries.decompose(matrixData);
			    var plot1b = $.jqplot('mem_chart_div', memory.data, {
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
			    			max: totalMemory,
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
			});
		</script>
	</jsp:attribute>
</t:graph-portlet>
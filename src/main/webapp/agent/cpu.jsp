<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<t:graph-portlet title="Dashboard">
	<jsp:attribute name="content">
		<div style="padding-left: 20px; padding-right: 150px">
			<div id="cpu_chart_div" style="width: 100%; height: 280px;"></div>
		</div>
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		<script type="text/javascript">
			var matrixData = [[ 'timestamp', 'User', 'System', 'Wait', 'Steal' ]
				<c:forEach var="snap" items="${snapshots}">
					<c:if test="${snap.deltaObs.user_pc != null}">
						, [ '${snap.dateTime}', ${snap.deltaObs.user_pc}, ${snap.deltaObs.system_pc}, ${snap.deltaObs.wait_pc}, ${snap.deltaObs.steal_pc} ]
					</c:if>
				</c:forEach>
			];
			$(document).ready(function(){
				
				var cpu = framework.timedSeries.decompose(matrixData);
			    var plot1b = $.jqplot('cpu_chart_div', cpu.data, {
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
			});
		</script>
	</jsp:attribute>
</t:graph-portlet>



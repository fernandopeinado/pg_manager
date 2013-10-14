<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<t:initvars />
<div id="mem_chart_div" style="width: 100%; height: 320px;"></div>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
   <script type="text/javascript">
	google.load("visualization", "1", {
		packages : [ "corechart" ]
	});
	google.setOnLoadCallback(drawChart);
	function drawChart() {
		var data = google.visualization.arrayToDataTable([
				[ 'timestamp', 'Used', 'Buffer', 'Cache', 'Free' ]
				<c:forEach var="snap" items="${snapshots}">
					<c:if test="${snap.deltaObs.used != null}">
						, [ '${snap.dateTime}', ${snap.observations.used}, ${snap.observations.buffer}, ${snap.observations.cache}, ${snap.observations.free} ]
					</c:if>
				</c:forEach>
				]);

		var options = {
			isStacked : true,
			hAxis : {
				title : 'Time',
				titleTextStyle : {
					color : '#333',
					fontSize : 1
				}
			},
			vAxis : {
				title: "Memory (Kb)",
				minValue : 0
			},
			lineWidth : 0,
			areaOpacity : 1,
			series : [ {color: '#009900'},{color: '#1FFF00'},{color: '#B6FFC4'},{color: '#CCCCCC'} ]
		};

		var chart = new google.visualization.AreaChart(document .getElementById('mem_chart_div'));
		chart.draw(data, options);
	}
</script>




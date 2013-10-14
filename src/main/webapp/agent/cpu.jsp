<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<t:portlet title="Dashboard">
    <jsp:attribute name="content">
		<div id="cpu_chart_div" style="width: 100%; height: 320px;"></div>
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		<script type="text/javascript">
			google.load("visualization", "1", {
				packages : [ "corechart" ]
			});
			google.setOnLoadCallback(drawChart);
			function drawChart() {
				var data = google.visualization.arrayToDataTable([
						[ 'timestamp', 'User', 'System', 'Wait', 'Steal' ]
						<c:forEach var="snap" items="${snapshots}">
							<c:if test="${snap.deltaObs.user_pc != null}">
								, [ '${snap.dateTime}', ${snap.deltaObs.user_pc}, ${snap.deltaObs.system_pc}, ${snap.deltaObs.wait_pc}, ${snap.deltaObs.steal_pc} ]
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
						title: "CPU (%)",
						minValue : 0
					},
					lineWidth : 0,
					areaOpacity : 1,
					series : [ {color: '#33CC33'},{color: '#FFFF4D'}, {color: '#FF3333'}, {color: '#99CCFF'} ]
				};
		
				var chart = new google.visualization.AreaChart(document
						.getElementById('cpu_chart_div'));
				chart.draw(data, options);
			}
		</script>
	</jsp:attribute>
</t:portlet>




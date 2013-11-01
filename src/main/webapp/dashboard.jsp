<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<t:window title="Dashboard">
    <jsp:attribute name="content">
    	<div class="row">
			<div class="span12">
				<iframe id="cpu_graph" src="${contextPath}/ws/agent/cpu" width="100%" height="340" seamless="seamless"></iframe>
			</div>
		</div>
    	<div class="row">
			<div class="span12">
				<iframe id="memory_graph" src="${contextPath}/ws/agent/memory" width="100%" height="340" seamless="seamless"></iframe>
			</div>
		</div>
    	<div class="row">
			<div class="span12">
				<iframe id="databaseStats_graph" src="${contextPath}/ws/agent/databaseStats" width="100%" height="1360" seamless="seamless"></iframe>
			</div>
		</div>
		<div class="row">
			<div class="span12">
				<div id="dash_3" data-load="${contextPath}/ws/dashboard/databaseStats"><!-- nt --></div>
			</div>
		</div>
    	<div class="row">
			<div class="span6">
				<iframe id="dash_1" src="${contextPath}/ws/dashboard/topDatabaseSizes/-1" style="height: 340px; width: 100%; padding-bottom: 20px"  seamless="seamless"></iframe>
			</div>
			<div class="span6">
				<iframe id="dash_2" src="${contextPath}/ws/dashboard/topRelationSizes/-1" style="height: 340px; width: 100%; padding-bottom: 20px"  seamless="seamless"></iframe>
			</div>
		</div>
		<div class="row">
			<div class="span6">
				<div id="dash_4" data-load="${contextPath}/ws/dashboard/cacheStats"><!-- nt --></div>
			</div>
		</div>
		<div class="row">
			<div class="span12">
				<div id="dash_topStatements" data-load="${contextPath}/ws/dashboard/topStatements/10"><!-- nt --></div>
			</div>
		</div>
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script type="text/javascript">
		framework.pageScript();
		
		function refreshCPU() {
			$("#cpu_graph")[0].contentWindow.location.reload();
			$("#memory_graph")[0].contentWindow.location.reload();
			$("#databaseStats_graph")[0].contentWindow.location.reload();
			$("#dash_1")[0].contentWindow.location.reload();
			$("#dash_2")[0].contentWindow.location.reload();
			setTimeout(refreshCPU, 60000);
		}
		
		setTimeout(refreshCPU, 60000);
		</script>
	</jsp:attribute>
</t:window>
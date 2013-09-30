<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<t:window title="Dashboard">
    <jsp:attribute name="content">
    	<div class="row">
			<div class="span6">
				<div id="dash_1" data-load="${contextPath}/ws/dashboard/topDatabaseSizes/10"><!-- nt --></div>
			</div>
			<div class="span6">
				<div id="dash_2" data-load="${contextPath}/ws/dashboard/topRelationSizes/10"><!-- nt --></div>
			</div>
		</div>
		<div class="row">
			<div class="span12">
				<div id="dash_3" data-load="${contextPath}/ws/dashboard/databaseStats"><!-- nt --></div>
			</div>
		</div>
		<div class="row">
			<div class="span4">
				<div id="dash_4" data-load="${contextPath}/ws/dashboard/cacheStats"><!-- nt --></div>
			</div>
			<div class="span8">
				<iframe id="cpu_graph" src="${contextPath}/ws/agent/cpu" width="100%" height="340" seamless="seamless"></iframe>
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
		</script>
	</jsp:attribute>
</t:window>
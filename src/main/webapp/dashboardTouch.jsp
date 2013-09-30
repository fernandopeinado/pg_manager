<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<t:window title="Dashboard Touch">
    <jsp:attribute name="content">
		<div class="row">
			<div class="span6">
				<div data-load="${contextPath}/ws/dashboardTouch/auditStats"><!-- nt --></div>
			</div>
		</div>
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script type="text/javascript">
		framework.pageScript();
		</script>
	</jsp:attribute>
</t:window>
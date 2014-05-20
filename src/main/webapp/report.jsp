<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<t:window title="Report">
	<jsp:attribute name="content">
		<h3>Missing Index for Foreign Key</h3>
		
		<c:forEach var="database" items="${databases}">
			<c:if test="${missingIndexFK != null}">
			<table class="table table-striped table-condensed">
				<caption>${database}</caption>
				<thead>
					<tr>
						<c:forEach var="entry" items="${missingIndexFK[database][0]}">
							<th>${entry.key}</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="missing" items="${missingIndexFK[database]}">
						<tr>
							<c:forEach var="entry" items="${missing}">
								<td>${entry.value}</td>
							</c:forEach>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
		</c:forEach>
	</jsp:attribute>
	<jsp:attribute name="scripts">

	</jsp:attribute>
</t:window>
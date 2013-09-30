<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<t:initvars/>
<table class="table table-striped table-condensed">
	<caption>Top Database Sizes</caption>
	<thead>
		<tr>
			<th>Database</th>
			<th>Size</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="database" items="${topSizes}">
			<tr>
				<td>${database["dbname"]}</td>
				<td>${database["size"]}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
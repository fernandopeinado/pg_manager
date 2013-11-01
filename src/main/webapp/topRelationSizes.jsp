<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<t:portlet title="Top Relation Sizes">
<jsp:attribute name="content">
	<table class="table table-striped table-condensed">
		<caption>Top Relation Sizes</caption>
		<thead>
			<tr>
				<th>Relation</th>
				<th>Size</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="relation" items="${topSizes}">
				<tr>
					<td>${relation["relation"]}</td>
					<td>${relation["size"]}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</jsp:attribute>
</t:portlet>

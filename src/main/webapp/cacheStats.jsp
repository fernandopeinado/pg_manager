<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<t:initvars />
<c:if test="${stats != null}">
	<table class="table table-striped table-condensed">
		<caption>Cache Statistics</caption>
		<thead>
			<tr>
				<c:forEach var="entry" items="${stats[0]}">
					<th>${entry.key}</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="stat" items="${stats}">
				<tr>
					<c:forEach var="entry" items="${stat}">
						<td>${entry.value}</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</c:if>
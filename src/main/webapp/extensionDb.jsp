<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<t:window title="Extension">
	<jsp:attribute name="content">
		<table class="table table-condensed table-striped">
        <thead>
            <th>Name</th>
        </thead>
        <tbody>
        <c:forEach items="${databases}" var="db">
            <tr>
                <td><a href="<c:url value="/ws/extension/${db}"/>">${db}</a></td>
            </tr>
        </c:forEach>
        </tbody>
        </table>
	</jsp:attribute>
	<jsp:attribute name="scripts">

	</jsp:attribute>
</t:window>

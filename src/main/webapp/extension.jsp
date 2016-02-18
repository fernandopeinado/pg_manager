<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<t:window title="Extension">
	<jsp:attribute name="content">
		<table class="table table-condensed table-striped">
        <thead>
            <th>Name</th>
            <th>Version</th>
            <th>Installed Version</th>
            <th>Comment</th>
        </thead>
        <tbody>
        <c:forEach items="${extensions}" var="pgExtension">
            <tr>
                <td>${pgExtension.name}</td>
                <td>${pgExtension.defaultVersion}</td>
                <td>
                    <c:if test="${pgExtension.installedVersion != null}">${pgExtension.installedVersion}</c:if>
                    <c:if test="${pgExtension.installedVersion == null}">
                        <a href="${contextPath}/ws/extension/${database}/create/${pgExtension.name}" class="btn btn-default">Create</input>
                    </c:if>
                </td>
                <td>${pgExtension.comment}</td>
            </tr>
        </c:forEach>
        </tbody>
        </table>
	</jsp:attribute>
	<jsp:attribute name="scripts">

	</jsp:attribute>
</t:window>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<t:window title="Parameters">
	<jsp:attribute name="content">
	    <c:forEach items="${categories}" var="category">
	        <h2>${category.name}</h2>
            <table class="table table-condensed table-striped">
            <thead>
                <th>Name</th>
                <th>Value</th>
                <th>Default</th>
                <th>Source</th>
                <th>Comment</th>
            </thead>
            <tbody>
            <c:forEach items="${category.parameters}" var="parameter">
                <tr>
                    <td>${parameter.name}</td>
                    <td>${parameter.value}</td>
                    <td>${parameter.defaultValue}</td>
                    <td>${parameter.source}</td>
                    <td>${parameter.description}</td>
                </tr>
            </c:forEach>
            </tbody>
            </table>
        </c:forEach>
	</jsp:attribute>
	<jsp:attribute name="scripts">

	</jsp:attribute>
</t:window>

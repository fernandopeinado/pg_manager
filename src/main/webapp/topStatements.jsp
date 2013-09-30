<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<t:initvars />
<c:if test="${topStatements != null}">
	<table class="table table-striped table-condensed">
		<caption><button class="btn btn-primary pull-left" data-bind="refreshStatementsStats"><i class="icon-refresh icon-white"></i>Refresh</button> Top Statements <button class="btn pull-right" data-bind="resetStatementsStats">Reset</button></caption>
		<thead>
			<tr>
				<c:forEach var="entry" items="${topStatements[0]}">
					<th>${entry.key}</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="stat" items="${topStatements}">
				<tr>
					<c:forEach var="entry" items="${stat}">
						<td>${entry.value}</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<script type="text/javascript">
	(function() {
		var TopStatements = {
			resetStatementsStats : {
				click : function(args) {
					$.ajax({
						url : framework.getUrl("ws/dashboard/resetStatementsStats"),
						success: function (data) {
							alert(data);
						}
					});
				}
			},
			refreshStatementsStats : {
				click : function(args) {
					$.ajax({
						url : framework.getUrl("ws/dashboard/topStatements/10"),
						success: function (data) {
							$("#dash_topStatements").html(data);
							framework.pageScript(TopStatements, "#dash_topStatements");
						}
					});
				}
			}
		}
		framework.pageScript(TopStatements);
	})();
	</script>
</c:if>
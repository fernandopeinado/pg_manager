<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<t:portlet title="Top Relation Sizes">
	<jsp:attribute name="content">
	<table id="dbSizes" class="table table-striped table-condensed">
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
					<td><a data-bind="databaseOpen" data-database="${database['dbname']}">${database["dbname"]}</a></td>
					<td>${database["size"]}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div id="relSizes" style="display: none;">
		<a class="btn btn-link pull-left" data-bind="databaseClose"><i class="icon-share-alt icon-flip-x"></i> Databases</a>
		<caption id="modalLabel"><!-- Nada --></caption>
		<div id="body"><!-- Nada --></div>
	</div>
</jsp:attribute>
<jsp:attribute name="scripts">
	<script type="text/javascript">
	(function() {
		var TopDatabaseSizes = {
			databaseOpen : {
				click : function(args) {
					$.ajax({
						url : framework.getUrl("ws/dashboard/topRelationSizes/" + args.database + "/-1"),
						success: function (data) {
							$("#modalLabel").html(args.database);
							$("#body").html(data);
							$('#relSizes').show();
							$('#dbSizes').hide();
						}
					});
				}
			},
			databaseClose : {
				click : function(args) {
					$('#dbSizes').show();
					$('#relSizes').hide();
				}
			}
		};
		framework.pageScript(TopDatabaseSizes);
	})();
	</script>
</jsp:attribute>
</t:portlet>
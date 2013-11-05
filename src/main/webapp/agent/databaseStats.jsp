<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t"%>
<t:graph-portlet title="Dashboard">
	<jsp:attribute name="content">
		<div style="padding-left: 20px; padding-right: 150px">
			<div id="commit_chart_div" style="width: 100%; height: 150px;"></div>
			<div id="rollback_chart_div" style="width: 100%; height: 150px; margin-top: 20px"></div>
			<div id="blksread_chart_div" style="width: 100%; height: 150px; margin-top: 20px"></div>
			<div id="blkshit_chart_div" style="width: 100%; height: 150px; margin-top: 20px"></div>
			<div id="tupreturned_chart_div" style="width: 100%; height: 150px; margin-top: 20px"></div>
			<div id="tupfetched_chart_div" style="width: 100%; height: 150px; margin-top: 20px"></div>
			<div id="tupinserted_chart_div" style="width: 100%; height: 150px; margin-top: 20px"></div>
			<div id="tupupdated_chart_div" style="width: 100%; height: 150px; margin-top: 20px"></div>
			<div id="tupdeleted_chart_div" style="width: 100%; height: 150px; margin-top: 20px"></div>
		</div>
	</jsp:attribute>
	<jsp:attribute name="scripts">
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		<script type="text/javascript">
			var commitMatrixData = [ [ 'timestamp' <c:forEach var="obs" items="${snapshots[0].observations}">, '${obs.key}'</c:forEach> ]
			<c:forEach var="snap" items="${snapshots}">
				, [ '${snap.dateTime}' <c:forEach var="delta" items="${snap.deltaObs}">, ${delta.value.commits_ps}</c:forEach> ]
			</c:forEach> ];

			var rollbackMatrixData = [ [ 'timestamp' <c:forEach var="obs" items="${snapshots[0].observations}">, '${obs.key}'</c:forEach> ]
			<c:forEach var="snap" items="${snapshots}">
				, [ '${snap.dateTime}' <c:forEach var="delta" items="${snap.deltaObs}">, ${delta.value.rollback_ps}</c:forEach> ]
			</c:forEach> ];
			
			var blksreadMatrixData = [ [ 'timestamp' <c:forEach var="obs" items="${snapshots[0].observations}">, '${obs.key}'</c:forEach> ]
			<c:forEach var="snap" items="${snapshots}">
				, [ '${snap.dateTime}' <c:forEach var="delta" items="${snap.deltaObs}">, ${delta.value.blks_read_ps}</c:forEach> ]
			</c:forEach> ];
			
			var blkshitMatrixData = [ [ 'timestamp' <c:forEach var="obs" items="${snapshots[0].observations}">, '${obs.key}'</c:forEach> ]
			<c:forEach var="snap" items="${snapshots}">
				, [ '${snap.dateTime}' <c:forEach var="delta" items="${snap.deltaObs}">, ${delta.value.blks_hit_ps}</c:forEach> ]
			</c:forEach> ];

			var tupreturnedMatrixData = [ [ 'timestamp' <c:forEach var="obs" items="${snapshots[0].observations}">, '${obs.key}'</c:forEach> ]
			<c:forEach var="snap" items="${snapshots}">
				, [ '${snap.dateTime}' <c:forEach var="delta" items="${snap.deltaObs}">, ${delta.value.tup_returned_ps}</c:forEach> ]
			</c:forEach> ];

			var tupfetchedMatrixData = [ [ 'timestamp' <c:forEach var="obs" items="${snapshots[0].observations}">, '${obs.key}'</c:forEach> ]
			<c:forEach var="snap" items="${snapshots}">
				, [ '${snap.dateTime}' <c:forEach var="delta" items="${snap.deltaObs}">, ${delta.value.tup_fetched_ps}</c:forEach> ]
			</c:forEach> ];

			var tupinsertedMatrixData = [ [ 'timestamp' <c:forEach var="obs" items="${snapshots[0].observations}">, '${obs.key}'</c:forEach> ]
			<c:forEach var="snap" items="${snapshots}">
				, [ '${snap.dateTime}' <c:forEach var="delta" items="${snap.deltaObs}">, ${delta.value.tup_inserted_ps}</c:forEach> ]
			</c:forEach> ];

			var tupupdatedMatrixData = [ [ 'timestamp' <c:forEach var="obs" items="${snapshots[0].observations}">, '${obs.key}'</c:forEach> ]
			<c:forEach var="snap" items="${snapshots}">
				, [ '${snap.dateTime}' <c:forEach var="delta" items="${snap.deltaObs}">, ${delta.value.tup_updated_ps}</c:forEach> ]
			</c:forEach> ];

			var tupdeletedMatrixData = [ [ 'timestamp' <c:forEach var="obs" items="${snapshots[0].observations}">, '${obs.key}'</c:forEach> ]
			<c:forEach var="snap" items="${snapshots}">
				, [ '${snap.dateTime}' <c:forEach var="delta" items="${snap.deltaObs}">, ${delta.value.tup_deleted_ps}</c:forEach> ]
			</c:forEach> ];

			
			$(document).ready(function(){
				
				var commits = framework.timedSeries.decompose(commitMatrixData);
				var rollbacks = framework.timedSeries.decompose(rollbackMatrixData);
				var blksread = framework.timedSeries.decompose(blksreadMatrixData);
				var blkshit = framework.timedSeries.decompose(blkshitMatrixData);
				var tupreturned = framework.timedSeries.decompose(tupreturnedMatrixData);
				var tupfetched = framework.timedSeries.decompose(tupfetchedMatrixData);
				var tupinserted = framework.timedSeries.decompose(tupinsertedMatrixData);
				var tupupdated = framework.timedSeries.decompose(tupupdatedMatrixData);
				var tupdeleted = framework.timedSeries.decompose(tupdeletedMatrixData);
				
			    $.jqplot('commit_chart_div', commits.data, getConfig(commits, 'Commits / sec', true));
			    $.jqplot('rollback_chart_div', rollbacks.data, getConfig(rollbacks, "Rollbacks / sec", false));
			    $.jqplot('blksread_chart_div', blksread.data, getConfig(blksread, "Blocks Read / sec", false));
			    $.jqplot('blkshit_chart_div', blkshit.data, getConfig(blkshit, "Blocks Hit / sec", false));
			    $.jqplot('tupreturned_chart_div', tupreturned.data, getConfig(tupreturned, "Tuples Returned / sec", false));
			    $.jqplot('tupfetched_chart_div', tupfetched.data, getConfig(tupfetched, "Tuples Fetched / sec", false));
			    $.jqplot('tupinserted_chart_div', tupinserted.data, getConfig(tupinserted, "Tuples Inserted / sec", false));
			    $.jqplot('tupupdated_chart_div', tupupdated.data, getConfig(tupupdated, "Tuples Updated / sec", false));
			    $.jqplot('tupdeleted_chart_div', tupdeleted.data, getConfig(tupdeleted, "Tuples Deleted / sec", false));
			    
			    function getConfig(root, title, legend) {
			    	return {
						stackSeries: true,
				       	showMarker: false,
				       	seriesDefaults: {
				       		shadow: false,
							fill: true
				       	},
				       	series: root.labels,
				       	axes: {
				    		yaxis: {
				    			min: 0,
				    			numberTicks: 5
				    	  	},
				           	xaxis: {
				            	renderer: $.jqplot.DateAxisRenderer,
				            	labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
				                tickRenderer: $.jqplot.CanvasAxisTickRenderer,
				            	tickOptions: {
				                    angle: 90,
				                    formatString:'%H:%M:%S',
				                    fontSize: '7pt'
				                },
				            	min: root.minTime, 
				            	max: root.maxTime,
				            	numberTicks: 10
				           	}
				       	},
				       	title: {
				       		text: title
				       	},
				       	legend: {
				            show: legend,
				            location: 'ne',
				            placement: 'outside'
				        },
				        grid: {
				        	drawBorder: false,
				        	shadow: false,
				        	background: '#FFFFFF',
				        	gridLineColor: '#E5E5E5'
				        }
			    	};
			    }
			});
		</script>
	</jsp:attribute>
</t:graph-portlet>
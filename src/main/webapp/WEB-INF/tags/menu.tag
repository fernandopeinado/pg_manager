<%@ attribute name="menuActive" required="true" %>
<li class="dropdown">
	<a href="#" class="dropdown-toggle" data-toggle="dropdown">Menu <b class="caret"></b></a>
	<ul class="dropdown-menu">
		<li role="presentation"><a role="menuitem" tabindex="-1" href="${contextPath}">Home</a></li>
		<li role="presentation"><a role="menuitem" tabindex="-1" href="${contextPath}/ws/dashboard">Dashboard</a></li>
		<li role="presentation"><a role="menuitem" tabindex="-1" href="${contextPath}/ws/extension">Extensions</a></li>
		<li role="presentation"><a role="menuitem" tabindex="-1" href="${contextPath}/ws/system">System</a></li>
		<li role="presentation"><a role="menuitem" tabindex="-1" href="${contextPath}/ws/parameters">Parameters</a></li>
		<li role="presentation"><a role="menuitem" tabindex="-1" href="${contextPath}/ws/parameters/nondefault">Parameters Overrided</a></li>
		<li role="presentation"><a role="menuitem" tabindex="-1" href="${contextPath}/ws/report">Report</a></li>
	</ul>
</li>
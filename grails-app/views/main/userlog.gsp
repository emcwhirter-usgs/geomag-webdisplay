<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>MagWorm WebDisplay User Log</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="layout" content="mainapp" />
	</head>
	<body>
		<div id="maincontent">
		<script>
$(function() {
	$("#tabs").tabs({
		ajaxOptions: {
					error: function( xhr, status, index, anchor ) {
						$(anchor.hash).html("Couldn't load.");
					}
		}
	});
	$("#fromdate").datepicker({ dateFormat: 'm/d/yy'});
	$("#todate").datepicker({ dateFormat: 'm/d/yy'});
});
	</script>
	<br/>

			<div class="demodds">
				<h1 class="firstHeader">User Log</h1>
				<br class="clear-both"/>
				<div id="tabs">
					<ul>
						<li><a href="#tab-1">Changes</a></li>
					</ul>
					
					<g:form action="userlog">
					<div id="tab-1">
							
							<table>
								<tr>
								<td>From <input type="text" id="fromdate" name="fromdate" style="width:10em" value="${fromdate}">
								To <input type="text" id="todate" name="todate" style="width:10em" value="${todate}">
								<g:submitButton name="filter" value="Filter by Dates"/>
								</td></tr>
							</table>
						
						<br/>
						<table>
							<tr><th>Date</th><th>User</th><th>Message</th></tr>
							<g:each in="${records}">
							<% def msg = it.message?.replaceAll(/^'([^']+)'/, '<b>$1</b>') %>
     					<tr><td>${it.timestamp}</td><td>${it.user}</td><td>${msg}</td></tr>
     					</g:each>
						</table>
					</div>

					<center>
						<g:hiddenField name="page_number" value="${page_number}" />

						<g:if test="${number_of_pages > 1}">
						<br/>
						<g:if test="${page_number > 1}">
							<g:submitButton name="prev" value="Prev"/>
						</g:if>
						page ${page_number} of ${number_of_pages}
						<g:if test="${page_number < number_of_pages}">
							<g:submitButton name="next" value="Next"/>
						</g:if>
						<br/>
						<br/>
						</g:if>

					<script>
					$( "#next" ).button( "option", "icons", {primary:'ui-icon-gear',secondary:'ui-icon-triangle-1-s'} );
					</script>
					
					</center>
					
					</g:form>
					
				</div>
			</div>
		</div>
	</body>
</html>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 	<head>
        <title>MagWorm WebDisplay Bin Log</title>
    	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta name="layout" content="mainapp" />
    </head>
	<body>
		<div id="maincontent">
			<script>
						$(function() {
							$( "#tabs" ).tabs({
								ajaxOptions: {
									error: function( xhr, status, index, anchor ) {
										$( anchor.hash ).html(
											"Couldn't load this tab.");
									}
								}
							});

                            var doc1 = "<table><tr><td><form method='post' action='binlog'>";
                            doc1 = doc1 + "From <input type='text' data-bind='value: fromDate' name='fromdate' id='fromdate1' style='width:10em'>";
                            doc1 = doc1 + " To <input type='text' data-bind='value:  toDate' name='todate' id='todate1' style='width:10em'>";
                            doc1 = doc1 + " <input type='submit' name='filter' value='Filter by Date' id='filter' />";
                            doc1 = doc1 + "</form></td></tr></table><br/>";

                            var doc2 = "<table><tr><td><form method='post' action='binlog'>";
                            doc2 = doc2 + "From <input type='text' data-bind='value: fromDate' name='fromdate' id='fromdate2' style='width:10em'>";
                            doc2 = doc2 + " To <input type='text' data-bind='value:  toDate' name='todate' id='todate2' style='width:10em'>";
                            doc2 = doc2 + " <input type='submit' name='filter' value='Filter by Date' id='filter' />";
                            doc2 = doc2 + "</form></td></tr></table><br/>";

                            var doc3 = "<table><tr><td><form method='post' action='binlog'>";
                            doc3 = doc3 + "From <input type='text' data-bind='value: fromDate' name='fromdate' id='fromdate3' style='width:10em'>";
                            doc3 = doc3 + " To <input type='text' data-bind='value:  toDate' name='todate' id='todate3' style='width:10em'>";
                            doc3 = doc3 + " <input type='submit' name='filter' value='Filter by Date' id='filter' />";
                            doc3 = doc3 + "</form></td></tr></table><br/>";

                            var doc4 = "<table><tr><td><form method='post' action='binlog'>";
                            doc4 = doc4 + "From <input type='text' data-bind='value: fromDate' name='fromdate' id='fromdate4' style='width:10em'>";
                            doc4 = doc4 + " To <input type='text' data-bind='value:  toDate' name='todate' id='todate4' style='width:10em'>";
                            doc4 = doc4 + " <input type='submit' name='filter' value='Filter by Date' id='filter' />";
                            doc4 = doc4 + "</form></td></tr></table><br/>";

							var doc5 = "<table><tr><td><form method='post' action='binlog'>";
							doc5 = doc5 + "From <input type='text' data-bind='value: fromDate' name='fromdate' id='fromdate5' style='width:10em'>";
							doc5 = doc5 + " To <input type='text' data-bind='value:  toDate' name='todate' id='todate5' style='width:10em'>";
							doc5 = doc5 + " <input type='submit' name='filter' value='Filter by Date' id='filter' />";
							doc5 = doc5 + "</form></td>";
							doc5 = doc5 + "<td>F spike resolution: ${f_spike_resolution} second${f_spike_resolution != 1 ? 's' : ''}</td>";
							doc5 = doc5 + "</tr></table><br>";



							$("#tab-1-daterange").html(doc1);
							$("#tab-2-daterange").html(doc2);
							$("#tab-3-daterange").html(doc3);
							$("#tab-4-daterange").html(doc4);
							$("#tab-5-daterange").html(doc5);

							$("input[name=fromdate]").datepicker({ dateFormat: 'm/d/yy'});
							$("input[name=todate]").datepicker({ dateFormat: 'm/d/yy'});

							$("td[id=secs_from_midnight]").html(function(){ 
												var str1 = $(this).html(); 
												var d = new Date(Number(str1));
												var str2 = d.toUTCDayMonthFullYear();
			                                    str2 = str2 + " " + pad2(d.getUTCHours()) + ":" + pad2(d.getUTCMinutes()) + ":" + pad2(d.getUTCSeconds());
			                                    str2 = str2 + " (" + d.getUTCSecondsFromMidnight() + ")";
												return  str2;
							});


                            function ViewModel() {
                                this.fromDate = ko.observable("${fromdate}");
                                this.toDate   = ko.observable("${todate}");
                            }

                            var obj1 = new ViewModel();

                            ko.applyBindings(obj1);


                        });

            </script>
			<br/>
			<div class="demodds">
			<h1 class="firstHeader">Bin Log</h1>
				<br class="clear-both"/>
				<div id="tabs">
					<ul>
						<li><a href="#tab-5">F</a></li>
						<li><a href="#tab-1">H</a></li>
						<li><a href="#tab-2">E</a></li>
						<li><a href="#tab-3">Z</a></li>
						<li><a href="#tab-4">H+E+Z</a></li>
					</ul>
											
					<div id="tab-1">
						<div id="tab-1-daterange"></div>
						<table>
							<tr><th>Date</th>
                                <th>Bin Before</th>
                                <th>Bin After</th>
                                <th>Voltage Before</th>
                                <th>Voltage After</th>
                                <th>nT Before</th>
                                <th>nT After</th>
                            </tr>
							<g:each in="${data['rows']}" var="row">
									<g:if test="${ row[8] != row[20]}">
									<tr>
										<td id="secs_from_midnight">${row[19]}</td>
										<td>${row[8]}</td>
										<td>${row[20]}</td>
										<td>${row[11]}</td>
										<td>${row[23]}</td>
										<td>${row[14]}</td>
										<td>${row[26]}</td>
									</tr>
									</g:if>
								</g:each>
						</table>
					</div>
					
					<div id="tab-2">
							<div id="datefilter"></div>
							<div id="tab-2-daterange"></div>
							<table>
								<tr><th>Date</th><th>Bin Before</th><th>Bin After</th><th>Voltage Before</th><th>Voltage After</th><th>nT Before</th><th>nT After</th></tr>
								<g:each in="${data['rows']}" var="row">
								
									<g:if test="${ row[9] != row[21]}">
									<tr>
										<td id="secs_from_midnight">${row[19]}</td>
										<td>${row[9]}</td>
										<td>${row[21]}</td>
										<td>${row[12]}</td>
										<td>${row[24]}</td>
										<td>${row[15]}</td>
										<td>${row[27]}</td>
									</tr>
									</g:if>
									
								</g:each>
							</table>
					</div>
					
					<div id="tab-3">
							<div id="tab-3-daterange"></div>
							<table>
								<tr><th>Date</th><th>Bin Before</th><th>Bin After</th><th>Voltage Before</th><th>Voltage After</th><th>nT Before</th><th>nT After</th></tr>
								<g:each in="${data['rows']}" var="row">
									<g:if test="${ row[10] != row[22]}">
									<tr>
										<td id="secs_from_midnight">${row[19]}</td>
										<td>${row[10]}</td>
										<td>${row[22]}</td>
										<td>${row[13]}</td>
										<td>${row[25]}</td>
										<td>${row[16]}</td>
										<td>${row[28]}</td>
									</tr>
									</g:if>
									
									
								</g:each>
							</table>
					</div>
					
					<div id="tab-4">
							<div id="tab-4-daterange"></div>
							<table>
								<tr><th>Bin</th><th>Date</th><th>Bin Before</th><th>Bin After</th><th>Voltage Before</th><th>Voltage After</th><th>nT Before</th><th>nT After</th></tr>

								<g:each in="${data['rows']}" var="row">
								
									<g:if test="${ row[8] != row[20]}">
									<tr>
										<td>H</td>
										<td id="secs_from_midnight">${row[19]}</td>
										<td>${row[8]}</td>
										<td>${row[20]}</td>
										<td>${row[11]}</td>
										<td>${row[23]}</td>
										<td>${row[14]}</td>
										<td>${row[26]}</td>
									</tr>
									</g:if>
									<g:elseif test="${ row[9] != row[21]}">
									<tr>
										<td>E</td>
										<td id="secs_from_midnight">${row[19]}</td>
										<td>${row[9]}</td>
										<td>${row[21]}</td>
										<td>${row[12]}</td>
										<td>${row[24]}</td>
										<td>${row[15]}</td>
										<td>${row[27]}</td>
										
									</tr>
									</g:elseif>
									<g:elseif test="${ row[10] != row[22]}">
									<tr>
										<td>Z</td>
										<td id="secs_from_midnight">${row[19]}</td>
										<td>${row[10]}</td>
										<td>${row[22]}</td>
										<td>${row[13]}</td>
										<td>${row[25]}</td>
										<td>${row[16]}</td>
										<td>${row[28]}</td>
									</tr>
									</g:elseif>
									
								</g:each>
							</table>
							<br/>
							<!-- <g:link controller="main" action="binlog" params="[download:'csv']">(download csv)</g:link> -->
							
					</div>

					<div id="tab-5">
						<div id="tab-5-daterange"></div>
						<table>
							<tr><th>Date</th><th>Delta from Avg</th><th>F</th><th>10-Min Avg</th><th>Threshold</th></tr>
							<g:each in="${data.fspikes}" var="row">
								<tr>
									<td id="secs_from_midnight">${row[0]}</td>
									<td>${row[1]}</td>
									<td>${row[2]}</td>
									<td>${row[3]}</td>
									<td>${row[4]}</td>
								</tr>
							</g:each>
						</table>
					</div>

				</div>
					
			</div>
			<p><b>Note:</b>
			<ul>
				<!-- <li>The &quot;Bin Log&quot; is refreshed automatically every two hours. To manually check 
				for recent changes click <g:link controller="main" action="binlog">here</g:link>.</li> -->
				<li>The &quot;Bin Log&quot; is automatically analyzed every 5 minutes. The analyzer will only evaluate data
				for changes, if data is available, no further than 2 days into the past, and if left running it will accumalate  
				historical data as far back as the	
				<g:link controller="main" action="settings" fragment="tab-5">Data Retention Policy</g:link> will permit.
				Currently data will not be retained past  
				<g:link controller="main" action="settings" fragment="tab-5">${data_retention} days</g:link>.  
				</li>
				<li>&quot;nT Before&quot; and &quot;nT After&quot; are calculated values. For example: 
				(Bin E * <g:link controller="main" action="settings" fragment="tab-2">Bin E Constant</g:link>) + 
				(Voltage E * <g:link controller="main" action="settings" fragment="tab-2">Voltage E Constant</g:link>) = nT. </li>
			</ul>
			</p>
				
		</div>

	</body>
</html>

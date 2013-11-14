<html>
<head>
	<title>${local_station_id} Magnetometers</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="layout" content="mainapp" />
</head>
<body>
<div id="maincontent">
<g:if test="${!local_station_id}">
	<div class="errors"><ul><li><b>No Station ID defined</b>. Please enter a Local Station ID in the <g:link action="settings" style="font-weight:normal;color:#c00;">Settings</g:link>.</li></ul></div>
</g:if>
<h1 class="firstHeader">
	<g:if test="${local_station_desc && local_station_id}">${local_station_desc} (${local_station_id})</g:if>
	<g:elseif test="${local_station_desc}">${local_station_desc}</g:elseif>
	<g:elseif test="${local_station_id}">${local_station_id}</g:elseif>
	<g:else>${exporter_host}</g:else>
Magnetometers</h1>
<div class="clear-both"></div>
<div id="nav1">
	<div class="homePagePanel">
		<div class="panelBody">
			<div class="panelbox first">
				<h1>Current System Time (UTC)</h1>
				<div id="currenttime"></div>
			</div>
			<div class="panelbox">
				<h1 id="data-header-atmarker" style="display: none;">Data at Marker</h1>
				<h1 id="data-header-latest" style="display: none;">Latest Data</h1>
				<h1 id="data-header-live">Current Data</h1>
				<div id="data_at_marker">
					<table>
						<tr><td width='120px'>Data Date</td><td><div id="x_part_a_axis"></div></td></tr>
						<tr><td>&nbsp;</td><td><div id="x_part_b_axis"></div></td></tr>
						<tr><td>Data Year Date</td><td><div id="data_year_date"></div></td></tr>
						<tr><td>Date Time (UTC,s)</td><td><div id="data_date_utc"></div></td></tr>
						<tr class="data-row" id="f_axisrow"><td>F (nT)</td><td><div id="f_axis"></div></td></tr>
						<tr class="data-row" id="h_axisrow"><td>H (nT)</td><td><div id="h_axis"></div></td></tr>
						<tr class="data-row" id="e_axisrow"><td>E (nT)</td><td><div id="e_axis"></div></td></tr>
						<tr class="data-row" id="z_axisrow"><td>Z (nT)</td><td><div id="z_axis"></div></td></tr>
						<tr class="data-row" id="df_axisrow"><td>&Delta;F (nT)</td><td><div id="df_axis"></div></td></tr>
					</table>
				</div>
			</div>
			<div class="panelbox">
				<h1>Current Temperature</h1>
				<div id="temperature"></div>
			</div>
			<div class="panelbox">
				<h1>Current Battery</h1>
				<div id="battery">
					<div id="voltage"></div>
				</div>
			</div>
			<div class="panelbox">
				<h1>System Diagnostics</h1>
				<div id="system_diag">
					<div id="connectionstatus">
						<table><tr>
							<td>Connection Status</td>
							<td>
								<span id="connectionstatus-ok" style="display:none;"><img class="led" src="${resource(dir:'images',file:'c_green_10x10.png')}"> Connected</span>
								<span id="connectionstatus-no"><img class="led" src="${resource(dir:'images',file:'c_red_10x10.png')}"> Not Connected</span>
								<span id="connectionstatus-err" style="display:none;"><img class="led" src="${resource(dir:'images',file:'c_red_10x10.png')}"> Error!</span>
							</td>
						</tr><tr>
							<td>Last Scalar Data</td>
							<td>
								<span id="scalardata-ok" style="display:none;"><img class="led" src="${resource(dir:'images',file:'c_green_10x10.png')}"></span>
								<span id="scalardata-no" style="display:none;"><img class="led" src="${resource(dir:'images',file:'c_red_10x10.png')}"></span>
								<span id="scalardata-off"><img class="led" src="${resource(dir:'images',file:'c_off_10x10.png')}"></span>
								<span id="scalardata-age"></span>
							</td>
						</tr><tr>
							<td>Last Vector Data</td>
							<td>
								<span id="vectordata-ok" style="display:none;"><img class="led" src="${resource(dir:'images',file:'c_green_10x10.png')}"></span>
								<span id="vectordata-no" style="display:none;"><img class="led" src="${resource(dir:'images',file:'c_red_10x10.png')}"></span>
								<span id="vectordata-off"><img class="led" src="${resource(dir:'images',file:'c_off_10x10.png')}"></span>
								<span id="vectordata-age"></span>
							</td>
						</tr></table>
					</div>
				</div>
			</div>
			<div class="panelbox" id="absolutesstatus-box">
				<div id="absolutesstatus-info" style="display:none;"></div>
				<h1>Absolutes Status</h1>
				<div id="absolutesstatus">
					<table><tr>
							<td>Values safe to take</td>
							<td>
								<span id="absolutestatus-ok" style="display:none;"><img class="led" src="${resource(dir:'images',file:'c_green_10x10.png')}"> Safe</span>
								<span id="absolutestatus-warn" style="display:none;"><img class="led" src="${resource(dir:'images',file:'c_yellow.png')}" width="10" height="10"> Not safe</span>
								<span id="absolutestatus-nored"><img class="led" src="${resource(dir:'images',file:'c_red_10x10.png')}"> Not safe</span>
								<span id="absolutestatus-nooff" style="display:none;"><img class="led" src="${resource(dir:'images',file:'c_off_10x10.png')}"> Not safe</span>
								<span id="absolutestatus-err" style="display:none;"><img class="led" src="${resource(dir:'images',file:'c_off_10x10.png')}"> (no connection)</span>
							</td>
					</tr></table>
				</div>
			</div>
			<div class="panelbox">
				<h1>Display Settings</h1>
				<div id="display_settings">
					<table>
						<tr>
							<td>Vertical Scale</td>
							<td>
								<g:select id="vertical_scale" style="width:100%"
								 name='vertical_scale' value="${v_value}" from='${v_list}'
								 optionKey="key" optionValue="value"></g:select>
							</td>
						</tr>
						<tr>
							<td>Horizontal Scale</td>
							<td>
								<g:select id="horizontal_scale" style="width:100%"
								 name='horizontal_scale' value="${h_value}" from='${h_list}'
								 optionKey="key" optionValue="value"></g:select>
							</td>
						</tr>
						<tr>
							<td class="buttoncontainer" colspan="2" style="text-align: center">
								<a href="" id="tape-backward" title="Move back in time" class="cassettetape-button cassettetape-button-disabled">◀◀</a>
								<a href="" id="tape-play" title="Update data in real time" class="cassettetape-button cassettetape-button-selected">▶</a>
								<a href="" id="tape-pause" title="Freeze data on screen" class="cassettetape-button">▮▮</a>
								<a href="" id="tape-forward" title="Move forward in time" class="cassettetape-button cassettetape-button-disabled">▶▶</a>
							</td>
						</tr>
						<tr>
							<td>Refresh Rate</td>
							<td>
								<div id="refresh_rate-edit">
									<g:select id="refresh_rate" style="width:100%"
									 name='refresh_rate' value="${rr_value}" from='${rr_list}'
									 optionKey="key" optionValue="value"></g:select>
								</div>
								<div id="refresh_rate-readonly" style="display: none;">
									<span id="refresh_rate-readonly-value">(no refresh)</span>
								</div>
							</td>
						</tr>
						<tr>
							<td>Start Date</td>
							<td style="white-space: nowrap;">
								<div id="startdate-edit" style="display: none;">
									<input type="text" id="startdate" value="${startdate}"><g:select id="sd_hour"
									 name='sd_hour' value="${hour_value}" from='${hour_list}'
									 optionKey="key" optionValue="value"></g:select><g:select id="sd_minute"
									 name='sd_minute' value="${minute_value}" from='${minute_list}'
									 optionKey="key" optionValue="value"></g:select>
								</div>
								<div id="startdate-readonly">
									<span id="startdate-readonly-value"></span>
								</div>
							</td>
						</tr>
						<tr>
							<td>End Date</td>
							<td style="white-space: nowrap;">
								<div id="enddate-edit" style="display: none;">
									<input type="text" id="enddate" value="${enddate}"><g:select id="ed_hour"
									 name='ed_hour' value="${hour_value}" from='${hour_list}'
									 optionKey="key" optionValue="value"></g:select><g:select id="ed_minute"
									 name='ed_minute' value="${minute_value}" from='${minute_list}'
									 optionKey="key" optionValue="value"></g:select>
								</div>
								<div id="enddate-readonly">
									<span id="enddate-readonly-value"></span>
								</div>
							</td>
						</tr>
						<tr>
							<td>View</td>
							<td class="buttoncontainer">
								<a href="" id="tape-table" title="View data in a table" class="cassettetape-button cassettetape-button-view">#</a>
								<a href="" id="tape-chart" title="View data in charts" class="cassettetape-button cassettetape-button-view cassettetape-button-selected">~</a>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
	<g:render template="/templates/version"/>
</div>

<div id="chart-view">
	<div id="chart1" class="margin-auto;">
		<div id="chart_1" class="float-left chart"></div>
		<div id="legend_1" class="float-left legend" title="click to expand or collapse chart"></div>
	</div>
	<div id="chart2" class="margin-auto;">
		<div id="chart_2" class="float-left chart"></div>
		<div id="legend_2" class="float-left legend" title="click to expand or collapse chart"></div>
	</div>
	<div id="chart3" class="margin-auto;">
		<div id="chart_3" class="float-left chart"></div>
		<div id="legend_3" class="float-left legend" title="click to expand or collapse chart"></div>
	</div>
	<div id="chart4" class="margin-auto;">
		<div id="chart_4" class="float-left chart"></div>
		<div id="legend_4" class="float-left legend" title="click to expand or collapse chart"></div>
	</div>
	<div id="all-legends" style="display:none;">
		<div style="padding-top:40px;">&nbsp;</div>
	</div>
	<div id="chartdf" class="margin-auto;">
		<div id="chart_df" class="float-left chart taller-chart" style="margin-top:5px;height:155px;"></div>
		<div id="legend_df" class="float-left legend" style="margin-top:15px;" title="click to expand or collapse chart"></div>
		<div id="all-legendsf" style="display:none;">
			<div style="padding-top:40px;">&nbsp;</div>
		</div>
	</div>
	<div id="legend_all" class="float-left legend legend_all" title="click to collapse chart" style="display:none;">ALL</div>
</div>
<div id="table-view" style="margin:auto;float:left;display:none;"></div>

<script type="text/javascript">
$(function(){
	var update_all_timeout = null;
	var update_data_at_marker_timeout = null;
	var refresh_rate = ${rr_value};
	var status_refresh_rate = 5000; // in ms
	var realtime = true;
	var end_epoch; // JS in ms
	var h_scale = 1 * $('#horizontal_scale').val(); // in s
	var chart_view = true; // table view otherwise
	var safe_absolutes_blink = true;
	var safe_absolutes_set = true;
	var safe_absolutes_el = 3;
	var safe_absolutes_blink_timeout = null;
	var safe_absolutes_error = false;
	var last_scalar_data_epoch = 0; // JS in ms
	var last_vector_data_epoch = 0; // JS in ms
	var reset_timeout = null;
	var settings_changed = false;

	function resetCharts() {
		var c = $('#chart_1').height()>300 ? 1 : $('#chart_2').height()>300 ? 2 : $('#chart_3').height()>300 ? 3 : $('#chart_4').height()>300 ? 4 : $('#chart_df').height()>300 ? 5 : 0;
		if (c > 0) {
			console.log('reset charts layout');
			$('#chart_df').height(155);
			$('#chart_1').height(135);
			$('#chart1').show();
			$('#chart_2').height(135);
			$('#chart2').show();
			$('#chart_3').height(135);
			$('#chart3').show();
			$('#chart_4').height(135);
			$('#chart4').show();
			$('#all-legends').hide();
			$('#all-legendsf').hide();
			$('#legend_1b').show();
			$('#legend_2b').show();
			$('#legend_3b').show();
			$('#legend_4b').show();
		}
		return c;
	}
	function toggleChart(n) {
		var c = resetCharts();
		if (c != n) {
			if (n != 1) $('#chart1').hide();
			if (n != 2) $('#chart2').hide();
			if (n != 3) $('#chart3').hide();
			if (n != 4) $('#chart4').hide();
			if (n == 1) { $('#chart_1').height(540); $('#legend_1b').hide(); }
			if (n == 2) { $('#chart_2').height(540); $('#legend_2b').hide(); }
			if (n == 3) { $('#chart_3').height(540); $('#legend_3b').hide(); }
			if (n == 4) { $('#chart_4').height(540); $('#legend_4b').hide(); }
			$('#all-legends').show();
			settings_changed = true;
		}
		resetResetTimeout();
		updateAllCharts();
	}
	function toggleChartDF() {
		var c = resetCharts();
		if (c == 0) {
			$('#chart1').hide();
			$('#chart2').hide();
			$('#chart3').hide();
			$('#chart4').hide();
			$('#chart_df').height(600);
			$('#all-legendsf').show();
			settings_changed = true;
		}
		resetResetTimeout();
		updateAllCharts();
	}
	// input handlers
	$('#legend_1').click(function(e) { toggleChart(1); });
	$('#legend_2').click(function(e) { toggleChart(2); });
	$('#legend_3').click(function(e) { toggleChart(3); });
	$('#legend_4').click(function(e) { toggleChart(4); });
	$('#legend_df').click(function(e) { toggleChartDF(); });
	$('#legend_all').click(function(e) { resetCharts(); });
	$('#legend_allf').click(function(e) { resetCharts(); });

	$('#refresh_rate').change(function(e) {
		setRefreshRate();
		resetResetTimeout(true);
		updateAllCharts();
	});

	$('#vertical_scale').change(function(e) {
		console.log("vertical scale set to " + $('#vertical_scale').val())
		resetResetTimeout(true);
		updateAllCharts();
	});

	$('#horizontal_scale').change(function(e) {
		setHScale();
		syncupDateTimes();
		resetResetTimeout(true);
		updateAllCharts();
	});

	$('#tape-play').click(function(e) {
		resetResetTimeout();
		if (realtime) return false;
		console.log('play');
		setPlay();
		syncupDateTimes();
		updateAllCharts();
		return false;
	});
	$('#tape-pause').click(function(e) {
		resetResetTimeout();
		if (!realtime) return false;
		console.log('pause');
		$('#tape-play').removeClass('cassettetape-button-selected');
		$('#tape-pause').addClass('cassettetape-button-selected');
		$('#tape-backward').removeClass('cassettetape-button-disabled');
		$('#tape-forward').removeClass('cassettetape-button-disabled');
		$('#refresh_rate-edit').hide();
		$('#refresh_rate-readonly').show();
		$('#startdate-edit').show();
		$('#startdate-readonly').hide();
		$('#enddate-edit').show();
		$('#enddate-readonly').hide();
		realtime = false;
		syncupDateTimes();
		settings_changed = true;
		updateAllCharts();
		return false;
	});
	$('#tape-backward').click(function(e) {
		resetResetTimeout();
		if (realtime) return false;
		setDateForward(false);
		settings_changed = true;
		updateAllCharts();
		return false;
	});
	$('#tape-forward').click(function(e) {
		resetResetTimeout();
		if (realtime) return false;
		setDateForward(true);
		settings_changed = true;
		updateAllCharts();
		return false;
	});

	$("#startdate").change(function(e) {
		resetResetTimeout(true);
		console.log('new start date');
		syncupDateTimes('startdate');
		updateAllCharts();
	});
	$("#sd_hour").change(function(e) {
		resetResetTimeout(true);
		console.log('new start hour');
		syncupDateTimes('startdate');
		updateAllCharts();
	});
	$("#sd_minute").change(function(e) {
		resetResetTimeout(true);
		console.log('new start minute');
		syncupDateTimes('startdate');
		updateAllCharts();
	});
	$("#enddate").change(function(e) {
		resetResetTimeout(true);
		console.log('new end date');
		syncupDateTimes('enddate');
		updateAllCharts();
	});
	$("#ed_hour").change(function(e) {
		resetResetTimeout(true);
		console.log('new end hour');
		syncupDateTimes('enddate');
		updateAllCharts();
	});
	$("#ed_minute").change(function(e) {
		resetResetTimeout(true);
		console.log('new end minute');
		syncupDateTimes('enddate');
		updateAllCharts();
	});

	$('#tape-chart').click(function(e) {
		resetResetTimeout();
		if (chart_view) return false;
		console.log('show charts');
		setChartView();
		updateAllCharts();
		return false;
	});
	$('#tape-table').click(function(e) {
		resetResetTimeout(true);
		if (!chart_view) return false;
		console.log('show table');
		$('#tape-table').addClass('cassettetape-button-selected');
		$('#tape-chart').removeClass('cassettetape-button-selected');
		$('#chart-view').hide();
		$('#table-view').show();
		chart_view = false;
		updateAllCharts();
		return false;
	});

	$('#absolutesstatus-box').mouseover(function(e) {
		if (!safe_absolutes_error) $('#absolutesstatus-info').show();
	});
	$('#absolutesstatus-box').mouseout(function(e) {
		$('#absolutesstatus-info').hide();
	});

	function setRefreshRate() {
		refresh_rate = 1 * $('#refresh_rate').val();
		console.log("refresh rate set to "+refresh_rate);
	}
	function setHScale() {
		h_scale = 1 * $('#horizontal_scale').val();
		console.log("horizontal scale set to "+h_scale+"s");
		var h_scale_hr = $('#horizontal_scale option:selected').text();
		$('#tape-backward').attr('title','Move back in time '+h_scale_hr);
		$('#tape-forward').attr('title','Move forward in time '+h_scale_hr);
	}
	function setPlay() {
		$('#tape-play').addClass('cassettetape-button-selected');
		$('#tape-pause').removeClass('cassettetape-button-selected');
		$('#tape-backward').addClass('cassettetape-button-disabled');
		$('#tape-forward').addClass('cassettetape-button-disabled');
		$('#refresh_rate-edit').show();
		$('#refresh_rate-readonly').hide();
		$('#startdate-edit').hide();
		$('#startdate-readonly').show();
		$('#enddate-edit').hide();
		$('#enddate-readonly').show();
		realtime = true;
	}
	function setChartView() {
		$('#tape-chart').addClass('cassettetape-button-selected');
		$('#tape-table').removeClass('cassettetape-button-selected');
		$('#table-view').hide();
		$('#chart-view').show();
		chart_view = true;
	}

	function xaxisTimeFormatter(val, axis) {
		return epochToAxisTime(val, h_scale<600);
	}

	var options_template = {
		series: {
			shadowSize: 0 // drawing is faster without shadows
		},
		yaxis: {
			show: true,
			labelWidth: 48
		},
		xaxis: {
			show: true,
			mode: "time",
			timeformat: '',
			tickFormatter: null,
			twelveHourClock: false
		},
		grid: {
			clickable: true,
			hoverable: true,
			autoHighlight: false,
			borderWidth: 1,
			borderColor: "#ccc",
			backgroundColor: { colors: ["#fff", "#f0f0f0"] }
		},
		crosshair: {
			mode: "x"
		},
		legend: {
			show: true,
			noColumns: 1
		}
	};
	var options1 = {legend:{container:$("#legend_1")}}; $.extend(true, options1, options_template);
	var options2 = {legend:{container:$("#legend_2")}}; $.extend(true, options2, options_template);
	var options3 = {legend:{container:$("#legend_3")}}; $.extend(true, options3, options_template);
	var options4 = {legend:{container:$("#legend_4")}}; $.extend(true, options4, options_template);
	var optionsDF= {legend:{container:$("#legend_df")}};$.extend(true, optionsDF,options_template);
	optionsDF.xaxis.timeformat = "%d %b %H:%M:%S";
	optionsDF.xaxis.tickFormatter = xaxisTimeFormatter;

	var plot1 = $.plot($("#chart_1"), [{label:"F", data:[], color:2}], options1);
	var plot2 = $.plot($("#chart_2"), [{label:"H", data:[], color:4}], options2);
	var plot3 = $.plot($("#chart_3"), [{label:"E", data:[], color:5}], options3);
	var plot4 = $.plot($("#chart_4"), [{label:"Z", data:[], color:3}], options4);
	var plotDF= $.plot($("#chart_df"),[{label:"&Delta;F", data:[], color:5}], optionsDF);

	$("#chart_1").bind("plotclick", function (event, pos, item) { toggleChart(1); });
	$("#chart_2").bind("plotclick", function (event, pos, item) { toggleChart(2); });
	$("#chart_3").bind("plotclick", function (event, pos, item) { toggleChart(3); });
	$("#chart_4").bind("plotclick", function (event, pos, item) { toggleChart(4); });
	$("#chart_df").bind("plotclick",function (event, pos, item) { toggleChartDF();});

	function setCrosshair(pos, c) {
		if (c != 'f') plot1.setCrosshair(pos);
		if (c != 'h') plot2.setCrosshair(pos);
		if (c != 'e') plot3.setCrosshair(pos);
		if (c != 'z') plot4.setCrosshair(pos);
		if (c != 'df') plotDF.setCrosshair(pos);
		if (!update_data_at_marker_timeout)
			update_data_at_marker_timeout = setTimeout(function(){updateDataAtMarker(pos, c)}, 50);
	}
	$("#chart_1").bind("plothover", function (event, pos, item) { setCrosshair(pos, 'f'); });
	$("#chart_2").bind("plothover", function (event, pos, item) { setCrosshair(pos, 'h'); });
	$("#chart_3").bind("plothover", function (event, pos, item) { setCrosshair(pos, 'e'); });
	$("#chart_4").bind("plothover", function (event, pos, item) { setCrosshair(pos, 'z'); });
	$("#chart_df").bind("plothover",function (event, pos, item) { setCrosshair(pos, 'df');});

	function clearCrosshair(c) {
		if (c != 1) plot1.clearCrosshair();
		if (c != 2) plot2.clearCrosshair();
		if (c != 3) plot3.clearCrosshair();
		if (c != 4) plot4.clearCrosshair();
		if (c != 5) plotDF.clearCrosshair();
		updateDataAtMarker();
	}
	$("#chart_1").mouseleave(function (event, pos, item) { clearCrosshair(1); });
	$("#chart_2").mouseleave(function (event, pos, item) { clearCrosshair(2); });
	$("#chart_3").mouseleave(function (event, pos, item) { clearCrosshair(3); });
	$("#chart_4").mouseleave(function (event, pos, item) { clearCrosshair(4); });
	$("#chart_df").mouseleave(function(event, pos, item) { clearCrosshair(5); });

	$('#absolutesstatus').click(function(e) {
		if (!safe_absolutes_blink) return;
		safe_absolutes_blink = false;
		console.log('quit blinking');
	});

	/** Sync up read only start and end date strings with graph */
	function syncupDateTimesRO() {
		var axes = plot1.getAxes(),
			startdatetime = '',
			enddatetime = '';
		if (axes) {
			startdatetime = epochToDateTime(axes.xaxis.min);
			enddatetime = epochToDateTime(axes.xaxis.max);
		}
		$('#startdate').val('');
		$('#sd_hour').val('00');
		$('#sd_minute').val('00');
		$('#enddate').val('');
		$('#ed_hour').val('00');
		$('#ed_minute').val('00');
		$('#startdate-readonly-value').text(startdatetime);
		$('#enddate-readonly-value').text(enddatetime);
	}

	/** Sync up start and end date field */
	function syncupDateTimes(from) {
		console.log('syncup dates from '+from);
		var axes = plot1.getAxes(),
			start_epoch = 0,
			start_datefields,
			end_datefields,
			h_scale_ms = h_scale * 1000;
		if (from === 'startdate') {
			start_epoch = dateFieldsToEpoch($('#startdate').val(), $('#sd_hour').val(), $('#sd_minute').val());
			end_epoch = start_epoch + h_scale_ms;
		} else if (from === 'enddate') {
			end_epoch = dateFieldsToEpoch($('#enddate').val(), $('#ed_hour').val(), $('#ed_minute').val());
			start_epoch = end_epoch - h_scale_ms;
		} else if (from === 'chart-begin') {
			if (axes) {
				start_epoch = axes.xaxis.min;
				end_epoch = start_epoch + h_scale_ms;
			}
		} else if (from === 'chart-end') {
			if (axes) {
				end_epoch = axes.xaxis.max;
				start_epoch = end_epoch - h_scale_ms;
			}
		} else if (from === 'now') {
			var now = new Date();
			end_epoch = now.getTime();
			start_epoch = end_epoch - h_scale_ms;
		} else { // from internal var end_epoch
			start_epoch = end_epoch - h_scale_ms;
		}
		if (realtime) {
			syncupDateTimesRO();
		} else {
			start_datefields = epochToDateFields(start_epoch);
			end_datefields = epochToDateFields(end_epoch);
			$('#startdate').val(start_datefields.mmddyyyy);
			$('#sd_hour').val(start_datefields.hh);
			$('#sd_minute').val(start_datefields.mm);
			$('#enddate').val(end_datefields.mmddyyyy);
			$('#ed_hour').val(end_datefields.hh);
			$('#ed_minute').val(end_datefields.mm);
			console.log("rt:"+realtime+" start epoch = "+start_epoch+" ("+start_datefields.mmddyyyy+" "+start_datefields.hh+":"+start_datefields.mm+":"+start_datefields.ss+"), end epoch = "+end_epoch+" ("+end_datefields.mmddyyyy+" "+end_datefields.hh+":"+end_datefields.mm+":"+end_datefields.ss+")");
		}
	}

	function setDateForward(isfw) {
		var direction = isfw ? 1 : -1,
			now_epoch = new Date().getTime(),
			new_end_epoch,
			amount = h_scale;
		if (amount < 60) amount = 60; // at least 1 min
		console.log('set date '+(isfw?'forward':'backward')+" "+amount+"s");
		new_end_epoch = end_epoch + direction * amount * 1000;
		end_epoch = new_end_epoch > now_epoch ? now_epoch : new_end_epoch; // not in the future
		syncupDateTimes();
	}

	function getDataAtCrosshair(pos) {
		var xpos;
		var axes1  = plot1.getAxes();
		var dataset1  = plot1.getData();
		var dataset2  = plot2.getData();
		var dataset3  = plot3.getData();
		var dataset4  = plot4.getData();
		var datasetDF = plotDF.getData();
		var i, j, ilen, jlen, series;
		var x, f, h, e, z, df, epoch, from;

		if (typeof pos !== 'undefined' && pos.x >= axes1.xaxis.min && pos.x < axes1.xaxis.max) {
			xpos = pos.x;
			from = 'atmarker';
		} else {
			xpos = axes1.xaxis.max - 2200;
			from = 'latest';
		}
		for (i=0, ilen=dataset1.length; i<ilen; ++i) {
			series = dataset1[i];
			// find the nearest points, x-wise
			for (j=0, jlen=series.data.length; j<jlen; ++j) {
				if (series.data[j][0] > xpos) {
					x = series.data[j][0];
					f = series.data[j][1];
					break;
		}	}	}
		for (i=0, ilen=dataset2.length; i<ilen; ++i) {
			series = dataset2[i];
			for (j=0, jlen=series.data.length; j<jlen; ++j) {
				if (series.data[j][0] > xpos) {
					h = series.data[j][1];
					break;
		}	}	}
		for (i=0, ilen=dataset3.length; i<ilen; ++i) {
			series = dataset3[i];
			for (j=0, jlen=series.data.length; j<jlen; ++j) {
				if (series.data[j][0] > xpos) {
					e = series.data[j][1];
					break;
		}	}	}
		for (i=0, ilen=dataset4.length; i<ilen; ++i) {
			series = dataset4[i];
			for (j=0, jlen=series.data.length; j<jlen; ++j) {
				if (series.data[j][0] > xpos) {
					z = series.data[j][1];
					break;
		}	}	}
		for (i=0, ilen=datasetDF.length; i<ilen; ++i) {
			series = datasetDF[i];
			for (j=0, jlen=series.data.length; j<jlen; ++j) {
				if (series.data[j][0] > xpos) {
					df = series.data[j][1];
					break;
		}	}	}
		if (x) epoch = new Date(x);
		return {
			from: from,
			epoch: epoch,
			f: f,
			h: h,
			e: e,
			z: z,
			df: df
		}
	}

	function updateDataAtMarker(pos, name) {
		$('.data-row').removeClass('highlight');
		if (typeof name !== 'undefined') $('#'+name+'_axisrow').addClass('highlight');
		updateData(getDataAtCrosshair(pos));
	}

	function updateData(data) {
		if (update_data_at_marker_timeout) clearTimeout(update_data_at_marker_timeout);
		update_data_at_marker_timeout = null;

		$('#data-header-live').hide();
		$('#data-header-latest').hide();
		$('#data-header-atmarker').hide();
		if (data.from === 'atmarker') {
			$('#data-header-atmarker').show();
		} else {
			if (realtime) {
				$('#data-header-live').show();
			} else {
				$('#data-header-latest').show();
		}	}

		var ts = new Date(data.epoch);
		if (ts.toString() == "Invalid Date" || ts.toString() == "NaN")
			console.log("updateData(): invalid date from epoch = "+data.epoch);
		else {
			$("#x_part_a_axis").html(ts.toUTCDayMonthFullYear());
			$("#x_part_b_axis").html(pad2(ts.getUTCHours()) + ":" + pad2(ts.getUTCMinutes()) + ":" + pad2(ts.getUTCSeconds()));
			var seconds_from_midnight_utc = (ts.getUTCHours() * 3600) + (ts.getUTCMinutes() * 60) + ts.getUTCSeconds();
			var jday = ts.getUTCDayOfYear();
			$("#data_date_utc").html(seconds_from_midnight_utc);
			$("#data_year_date").html(jday);
		}
		$("#f_axis").html(data.f ? data.f.toFixed(2) : '');
		$("#h_axis").html(data.h ? data.h.toFixed(2) : '');
		$("#e_axis").html(data.e ? data.e.toFixed(2) : '');
		$("#z_axis").html(data.z ? data.z.toFixed(2) : '');
		$("#df_axis").html(data.df ? data.df.toFixed(2) : '');
	}

	function updateAllCharts(from_timer_call) {
		if (update_all_timeout) clearTimeout(update_all_timeout);
		update_all_timeout = null;
		if (!from_timer_call) showBusy();

		var date='', hh='00', mm='00';
		if (!realtime) {
			date = $("#enddate").val();
			hh = $("#ed_hour").val();
			mm = $("#ed_minute").val();
		}
		var baseUrlAll = "${createLink(controller:'main',action:'magnetometer_data')}" +
			"?horizontal_scale="+h_scale + "&startdate="+date + "&hour="+hh + "&minute="+mm;

		function onDataReceived(series) {
			var scale = $('#vertical_scale').val();
			var i, dlen, data = {};
			var ticks, plotdata, midistance, centerline;

			end_epoch = series.end_epoch * 1000;
			console.log('end epoch = '+end_epoch+" "+epochToDateTime(end_epoch));

			if (chart_view) {
				plot1.setData([series.f]);
				plotdata = plot1.getData();
				if (scale > 0.5) { // not auto
					midistance = Math.abs(plotdata[0].yaxis.datamax - plotdata[0].yaxis.datamin) / 2;
					centerline = plotdata[0].yaxis.datamax - midistance;
					ticks = $("#chart_1").height() > 300 ? 10.5 : 2.5;
					options1.yaxis.tickSize = scale;
					options1.yaxis.minTickSize = scale;
					options1.yaxis.tickDecimals = 0;
					options1.yaxis.max = centerline + ticks * scale; // 2 because a chart displays 4 divisions (2 + 2)
					options1.yaxis.min = centerline - ticks * scale;
				} else { // auto
					options1.yaxis.tickSize = null;
					options1.yaxis.minTickSize = null;
					options1.yaxis.tickDecimals = null;
					options1.yaxis.max = null;
					options1.yaxis.min = null;
				}
				plot1 = $.plot($("#chart_1"),[series.f], options1);
				plot1.setupGrid();

				plot2.setData([series.h]);
				plotdata = plot2.getData();
				if (scale > 0.5) { // not auto
					midistance = Math.abs(plotdata[0].yaxis.datamax - plotdata[0].yaxis.datamin) / 2;
					centerline = plotdata[0].yaxis.datamax - midistance;
					ticks = $("#chart_2").height() > 300 ? 10.5 : 2.5;
					options2.yaxis.tickSize = scale;
					options2.yaxis.minTickSize = scale;
					options2.yaxis.tickDecimals = 0;
					options2.yaxis.max = centerline + ticks * scale;
					options2.yaxis.min = centerline - ticks * scale;
				} else { // auto
					options2.yaxis.tickSize = null;
					options2.yaxis.minTickSize = null;
					options2.yaxis.tickDecimals = null;
					options2.yaxis.max = null;
					options2.yaxis.min = null;
				}
				plot2 = $.plot($("#chart_2"),[series.h], options2);
				plot2.setupGrid();

				plot3.setData([series.e]);
				plotdata = plot3.getData();
				if (scale > 0.5) { // not auto
					midistance = Math.abs(plotdata[0].yaxis.datamax - plotdata[0].yaxis.datamin) / 2;
					centerline = plotdata[0].yaxis.datamax - midistance;
					ticks = $("#chart_3").height() > 300 ? 10.5 : 2.5;
					options3.yaxis.tickSize = scale;
					options3.yaxis.minTickSize = scale;
					options3.yaxis.tickDecimals = 0;
					options3.yaxis.max = centerline + ticks * scale;
					options3.yaxis.min = centerline - ticks * scale;
				} else { // auto
					options3.yaxis.tickSize = null;
					options3.yaxis.minTickSize = null;
					options3.yaxis.tickDecimals = null;
					options3.yaxis.max = null;
					options3.yaxis.min = null;
				}
				plot3 = $.plot($("#chart_3"),[series.e], options3);
				plot3.setupGrid();

				plot4.setData([series.z]);
				plotdata = plot4.getData();
				if (scale > 0.5) { // not auto
					midistance = Math.abs(plotdata[0].yaxis.datamax - plotdata[0].yaxis.datamin) / 2;
					centerline = plotdata[0].yaxis.datamax - midistance;
					ticks = $("#chart_4").height() > 300 ? 10.5 : 2.5;
					options4.yaxis.tickSize = scale;
					options4.yaxis.minTickSize = scale;
					options4.yaxis.tickDecimals = 0;
					options4.yaxis.max = centerline + ticks * scale;
					options4.yaxis.min = centerline - ticks * scale;
				} else { // auto
					options4.yaxis.tickSize = null;
					options4.yaxis.minTickSize = null;
					options4.yaxis.tickDecimals = null;
					options4.yaxis.max = null;
					options4.yaxis.min = null;
				}
				plot4 = $.plot($("#chart_4"),[series.z], options4);
				plot4.setupGrid();

				plotDF.setData([series.df]);
				plotdata = plotDF.getData();
				if (scale > 0.5) { // not auto
					midistance = Math.abs(plotdata[0].yaxis.datamax - plotdata[0].yaxis.datamin) / 2;
					centerline = plotdata[0].yaxis.datamax - midistance;
					ticks = $("#chart_df").height() > 300 ? 12 : 2.4;
					optionsDF.yaxis.tickSize = scale;
					optionsDF.yaxis.minTickSize = scale;
					optionsDF.yaxis.tickDecimals = 0;
					optionsDF.yaxis.max = centerline + ticks * scale;
					optionsDF.yaxis.min = centerline - ticks * scale;
				} else { // auto
					optionsDF.yaxis.tickSize = null;
					optionsDF.yaxis.minTickSize = null;
					optionsDF.yaxis.tickDecimals = null;
					optionsDF.yaxis.max = null;
					optionsDF.yaxis.min = null;
				}
				plotDF = $.plot($("#chart_df"),[series.df], optionsDF);
				plotDF.setupGrid();

				plot1.draw();
				plot2.draw();
				plot3.draw();
				plot4.draw();
				plotDF.draw();
				plot1.clearCrosshair();
				plot2.clearCrosshair();
				plot3.clearCrosshair();
				plot4.clearCrosshair();
				plotDF.clearCrosshair();
			} else {
				updateTable(series);
			}
			// get most current data
			dlen = series.f.data.length;
			for (i = dlen-1; i >= 0; i--) {
				if (series.f.data[i][1] == null && series.h.data[i][1] == null && series.e.data[i][1] == null && series.z.data[i][1] == null) continue;
				data.from = 'live';
				data.epoch = series.f.data[i][0];
				data.f = series.f.data[i][1];
				data.h = series.h.data[i][1];
				data.e = series.e.data[i][1];
				data.z = series.z.data[i][1];
				data.df = series.df.data[i][1];
				break;
			}
			updateData(data);

			// last scalar data = f
			dlen = series.f.data.length;
			for (i = dlen-1; i >= 0; i--) {
				if (series.f.data[i][1] == null) continue;
				last_scalar_data_epoch = series.f.data[i][0];
				break;
			}
			// last vector data = h,e,z
			dlen = series.h.data.length;
			for (i = dlen-1; i >= 0; i--) {
				if (series.h.data[i][1] == null) continue;
				last_vector_data_epoch = series.h.data[i][0];
				break;
			}
			updateDataStreamStatus();

			// absolutes status
			safe_absolutes_error = false;
			if (safe_absolutes_el != series.absolutes_status) safe_absolutes_set = true;
			safe_absolutes_el = series.absolutes_status;
			if (safe_absolutes_set) {
				if (safe_absolutes_blink_timeout) clearTimeout(safe_absolutes_blink_timeout);
				$('#absolutestatus-err').hide();
				switch(safe_absolutes_el) {
				case 0: // ok
					$('#absolutestatus-ok').show();
					$('#absolutestatus-warn').hide();
					$('#absolutestatus-nored').hide();
					$('#absolutestatus-nooff').hide();
					safe_absolutes_blink = false;
					break;
				case 1: // warning
					$('#absolutestatus-ok').hide();
					$('#absolutestatus-warn').show();
					$('#absolutestatus-nored').hide();
					$('#absolutestatus-nooff').hide();
					safe_absolutes_blink = false;
					break;
				default: // critical
					$('#absolutestatus-ok').hide();
					$('#absolutestatus-warn').hide();
					$('#absolutestatus-nored').show();
					$('#absolutestatus-nooff').hide();
					safe_absolutes_blink = true;
				}
				console.log('update safe absolutes status '+safe_absolutes_el);
				safe_absolutes_set = false;
				if (safe_absolutes_blink) safe_absolutes_blink_timeout = setTimeout(blinkAbsolutesStatus, 400);
			}
			$('#absolutesstatus-info').html(series.absolutes_info);

			syncupDateTimes();
		}

		function onError() {
			if (safe_absolutes_blink_timeout) clearTimeout(safe_absolutes_blink_timeout);
			console.log("data updater: on error");
			safe_absolutes_blink = false;
			safe_absolutes_error = true;
			$('#absolutestatus-ok').hide();
			$('#absolutestatus-warn').hide();
			$('#absolutestatus-nored').hide();
			$('#absolutestatus-nooff').hide();
			$('#absolutestatus-err').show();
			safe_absolutes_set = true;
		}

		$.ajax({
			url: baseUrlAll,
			method: 'GET',
			dataType: 'json',
			success: onDataReceived,
			error: onError
		}).done(function(){
			showReady();
			if (realtime)
				update_all_timeout = setTimeout(function(){updateAllCharts(true);}, refresh_rate * 1000);
			else
				update_all_timeout = null;
		});
	}

	function updateTable(series) {
		var t = "<table><th>F</th><th>H</th><th>E</th><th>Z</th><th>&Delta;F</th><th>Time</th>";
		var i, dlen = series.f.data.length;
		var is_data = false;

		for (i = dlen-1; i >= 0; i--) {
			if (series.f.data[i][1] == null && series.h.data[i][1] == null && series.e.data[i][1] == null && series.z.data[i][1] == null) continue;
			var d = new Date(Number(series.f.data[i][0]));
			var ss = d.getUTCHours() * 3600 + d.getUTCMinutes() * 60 + d.getUTCSeconds();
 			var ts = d.toUTCDayMonthFullYear() + " " + pad2(d.getUTCHours()) + ":" + pad2(d.getUTCMinutes()) + ":" + pad2(d.getUTCSeconds()) + " (" + ss + ")";
			t += "<tr><td>" +
				(series.f.data[i][1] ? series.f.data[i][1].toFixed(2) : 'null') + "</td><td>" +
				(series.h.data[i][1] ? series.h.data[i][1].toFixed(2) : 'null') + "</td><td>" +
				(series.e.data[i][1] ? series.e.data[i][1].toFixed(2) : 'null') + "</td><td>" +
				(series.z.data[i][1] ? series.z.data[i][1].toFixed(2) : 'null') + "</td><td>" +
				(series.df.data[i][1]? series.df.data[i][1].toFixed(2): 'null') + "</td><td>" +
				ts + "</td></tr>";
			is_data = true;
		}
		t += "</table>";
		if (!is_data) t = "<p>&nbsp; No data for the selected time window.</p>";
		$("#table-view").html(t);
	}

	function updateDataStreamStatus(err) {
		var age, d = 'n/a';
		if (!realtime) {
			$('#scalardata-ok').hide();
			$('#scalardata-no').hide();
			$('#scalardata-off').show();
			$('#scalardata-age').html(d).attr('title','(not in real time mode)');
			$('#vectordata-ok').hide();
			$('#vectordata-no').hide();
			$('#vectordata-off').show();
			$('#vectordata-age').html(d).attr('title','(not in real time mode)');
			return;
		}
		if (err) {
			$('#scalardata-ok').hide();
			$('#scalardata-no').hide();
			$('#scalardata-off').show();
			$('#scalardata-age').html('(no connection)').attr('title','(not connected)');
			$('#vectordata-ok').hide();
			$('#vectordata-no').hide();
			$('#vectordata-off').show();
			$('#vectordata-age').html('(no connection)').attr('title','(not connected)');
			return;
		}
		$('#scalardata-off').hide();
		age = end_epoch - last_scalar_data_epoch;
		if (age < 1000 * ${scalar_data_check}) {
			$('#scalardata-no').hide();
			$('#scalardata-ok').show();
			d = msToHuman(age); //+" - "+end_epoch +", "+last_scalar_data_epoch+", "+(1000 * ${scalar_data_check})+" <");
		} else {
			$('#scalardata-ok').hide();
			$('#scalardata-no').show();
			d = 'n/a';
		}
		$('#scalardata-age').html(d).attr('title',epochToDateTime(last_scalar_data_epoch));

		$('#vectordata-off').hide();
		age = end_epoch - last_vector_data_epoch;
		if (age < 1000 * ${vector_data_check}) {
			$('#vectordata-no').hide();
			$('#vectordata-ok').show();
			d = msToHuman(age); //+" - "+end_epoch +", "+last_vector_data_epoch+", "+(1000 * ${vector_data_check}));
		} else {
			$('#vectordata-ok').hide();
			$('#vectordata-no').show();
			d = 'n/a';
		}
		$('#vectordata-age').html(d).attr('title',epochToDateTime(last_vector_data_epoch));
	}

	function updateStatusPanel() {

		function onDataReceived(data) {
			$('#connectionstatus-err').hide();
			if (data.connected) {
				$('#connectionstatus-no').hide();
				$('#connectionstatus-ok').show();
			} else {
				$('#connectionstatus-ok').hide();
				$('#connectionstatus-no').show();
				updateDataStreamStatus(true);
			}

			var doc2 = "<table><tr><td width='150'>Voltage</td><td>" + data.voltage + "</td></tr></table>";
			$("#voltage").html(doc2);

			var doc3 = "<table><tr><td width='150'>Electronics</td><td><span class='temp " + data.el_temp_state + "'>" + data.electronics.toFixed(1) + "</span></td></tr>";
			doc3 += "<tr><td>Fluxgate</td><td><span class='temp " + data.fg_temp_state + "'>" + data.fluxgate.toFixed(1) + "</span></td></tr>";
			doc3 += "<tr><td>Total Field</td><td><span class='temp " + data.tf_temp_state + "'>" + data.totalfield.toFixed(1) + "</span></td></tr>";
			doc3 += "<tr><td>Outside</td><td>" + data.outside.toFixed(1) + "</td></tr></table>";
			$("#temperature").html(doc3);

			var doc4, d = new Date(data.currenttime);
			if (d.toString() == "Invalid Date" || d.toString() == "NaN") {
				doc4 = "(no data)";
			} else {
				var timestring = pad2(d.getUTCHours()) + ":" + pad2(d.getUTCMinutes()) + ":" + pad2(d.getUTCSeconds());
				var months = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
				var month = months[d.getUTCMonth()];
				var mdystring =  d.getUTCDate() + " " + month + " " + d.getUTCFullYear();
				doc4 = "<table><tr><td>" + mdystring + " " + timestring + "</td></tr></table>";
			}
			$("#currenttime").html(doc4);
		}

		function onError() {
			$('#connectionstatus-err').show();$('#connectionstatus-ok').hide();$('#connectionstatus-no').hide();
			$("#voltage").html("<table><tr><td>Error!</td></tr></table>");
			$("#temperature").html("<table><tr><td>Error!</td></tr></table>");
			$("#currenttime").html("<table><tr><td>Error!</td></tr></table>");
			updateDataStreamStatus(true);
			// try connecting again in a minute
			setTimeout(updateStatusPanel, 60000);
			setTimeout(updateAllCharts, 60100);
		}

		$.ajax({
			url: "${createLink(controller:'main',action:'status_data')}",
			method: 'GET',
			dataType: 'json',
			success: onDataReceived,
			error: onError
		}).done(function(){
			setTimeout(updateStatusPanel, status_refresh_rate);
		});
	}

	function blinkAbsolutesStatus() {
		var ledon = $('#absolutestatus-nored'), ledoff = $('#absolutestatus-nooff');
		var is_off = $(ledoff).is(":visible");
		if (safe_absolutes_blink) safe_absolutes_blink_timeout = setTimeout(blinkAbsolutesStatus, is_off ? 400 : 150);
		if (!safe_absolutes_blink || (safe_absolutes_blink && is_off)) {
			$(ledoff).hide();
			$(ledon).show();
		} else {
			$(ledon).hide();
			$(ledoff).show();
		}
	}

	function resetSettings() {
		console.log("resetting settings");
		setChartView();
		resetCharts();
		$('#refresh_rate').val(${rr_value}); setRefreshRate();
		$('#horizontal_scale').val(${h_value}); setHScale();
		$('#vertical_scale').val(${v_value});
		setPlay();
		updateAllCharts();
		safe_absolutes_set = true;
		settings_changed = false;
	}

	function resetResetTimeout(set_settings_changed) {
		if (reset_timeout) clearTimeout(reset_timeout);
		if (set_settings_changed === true) settings_changed = true;
		if (!settings_changed) return; // nothing to reset
		reset_timeout = setTimeout(resetSettings, 1000 * ${reset_timeout});
		console.log('settings will reset in ${reset_timeout}s');
	}

	function showBusy() {
		$('#spinner').show();
	}
	function showReady() {
		$('#spinner').hide();
	}
	$('#spinner').css({
		position:"fixed",
		left: ($(window).width()/2 + $(document).scrollLeft()),
		top: ($(window).height()/2 + $(document).scrollTop())
	});

	var e;
	e = $('#legend_1').clone(true); $(e).attr('id','legend_1b'); $('#all-legends').append(e);
	e = $('#legend_2').clone(true); $(e).attr('id','legend_2b'); $('#all-legends').append(e);
	e = $('#legend_3').clone(true); $(e).attr('id','legend_3b'); $('#all-legends').append(e);
	e = $('#legend_4').clone(true); $(e).attr('id','legend_4b'); $('#all-legends').append(e);
	e = $('#legend_all').clone(true); $(e).attr('id','legend_allb').show(); $('#all-legends').append(e);
	e = $('#legend_1').clone(true); $(e).attr('id','legend_1f'); $('#all-legendsf').append(e);
	e = $('#legend_2').clone(true); $(e).attr('id','legend_2f'); $('#all-legendsf').append(e);
	e = $('#legend_3').clone(true); $(e).attr('id','legend_3f'); $('#all-legendsf').append(e);
	e = $('#legend_4').clone(true); $(e).attr('id','legend_4f'); $('#all-legendsf').append(e);
	e = $('#legend_all').clone(true); $(e).attr('id','legend_allf').show(); $('#all-legendsf').append(e);

	$("#startdate").datepicker();
	$("#enddate").datepicker();

	setHScale();
	updateAllCharts();
	updateStatusPanel();
});
</script>
</div>
</body>
</html>

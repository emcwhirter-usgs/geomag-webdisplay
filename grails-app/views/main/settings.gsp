<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/html">
<head>
	<title>MagWorm WebDisplay Settings</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="layout" content="mainapp" />
</head>

<body>
<div id="maincontent">

<g:if test="${!local_station_id}">
	<div class="errors"><ul><li><b>No Station ID defined</b>. Please enter a Local Station ID.</li></ul></div>
</g:if>

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
	$("#tabs li a").click(function(){
		$("div.message").remove();
	});
});
</script>
            <br/>
            <g:if test='${flash.message}'><br/><div class='message'>${flash.message}</div></g:if>
            <div class="demodds">
                <h1 class="firstHeader">Settings</h1>
                <br class="clear-both"/>
                <div id="tabs">
                    <ul>
                        <li><a href="#tab-1">Station Information</a></li>
                        <li><a href="#tab-2">Magnetometers</a></li>
                        <li><a href="#tab-3">Connection Settings</a></li>
                        <li><a href="#tab-4">Absolutes Thresholds</a></li>
                        <li><a href="#tab-5">Defaults</a></li>
<sec:ifLoggedIn>
												<li><a href="#tab-6">Password</a></li>
</sec:ifLoggedIn>
										</ul>
                    
                    <g:form action="savesettings">
                        <div id="tab-1">
                            <table style="border: 1px none;">
                                <tr><td width="20%"><label for='local_station_id'>Local Station ID</label>
                                </td><td><g:textField  name="local_station_id" value="${local_station_id}"/>  
                                </td></tr>
                                <tr><td><label for='local_station_desc'>Local Station Description</label>
                                </td><td><g:textField   name="local_station_desc" value="${local_station_desc}" size="35"/>  
                                </td></tr>
                                <tr><td><label for='co_latitude'>Co-Latitude</label>
                                </td><td><g:textField   name="co_latitude" value="${co_latitude}"/>
                                </td></tr>
                                <tr><td><label for='east_longitude'>East Longitude</label>
                                </td><td><g:textField   name="east_longitude" value="${east_longitude}"/>
                                </td></tr>
                                <tr><td><label for='inclination'>Inclination (min)</label>
                                </td><td><g:textField   name="inclination" value="${inclination}"/>
                                </td></tr>
                                <tr><td><label for='declination'>Declination (min)</label>
                                </td><td><g:textField   name="declination" value="${declination}"/>
                                </td></tr>
                                %{--<tr><td><label for='observer_declination'>Observer Declination</label>--}%
                                %{--</td><td><g:textField   name="observer_declination" value="${observer_declination}"/>--}%
                                %{--</td></tr>--}%
                            </table>
                    </div>
                    
                    <div id="tab-2">
                            <table style="border: 1px;">
                                <tr><td width="15%"><label for='vector_mag_sn'>Vector Mag. S/N</label>
                                </td><td><g:textField  name="vector_mag_sn" value="${vector_mag_sn}"/>  
                                </td></tr>
                                <tr><td><label for='scalar_mag_sn'>Scalar Mag. S/N</label>
                                </td><td><g:textField  name="scalar_mag_sn" value="${scalar_mag_sn}"/>  
                                </td></tr>
                                <tr><td width="15%"><label for='bin_h_const'>Bin H Constant</label>
                                </td><td><g:textField  name="bin_h_const" value="${bin_h_const}"/>  
                                </td></tr>
                                <tr><td width="15%"><label for='bin_e_const'>Bin E Constant</label>
                                </td><td><g:textField  name="bin_e_const" value="${bin_e_const}"/>  
                                </td></tr>
                                <tr><td width="15%"><label for='bin_z_const'>Bin Z Constant</label>
                                </td><td><g:textField  name="bin_z_const" value="${bin_z_const}"/>  
                                </td></tr>
        						<tr><td width="15%"><label for='voltage_h_const'>Voltage H Constant</label>
                                </td><td><g:textField  name="voltage_h_const" value="${voltage_h_const}"/>  
                                </td></tr>
        						<tr><td width="15%"><label for='voltage_e_const'>Voltage E Constant</label>
                                </td><td><g:textField  name="voltage_e_const" value="${voltage_e_const}"/>  
                                </td></tr>
        						<tr><td width="15%"><label for='voltage_z_const'>Voltage Z Constant</label>
                                </td><td><g:textField  name="voltage_z_const" value="${voltage_z_const}"/>  
                                </td></tr>
                            </table>
                    </div>
                    
                    <div id="tab-3">
											<table style="border: 1px;">
											<tr>
												<td width="38%"><label for='exporter_host'>Exporter Host IP Address</label></td>
												<td><g:textField  name="exporter_host" value="${exporter_host}"/></td>
											</tr>
											<tr>
												<td><label for='exporter_port'>Exporter Host TCP Port</label></td>
												<td><g:textField  name="exporter_port" value="${exporter_port}" size="10"/></td>
											</tr>
											<tr>
												<td><label for='heartbeat_interval'>Expected Upstream Exporter Heartbeat Interval (seconds)</label></td>
												<td><g:textField  name="heartbeat_interval" value="${heartbeat_interval}" size="10"/></td>
											</tr>
											<tr>
												<td><label for='scalar_data_check'>Scalar Data Stream Check Window (seconds)</label></td>
												<td><g:textField  name="scalar_data_check" value="${scalar_data_check}" size="10"/></td>
											</tr>
											<tr>
												<td><label for='vector_data_check'>Vector Data Stream Check Window (seconds)</label></td>
												<td><g:textField  name="vector_data_check" value="${vector_data_check}" size="10"/></td>
											</tr>
											</table>
                    </div>
                    
                    <div id="tab-4">
                    	<table style="border: 1px;">
                        <tr><td>
                        		<table style="border: 1px;">
		                        <tr><td><label for='h_bin_minimum'>H Bin Minimum</label>
		                        </td><td><g:textField name="h_bin_minimum" value="${h_bin_minimum}" size="10"/>  
		                        </td></tr>
		                        <tr><td><label for='h_bin_maximum'>H Bin Maximum</label>
		                        </td><td><g:textField  name="h_bin_maximum" value="${h_bin_maximum}" size="10"/>  
		                        </td></tr>
															<tr><td><label for='e_bin_minimum'>E Bin Minimum</label>
															</td><td><g:textField  name="e_bin_minimum" value="${e_bin_minimum}" size="10"/>
															</td></tr>
															<tr><td><label for='e_bin_maximum'>E Bin Maximum</label>
															</td><td><g:textField  name="e_bin_maximum" value="${e_bin_maximum}" size="10"/>
															</td></tr>
															<tr><td><label for='z_bin_minimum'>Z Bin Minimum</label>
		                        </td><td><g:textField  name="z_bin_minimum" value="${z_bin_minimum}" size="10"/>  
								</td></tr>
		                        <tr><td><label for='z_bin_maximum'>Z Bin Maximum</label>
		                        </td><td><g:textField  name="z_bin_maximum" value="${z_bin_maximum}" size="10"/>  
		                        </td></tr>
		                        <tr><td><label for='h_z_e_voltage_tol'>H, E, Z Voltage Tolerance Threshold</label>
		                        </td><td><g:textField  name="h_z_e_voltage_tol" value="${h_z_e_voltage_tol}" size="10"/>  
		                        </td></tr>
															<tr><td><label for='hez_rate_change'>H, E, Z Rate Change Critical Threshold (nt/Hour)</label>
															</td><td><g:textField  name="hez_rate_change" value="${hez_rate_change}" size="10"/>
															</td></tr>
															<tr><td><label for='h_z_e_dead_value'>H, E, Z Dead Values/Hour Threshold</label>
		                        </td><td><g:textField  name="h_z_e_dead_value" value="${h_z_e_dead_value}" size="10"/>  
		                        </td></tr>
		                        <tr><td><label for='f_spike_per_hour'>F Spike/Hour Threshold</label>
		                        </td><td><g:textField  name="f_spike_per_hour" value="${f_spike_per_hour}" size="10"/>  
		                        </td></tr>
															<tr><td><label for='f_spike_amplitude'>F Spike Amplitude Threshold</label>
															</td><td><g:textField  name="f_spike_amplitude" value="${f_spike_amplitude}" size="10"/>
															</td></tr>
															<tr><td><label for='f_spike_resolution'>F Spike Resolution (seconds)</label>
															</td><td><g:textField  name="f_spike_resolution" value="${f_spike_resolution}" size="10"/>
															</td></tr>
															<tr><td><label for='delta_f_critical'>&Delta;F Critical Threshold (nT)</label>
															</td><td><g:textField  name="delta_f_critical" value="${delta_f_critical}" size="10"/>
															</td></tr>
															<tr><td><label for='delta_f_rate_change'>&Delta;F Rate Change Critical Threshold (nT/Hour)</label>
															</td><td><g:textField  name="delta_f_rate_change" value="${delta_f_rate_change}" size="10"/>
															</td></tr>
														</table>
		                </td><td>
														<table style="border: 1px;">
															<tr><td><label for='electronics_max_critical'>Electronics Temperatures Critical Max</label>
															</td><td><g:textField  name="electronics_max_critical" value="${electronics_max_critical}" size="10"/>
															</td></tr>
															<tr><td><label for='electronics_max_warning'>Electronics Temperatures Warning Max</label>
															</td><td><g:textField  name="electronics_max_warning" value="${electronics_max_warning}" size="10"/>
															</td></tr>
															<tr><td><label for='electronics_min_warning'>Electronics Temperatures Warning Min</label>
															</td><td><g:textField  name="electronics_min_warning" value="${electronics_min_warning}" size="10"/>
															</td></tr>
															<tr><td><label for='electronics_min_critical'>Electronics Temperatures Critical Min</label>
															</td><td><g:textField  name="electronics_min_critical" value="${electronics_min_critical}" size="10"/>
															</td></tr>

															<tr><td><label for='proton_max_critical'>Proton Temperatures Critical Max</label>
															</td><td><g:textField  name="proton_max_critical" value="${proton_max_critical}" size="10"/>
															</td></tr>
															<tr><td><label for='proton_max_warning'>Proton Temperatures Warning Max</label>
															</td><td><g:textField  name="proton_max_warning" value="${proton_max_warning}" size="10"/>
															</td></tr>
															<tr><td><label for='proton_min_warning'>Proton Temperatures Warning Min</label>
															</td><td><g:textField  name="proton_min_warning" value="${proton_min_warning}" size="10"/>
															</td></tr>
															<tr><td><label for='proton_min_critical'>Proton Temperatures Critical Min</label>
															</td><td><g:textField  name="proton_min_critical" value="${proton_min_critical}" size="10"/>
															</td></tr>

															<tr><td><label for='fluxgate_max_critical'>Fluxgate Temperatures Critical Max</label>
															</td><td><g:textField  name="fluxgate_max_critical" value="${fluxgate_max_critical}" size="10"/>
															</td></tr>
															<tr><td><label for='fluxgate_max_warning'>Fluxgate Temperatures Warning Max</label>
															</td><td><g:textField  name="fluxgate_max_warning" value="${fluxgate_max_warning}" size="10"/>
															</td></tr>
															<tr><td><label for='fluxgate_min_warning'>Fluxgate Temperatures Warning Min</label>
															</td><td><g:textField  name="fluxgate_min_warning" value="${fluxgate_min_warning}" size="10"/>
															</td></tr>
															<tr><td><label for='fluxgate_min_critical'>Fluxgate Temperatures Critical Min</label>
															</td><td><g:textField  name="fluxgate_min_critical" value="${fluxgate_min_critical}" size="10"/>
															</td></tr>

															<tr><td><label for='battery_critical'>Battery Voltage Critical Threshold</label>
															</td><td><g:textField  name="battery_critical" value="${battery_critical}" size="10"/>
															</td></tr>
															<tr><td><label for='battery_warning'>Battery Voltage Warning Threshold</label>
                                </td><td><g:textField  name="battery_warning" value="${battery_warning}" size="10"/>  
                                </td></tr>
                                </table>
                          </td></tr>        
                          </table>
											<br>
											Note: Leaving an input field empty above will instruct the Absolutes Status data analyser to <i>not</i> run a check against that condition.
                    </div>
                    <div id="tab-5">
                            <table style="border: 1px;">
                                <tr><td><h3>Main Graph Defaults</h3><br/></td><td><h3>Program Defaults</h3><br/></td></tr>
                                <tr><td>
                                    <table style="border: 1px;">
                                        <tr><td width="30%">
                                            <label for='vertical_scale'>Vertical Scale</label>
                                        </td><td>
                                            <g:select id="type" 
                                                      name='vertical_scale' value="${v_value}"  
                                                      from='${v_list}'
                                                      optionKey="key" optionValue="value"  ></g:select>
                                        </td></tr>
                                        <tr><td>
                                             <label for='horizontal_scale'>Horizontal Scale</label>
                                        </td><td>
                                             <g:select id="type" 
                                                      name='horizontal_scale' value="${h_value}"  
                                                      from='${h_list}'
                                                      optionKey="key" optionValue="value"  ></g:select>
                                        </td></tr>
																			<tr><td>
																				<label for='horizontal_scale'>Refresh Rate</label>
																			</td><td>
																				<g:select id="type" name='refresh_rate'
																					value="${rr_value}" from='${rr_list}' optionKey="key" optionValue="value"></g:select>
																			</td></tr>
                                    </table>
                                    <br/>
                               </td><td>
                                    <table style="border: 1px;">
                                         <tr><td width="35%">
                                            <label for='login_timeout'>Default Login Timeout</label>
                                          </td><td>
                                           
                                            <g:select id="type" 
                                                      name='login_timeout' value="${lt_value}"  
                                                      from='${lt_list}'
                                                      optionKey="key" optionValue="value"  ></g:select>                                            
                                        </td></tr>
                                        <tr><td>
                                            <label for='reset_timeout'>Default Reset Timeout</label>
                                          </td><td>
                                              <g:select id="type" 
                                                      name='reset_timeout' value="${rt_value}"  
                                                      from='${rt_list}'
                                                      optionKey="key" optionValue="value"  ></g:select>
                                        </td></tr>
                                        <tr><td>
                                            <label for='data_retention_policy'>Data Retention Policy</label>
                                          </td><td>
                                              <g:select id="type" 
                                                      name='data_retention_policy' value="${drp_value}"  
                                                      from='${drp_list}'
                                                      optionKey="key" optionValue="value"  ></g:select>
                                        </td></tr>
                                        <tr><td>
                                            <label for='default_records_per_page'>Default Records Per Page</label>
                                          </td><td>
                                              <g:select id="type" 
                                                      name='default_records_per_page' value="${rpp_value}"  
                                                      from='${rpp_list}'
                                                      optionKey="key" optionValue="value"  ></g:select>
                                        </td></tr>

                                    </table>
                                </td></tr>
                             </td></tr>
                            </table>
                    </div>

<sec:ifLoggedIn>
			<div id="tab-6">
				<p style="margin: 0 0 10px 0;">Change Password for user <i><sec:username></sec:username></i>:</p>
					<table style='border: 0px none;'>
						<tr>
							<td width='15%'><label for='oldpassword'>Current Password</label></td>
							<td><input id='oldpassword' type='password' name='oldpassword'></td>
						</tr>
						<tr>
							<td width='15%'><label for='newpassword'>New Password</label></td>
							<td><input id="newpassword" type='password' name='newpassword'></td>
						</tr>
						<tr>
							<td width='15%'><label for='confirmpassword'>Confirm Password</label></td>
							<td><input id="confirmpassword" type='password' name='confirmpassword'></td>
						</tr>
						%{--<tr>--}%
							%{--<td width='15%'><input type='submit' name='change' value='Change' /></td>--}%
						%{--</tr>--}%
					</table>
			</div>
</sec:ifLoggedIn>
										<sec:ifLoggedIn>
                    	<p><center><g:submitButton name="save" value=" Save " /></center></p>
                    </sec:ifLoggedIn> 
                    </g:form>
                </div>
                <sec:ifNotLoggedIn><p><b>Note: </b>To change settings please <g:link controller="login" action="index">login</g:link>.</p></sec:ifNotLoggedIn>
                <sec:ifNotLoggedIn>
                	<script>
                		$(function(){
                			$("input").attr("disabled","disabled");
                			$("select").attr("disabled","disabled");
                		});
                	</script>
                </sec:ifNotLoggedIn>    
            </div>
         
    </body>
</html>


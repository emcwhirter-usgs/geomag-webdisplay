<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 	<head>
        <title>MagWorm WebDisplay Administration</title>
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
							
							$("#tabs li a").click(function(){
								$("div.message").remove();
							});
							
						});
			</script>
			<br/>
			<g:if test='${flash.message}'><br/><div class='message'>${flash.message}</div></g:if>
			
			<div class="demodds">
				<h1 class="firstHeader">Administration</h1>
				<br class="clear-both"/>
				<div id="tabs">
					<ul>
						<li><a href="#tab-1">Edit User &quot;${user.username}&quot;</a></li>
					</ul>
					<div id="tab-1">
						 <table style="border: 1px;">
	                            <tr><td width="250">
	                            	<g:form action="edituser">
	                            	<g:hiddenField name="user_id" value="${user.id}"/>
	                            		<table>
	                            		<tr><th colspan="2">Password Management</th></tr>
	                            		<tr><td width="20">
		                            		<label for='newpassword'>New Password</label>
		                            		</td>
		                            		<td>
		                            		<g:passwordField name="newpassword" value="${newpassword}" />
		                            		</td>
		                            	</tr>
		                            	<tr><td width="120">
		                            		<label for='confirmpassword'>Confirm Password</label>
		                            		</td>
		                            		<td>
		                            		<g:passwordField name="confirmpassword" value="${confirmpassword}" />
		                            		</td>
		                            	</tr>
		                            	<tr colspan="2">
		                            		<td>
		                         				<g:submitButton name="changepassword" value=" Change Password " />
		                		   			</td>
		                		 		</tr>  			
		                            	</table>
									</g:form>
								</td>
	                            <td>
	                            	<g:form action="edituser">
									<g:hiddenField name="user_id" value="${user.id}"/>
										<table>
											<tr><th>Role Management</th></tr>
											<tr>
											<td>
												<table>									
							                        	<g:each in="${roles}" var="role">
							                        		<tr><td>
							                        		<g:if test="${usersrolesmap.containsKey(role.id)}">
							                        				<g:if test="${role.authority == 'ROLE_USER'}">		
							                        					<input type="checkbox" name="${role.authority}" checked="checked" disabled="disabled" />
							                        				</g:if>
							                        				<g:elseif test="${role.authority == 'ROLE_ADMIN' && user.username == 'System' }">
							                        					<input type="checkbox" name="${role.authority}" checked="checked" disabled="disabled" />
							                        				</g:elseif>
							                        				<g:else>
							                        					<g:checkBox name="${role.authority}" value="${true}" />
							                        				</g:else>
									                            	<label for='${role.authority}'>${role.authority}</label>
									                        </g:if>
							                        		<g:else>
							                        			<g:checkBox name="${role.authority}" value="${false}" />
							                        		    <label for='${role.authority}'>${role.authority}</label>
									                        </g:else>
							                        		</td></tr>
							                            </g:each>
					                            	</table>
					                        </td>
					                        </tr>
		                            		<tr>
			                            		<td>
			                        	    		<g:submitButton name="changeroles" value=" Change Roles " />
			                		   			</td>
			                		 		</tr>
			                		 		</table>
		              				</g:form>
						        	</td>
	                            </tr>
	                     </table>
	                     <br/>
	                     <g:link controller="main" action="admin">&lt; back</g:link>      	
	               	</div>	        
	          </div>
		</div>
	</body>
</html>


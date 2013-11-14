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
						<li><a href="#tab-1">Users</a></li>
						<li><a href="#tab-2">New User</a></li>
						%{--<li><a href="#tab-3">Roles</a></li>--}%
						%{--<li><a href="#tab-4">Create Role</a></li>--}%
					</ul>
					
					<div id="tab-1">
							<table><tr><th>User</th><th>Actions</th></tr>
							<g:each in="${users}" var="user">
     							
     							<g:if test="${ user.username == 'System'}">
     								<tr>
     									<td>${user.username}</td>
     									<td>
     										<g:form action="edituser">
     											<g:hiddenField name="user_id" value="${user.id}" />
     											<g:submitButton name="edit" value="Edit" />
     										</g:form>
     									</td>
     								</tr>
     							</g:if>
     							<g:else>
     								<tr>
     									<td>${user.username} </td>
     									<td>
     									
     										<g:form action="edituser">
     											<g:hiddenField name="user_id" value="${user.id}" />
     											<g:submitButton name="edit" value="Edit" />
     										</g:form>
     										<g:form action="deleteuser">
     											<g:hiddenField name="user_id" value="${user.id}" />
     											<g:submitButton name="delete" value="Delete User" />
     										</g:form>
     									
     								   	</td>
     								</tr>
     							</g:else>
     						</g:each>
     						</table>
	               	</div>	        
	              
                   	<g:form action="createuser">
	                	<div id="tab-2">
	                            <table style="border: 1px;">
	                            <tr>
	                            	<td width="15%"><label for='username'>Username</label></td>
	                            	<td><g:textField  name="username" value="${username}"/></td>
	                            </tr>
	                            <tr>
	                            	<td><label for='newpassword'>New Password</label></td>
	                            	<td><g:passwordField name="newpassword" value="${newpassword}" /></td>
	                            </tr>
	                            <tr>
	                            	<td><label for='confirmpassword'>Confirm Password</label></td>
	                            	<td><g:passwordField name="confirmpassword" value="${confirmpassword}" /></td>
	                            </tr>
	                            <tr>
	                            	<td>&nbsp;</td>
	                            	<td>
	                            		<g:each in="${roles}">
	                            			<g:if test="${it.authority != 'ROLE_USER'}">
	                            				<g:checkBox name="${it.authority}" value="${false}" />	
	                            				<label for='admin'>${it.authority == 'ROLE_ADMIN' ? "Administrator" : it.authority}</label>
	                            			</g:if>
	                            			 
	                            			<!-- g:elseif test="${it.authority == 'ROLE_USER'}" -->
	                            				<!-- input type="checkbox" name="${it.authority}" checked="checked" disabled="disabled" / -->
	                            				<!-- label for='admin'>${it.authority}</label -->
	                            			<!--  /g:elseif -->
	                            			
	                            			 
	                            		</g:each>
	                            	</td>
	                            <tr>
	                            	<td colspan="2"><g:submitButton name="submit" value=" Submit " /></td>
	                            </tr>      
	                            </table>
	                    </div>
	                </g:form>
	                
%{--
	               <div id="tab-3">
	               		<table><tr><th>Role</th><th>Actions</th></tr>
	               		<g:each in="${roles}" var="role">
	               			<g:if test="${role.authority == 'ROLE_USER' || role.authority == 'ROLE_ADMIN'}">
	               				<tr><td>${role.authority}</td></tr>
	               			</g:if>
	               			<g:else>
	               				<tr><td>${role.authority}</td>
	               					<td>
	               						<g:form action="deleterole">
	               							<g:hiddenField name="role_id" value="${role.id}" />
	               							<g:submitButton name="delete" value="Delete" />
	               						</g:form>
	               					</td>
	               				</tr>
	               			</g:else>
	               		</g:each>
	               		</table>
	               </div>	        
--}%

%{--
                   	<g:form action="createrole">
                    	<div id="tab-4">
								<table style="border: 1px;">
	                            <tr>
	                            	<td width="5%"><label for='rolename'>Role</label></td>
	                            	<td>
	                            		<g:textField  name="rolename" value="${rolename}"/>
	                            		<g:submitButton name="submit" value=" Submit " />
	                            	</td>
	                            </tr>      
	                            </table>
	                    </div>	                     
	                </g:form>
--}%
				</div>
			</div>
		</div>
	</body>
</html>


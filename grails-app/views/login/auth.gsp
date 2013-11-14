<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 	<head>
        <title>MagWorm &raquo; WebDisplay &raquo; Login</title>
    	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta name="layout" content="mainapp" />
	</head>
	<body>
		<div id="maincontent">
			<div id="login">
	        	<div class="homePagePanel">
	                <div class="panelTop"></div>
	                <div class="panelBody">
	                    <h1 class="firstHeader">Login</h1>
                            <br class="clear-both"/>
	                	<div class='info'>
	                    	<g:if test='${flash.message}'>
								<div class='login_message'>${flash.message}</div>
								<br/>
	                       	</g:if>
	
	                    	<form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
								<table border="0" style="border: none;">
								<tr>
									<td><label for='username'>User ID</label></td>
									<td><input type='text' class='text_' name='j_username' id='username' /></td>
								</tr>
								<tr>
									<td><label for='password'>Password</label></td>
									<td><input type='password' class='text_' name='j_password' id='password' /></td>
								</tr>
								</table>
													<div style="padding:10px 0 14px 8px;text-align:center"><input type='submit' value=' Login ' /></div>
		                    </form>
		                	</div>
		                </div>
	                <div class="panelBtm"></div>
	            </div>
		</div>

		<script type='text/javascript'>
		<!--
		(function(){
        		document.forms['loginForm'].elements['j_username'].focus();
		})();
		// -->
		</script>

	</body>
</html>


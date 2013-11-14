<!DOCTYPE html>
<html>
<head>
<title><g:layoutTitle default="Geomag Webdisplay" /></title>
<link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
<link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
<g:layoutHead />
<g:javascript library="application" />
<script type="text/javascript" src="${resource(dir:'js',file:'jquery.js')}"></script>
<script type="text/javascript" src="${resource(dir:'js',file:'jquery.flot.js')}"></script>
<script type="text/javascript" src="${resource(dir:'js',file:'jquery.flot.selection.js')}"></script>
<script type="text/javascript" src="${resource(dir:'js',file:'jquery.flot.crosshair.js')}"></script>
<script type="text/javascript" src="${resource(dir:'js',file:'jquery.flot.resize.js')}"></script>
<link type="text/css" href="${resource(dir:'css/custom-theme',file:'jquery-ui-1.8.16.custom.css')}" rel="Stylesheet" />
<script type="text/javascript" src="${resource(dir:'js',file:'jquery-ui-1.8.16.custom.min.js')}"></script>
<!--[if lte IE 8]><script language="javascript" type="text/javascript" src="${resource(dir:'js',file:'excanvas.min.js')}"></script><![endif]-->
<script type="text/javascript" src="${resource(dir:'js',file:'webdisplay-common.js')}"></script>

<script type="text/javascript" src="${resource(dir:'js',file:'knockout-2.1.0.js')}"></script>

<link rel="stylesheet" href="${resource(dir:'css',file:'geomag.css')}"/>
<link rel="stylesheet" href="${resource(dir:'css',file:'theme-white.css')}" title="white (default)" />
<link rel="stylesheet" href="${resource(dir:'css',file:'theme-grey.css')}" title="grey" />
</head>

<body class="soria" style="margin: auto;">
<div id="layout1" style="margin: auto;">
<div id="spinner" class="spinner" style="z-index:999;display:none;">
<img src="${resource(dir:'images',file:'spinner.gif')}" alt="Loading..." />
</div>
<div id="appHeader">
<div id="appLogo"><a href="http://geomag.usgs.gov" target="_blank"><img src="${resource(dir:'images',file:'app_logo.png')}" alt="USGS" border="0" /></a></div>
</div>
<div class="mainMenuBarWrapper">
<ul id="mainMenuBar">
<li><g:link controller="main" action="magnetometers">Magnetometers</g:link></li>
<li><g:link controller="main" action="temperatures">Temperatures</g:link></li>
<li><span class="menuitem">Logs</span>
<ul>
<li><g:link controller="main" action="binlog">Bin Log</g:link></li>
<sec:ifAllGranted roles="ROLE_ADMIN">
	<li><g:link controller="main" action="userlog">User Log</g:link></li>
</sec:ifAllGranted>
</ul>
</li>
<li><g:link controller="main" action="settings">Settings</g:link></li>

<sec:ifAllGranted roles="ROLE_ADMIN">
<li><g:link controller="main" action="admin">Admin</g:link></li>
</sec:ifAllGranted>

<sec:ifNotLoggedIn>
<li style="float:right;"><g:link controller="login" action="index">Login</g:link></li>
</sec:ifNotLoggedIn>
<sec:ifLoggedIn>
<li style="float:right;"><g:link controller="logout" action="index">Logout</g:link></li>
<li style="float:right;"><span class="menuitem" style="font-weight:normal;"><small>logged in as:</small> <sec:username/></span></li>
</sec:ifLoggedIn>

%{--<li><a href="http://geomag.usgs.gov/webdisplay/help/" target="_blank">Help</a></li>--}%
</ul>
</div>
<g:layoutBody />
</div>

<script type="text/javascript">
(function($){
	$.fn.extend({
//plugin name - animatemenu
		webtoolkitMenu: function(options) {
			return this.each(function() {
				var obj = $(this);
				$("li ul", obj).each(function(i) {
					$(this).css('top', $(this).parent().outerHeight());
				})
				$("li", obj).hover(
					function () { $(this).addClass('over'); },
					function () { $(this).removeClass('over'); }
				);
			});
		}
	});
})(jQuery);

$(function () {
	$('#mainMenuBar').webtoolkitMenu();
});
</script>

</body>
</html>

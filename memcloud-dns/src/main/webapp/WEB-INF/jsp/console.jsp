<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
		
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<title>缓存云平台</title>
		<!--                       CSS                       -->
		<!-- Reset Stylesheet -->
		<link rel="stylesheet" href="/resources/css/reset.css" type="text/css" media="screen" />
		<!-- Main Stylesheet -->
		<link rel="stylesheet" href="/resources/css/style.css" type="text/css" media="screen" />
		<!-- Invalid Stylesheet. This makes stuff look pretty. Remove it if you want the CSS completely valid -->
		<link rel="stylesheet" href="/resources/css/invalid.css" type="text/css" media="screen" />	
		
		<!-- Internet Explorer Fixes Stylesheet -->
		<!--[if lte IE 7]>
			<link rel="stylesheet" href="/resources/css/ie.css" type="text/css" media="screen" />
		<![endif]-->
		<!--                       Javascripts                       -->
		<!-- jQuery -->
		<script type="text/javascript" src="/resources/scripts/jquery-1.3.2.min.js"></script>
		<!-- jQuery Configuration -->
		<script type="text/javascript" src="/resources/scripts/simpla.jquery.configuration.js"></script>
		<!-- Facebox jQuery Plugin -->
		<script type="text/javascript" src="/resources/scripts/facebox.js"></script>
		<!-- jQuery WYSIWYG Plugin -->
		<script type="text/javascript" src="/resources/scripts/jquery.wysiwyg.js"></script>
		<!-- jQuery Datepicker Plugin -->
		<!--[if IE]><script type="text/javascript" src="/resources/scripts/jquery.bgiframe.js"></script><![endif]-->
		<!-- Internet Explorer .png-fix -->
		<!--[if IE 6]>
			<script type="text/javascript" src="/resources/scripts/DD_belatedPNG_0.0.7a.js"></script>
			<script type="text/javascript">
				DD_belatedPNG.fix('.png_bg, img, li');
			</script>
		<![endif]-->
<style type="text/css">
<!--
.STYLE1 {font-size: x-large}
.STYLE2 {color: #FF0000}
-->
</style>
</head>
  
<body>
	<div id="body-wrapper">
		<div id="sidebar">
			<iframe id="navigation" name="navigation" src="/resources/static/navigation.html" scrolling="no" frameborder="0" width="100%" height="100%">
			</iframe>
		</div>
	  	<div id="main-content">
	  		<iframe id="workspace" name="workspace" src="/resources/static/workspace.html" scrolling="no" frameborder="0" width="100%" onload="this.height=this.contentWindow.document.body.scrollHeight">
	  		<!--
	  		<iframe id="workspace" src="/static/welcome.html" scrolling="no" frameborder="0" width="100%">
	  		<iframe id="workspace" src="/static/welcome.html" scrolling="no" frameborder="0" width="100%" onload="this.height=this.contentWindow.document.body.scrollHeight">
	  		 -->
	  		</iframe>
	  	</div>
	</div>
</body>
</html>

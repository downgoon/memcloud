<%@ page contentType="text/html;charset=UTF-8" %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN""http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>缓存云平台登录页面</title>
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
		<!-- Internet Explorer .png-fix -->
		<!--[if IE 6]>
			<script type="text/javascript" src="/resources/scripts/DD_belatedPNG_0.0.7a.js"></script>
			<script type="text/javascript">
				DD_belatedPNG.fix('.png_bg, img, li');
			</script>
		<![endif]-->
		
		<script type="text/javascript" src="/resources/scripts/session-create.js"></script>
	</head>
  
	<body id="login">
		
		<div id="login-wrapper" class="png_bg">
			<div id="login-top">
				<h1>欢迎使用缓存云平台</h1>
				<!-- Logo (221px width) -->
				<img id="logo" src="/resources/images/mem_logo.png" alt="Simpla Admin logo" />
				<!-- 
				logo尚未专门设计 
				 -->
			</div> <!-- End #logn-top -->
			
			<div id="login-content">
				
				<form>
					<!-- 信息提示区：例如密码认证失败时 -->
					<div class="notification information png_bg" >
						<div id="devNotify">您尚未登录，请输入用户名和密码</div>
					</div>
					<p>
						<label>用户名：</label>
						<input class="text-input" type="text" id="devUser"/><!-- style="display: none" -->
					</p>
					<div class="clear"></div>
					<p>
						<label>密码：</label>
						<input class="text-input" type="password" id="devPwd"/>
					</p>
					<div class="clear"></div>
					<div class="clear"><label><a href="/admin.html">管理员入口</a></label></div>
					<p>
						<input class="button" type="submit" value="登录" id="btnLogin" onclick="doLoginAuth(); return false;"/>
					</p>
				</form>
				
			</div> <!-- End #login-content -->
		</div> <!-- End #login-wrapper -->
  </body>
  </html>

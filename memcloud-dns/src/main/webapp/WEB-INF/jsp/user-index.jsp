<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.github.downgoon.jresty.rest.model.UnifiedResponse,com.github.downgoon.jresty.rest.model.UnifiedResponseCode" %>
<%@page import="java.util.*,io.memcloud.memdns.dao.entry.AppDesc,io.memcloud.memdns.dao.entry.User" %>
<%@ include file="../conf.jsp"%>
<%
	UnifiedResponse  up = (UnifiedResponse)request.getAttribute("model");
	Map<String,Object> attachment = (Map<String,Object>)up.getAttachment();
	Long uid = (Long)attachment.get("uid");
	String name = (String)attachment.get("name");
	String mobile = (String)attachment.get("mobile");
	String email = (String)attachment.get("email");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户信息</title>
<link rel="stylesheet" href="/resources/css/reset.css" type="text/css" media="screen" />
<link rel="stylesheet" href="/resources/css/style.css" type="text/css" media="screen" />
<link rel="stylesheet" href="/resources/css/invalid.css" type="text/css" media="screen" />	
<script type="text/javascript" src="/resources/scripts/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="/resources/scripts/simpla.jquery.configuration.js"></script>
<script type="text/javascript" src="/resources/scripts/facebox.js"></script>
<script type="text/javascript" src="/resources/scripts/jquery.wysiwyg.js"></script>
<style type="text/css">
<!--
.STYLE1 {font-size: x-large}
body{ background-color:#f0f0f0; background-image:none;}
-->
</style>
</head>
<body>
<div id="main-content"  style="margin-left:20px; padding-top:10px;"> <!-- Main Content Section with everything -->
	<noscript> <!-- Show a notification if the user has disabled javascript --></noscript>
	<h3>我的资料</h3>
	<!-- 
	<div class="notification error png_bg"> <a href="#" class="close"><img src="resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
        <div>错误信息提示</div>
  	</div>
	<div class="notification success png_bg"> <a href="#" class="close"><img src="resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
        <div>成功信息提示</div>
  	</div>
  	 -->
	<div class="content-box"><!-- Start Content Box -->
		<div class="content-box-header">
			<h3>用户资料</h3>
			<ul class="content-box-tabs">
				<li></li> <!-- href must be unique and match the id of target div -->
			</ul>
			<div class="clear"></div>
		</div>
		
		<div class="box-content">
			<div class="default-tab" id="tab1"> <!-- This is the target div. id must match the href of this div's tab -->
		    </div> 
			<div class="tab-content" id="tab2">
				<form action="#" method="post">
					<fieldset><!-- Set class to "column-left" or "column-right" on fieldsets to divide the form into columns -->
					<p>
						<label>用户：</label>
						<input name="small-input" type="text" class="text-input small-input" id="small-input" value="<%=name %>" />
					</p>
					<!-- 
					<p>
						<label>密码：</label>
						<input class="text-input small-input" type="text" id="small-input2" name="small-input2" />
						<span class="input-notification error png_bg">Error message</span>								
					</p>
					<p>
						 <label>重复输入密码：</label>
                          <input class="text-input small-input" type="text" id="small-input22" name="small-input22" />
                          <span class="input-notification error png_bg">Error message</span>
                    </p>
                     -->
					<p>
					  	<label>手机号码：</label>
                       	<input class="text-input small-input" type="text" id="small-input23" name="small-input23" value="<%=(mobile==null?"":mobile) %>" />
                        <!-- <span class="input-notification error png_bg">Error message</span> -->
                     </p>
					<p>
					  <label>邮箱：</label>
                      <input name="small-input24" type="text" class="text-input small-input" id="small-input24" value="<%=(email==null?"":email) %>" />
					</p>
					<!-- 
					<p>
						<input class="button" type="submit" value="确定保存" />
					</p>
					 -->
								
					</fieldset>
							<div class="clear"></div><!-- End .clear -->
						</form>
					</div>        
				</div>
			</div>
	<div class="clear"></div>
	<%@include file="/WEB-INF/include/foot.jsp" %>
	</div>
</body>
</html>

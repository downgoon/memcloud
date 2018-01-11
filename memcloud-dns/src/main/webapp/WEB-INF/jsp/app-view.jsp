<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.github.downgoon.jresty.rest.model.UnifiedResponse,com.github.downgoon.jresty.rest.model.UnifiedResponseCode" %>
<%@page import="java.util.*,io.memcloud.memdns.dao.entry.AppDesc,com.github.downgoon.jresty.commons.utils.*,com.github.downgoon.jresty.commons.security.*" %>
<%@ include file="../conf.jsp"%>
<%
	UnifiedResponse  up = (UnifiedResponse)request.getAttribute("model");
	AppDesc appDesc = (AppDesc)up.getAttachment();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>应用信息</title>
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
	<h3>我的应用</h3>
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
			<h3><%=appDesc.getName() %></h3>
			<ul class="content-box-tabs">
				<li></li>
			</ul>
			<div class="clear"></div>
		</div>
		
		<div class="box-content">
			<div class="default-tab" id="tab1"> <!-- This is the target div. id must match the href of this div's tab -->
		    </div> 
			<div class="tab-content" id="tab2">
				<form action="#" method="post">
					<fieldset>
					<p>
						<label>应用ID：</label>
						<input name="appID" type="text" class="text-input small-input" id="appID" value="<%=appDesc.getId() %>" readonly="readonly"/>
					</p>
					<p>
						<label>应用名称：</label>
						<input name="appName" type="text" class="text-input small-input" id="appName" value="<%=URLEncodec.encodeUTF8(appDesc.getName()) %>" readonly="readonly" />
					</p>
					<p>
						<label>应用状态：</label>
						<input name="appStatus" type="text" class="text-input small-input" id="appStatus" value="<%=appDesc.getStatus() %>" readonly="readonly" />
					</p>
					<% if (appDesc.getCreateTime() != null) { %>
					<p>
						<label>申请时间：</label>
						<input name="creatTime" type="text" class="text-input small-input" id="creatTime" value="<%=DateUtil.format(appDesc.getCreateTime()) %>" readonly="readonly" />
					</p>
					<% } %>
					
					<% if (appDesc.getCreateTime()!=null && appDesc.getStatus()!=null && appDesc.getStatus()==1) {%>
						<p>
							<label>通过时间：</label>
							<input name="passedTime" type="text" class="text-input small-input" id="passedTime" value="<%=DateUtil.format(appDesc.getCreateTime()) %>" readonly="readonly" />
						</p>
					<% } %>
					
					<p>
					    <label>告警手机序列：</label>
                       	<input class="text-input small-input" type="text" id="notifyMobiles" name="notifyMobiles" value="<%=(appDesc.getNotifyMobiles()==null?"":appDesc.getNotifyMobiles()) %>" readonly="readonly" />
                     </p>
					<p>
					  <label>告警邮箱序列：</label>
                      <input name="notifyEmails" type="text" class="text-input small-input" id="notifyEmails" value="<%=(appDesc.getNotifyEmails()==null?"":appDesc.getNotifyEmails()) %>" readonly="readonly" />
					</p>
					
					</fieldset>
					<div class="clear"></div>
				</form>
			</div>        
		</div>
	</div>
	<div class="clear"></div>
	<%@include file="/WEB-INF/include/foot.jsp" %>
	</div>
</body>
</html>

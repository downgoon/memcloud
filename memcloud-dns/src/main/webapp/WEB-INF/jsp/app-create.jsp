<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.github.downgoon.jresty.rest.model.UnifiedResponse,com.github.downgoon.jresty.rest.model.UnifiedResponseCode" %>
<%@page import="java.util.*,io.memcloud.memdns.dao.entry.AppDesc,com.github.downgoon.jresty.commons.utils.*" %>
<%@ include file="../conf.jsp"%>
<%
	boolean isRequestPage = false;//表示本次页面是作为请求，还是响应
	String name = request.getParameter("name");
	String descr = request.getParameter("descr");
	String emails = request.getParameter("emails");
	String mobiles = request.getParameter("mobiles");
	if (name == null && descr == null && emails == null && mobiles == null) {
		isRequestPage = true;
	}

	UnifiedResponse  up = (UnifiedResponse)request.getAttribute("model");
	boolean hasErrorInfo = false;//表示本次为响应页面，并且输入参数有错误，需要提示错误信息
	if (!isRequestPage && up != null && up.getStatus() >= 400 && up.getStatus() < 500) {
		hasErrorInfo = true;
	}
	AppDesc appDesc = null;
	if (up != null) {
		appDesc = (AppDesc)up.getAttachment();	
	}
	
	String flagReadonly = ( (!isRequestPage && !hasErrorInfo) ? "readonly=\"readonly\"" : "");
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
	<h3>申请应用</h3>
	
	<% if (!isRequestPage && hasErrorInfo) { %>
	<div class="notification error png_bg"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
        <div>错误信息提示：<%=up.getDebug() %></div>
  	</div>
  	<% } %>
  	
  	<% if (!isRequestPage && !hasErrorInfo && appDesc!=null ) { %>
	<div class="notification success png_bg"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
        <div>成功信息提示：保存成功，应用ID为<%=appDesc.getId() %></div>
  	</div>
  	<% } %>
  	
	<div class="content-box"><!-- Start Content Box -->
		<div class="content-box-header">
			<h3><%= (isRequestPage ? "填写" : "反馈") %>应用信息</h3>
			<ul class="content-box-tabs">
				<li></li>
			</ul>
			<div class="clear"></div>
		</div>
		
		<div class="box-content">
			<div class="default-tab" id="tab1">
			</div> 
			<div class="tab-content" id="tab2">
				<form action="<%=(isRequestPage || hasErrorInfo) ? "/memcloud/app-create.html" : "#" %>" method="post">
					<fieldset>
					<% if ( !isRequestPage && !hasErrorInfo && appDesc!=null ) { %>
					<p><label>应用ID：</label>
					<input type="text" class="text-input small-input" id="appId" name="appId" value="<%=appDesc.getId()%>" <%=flagReadonly %>/></p>
					<% } %>
					
					<p><label>应用名称（必填，全局唯一，以字母开头的4~16个字符序列，不能含中文，可以是大小写字符，数字，下划线，连接线，点）：</label>
						<input type="text" class="text-input small-input" id="name" name="name" value="<%=(name!=null ? name : "")%>" <%=flagReadonly %> /></p>
						
					<p><label>应用描述（必填，不超过128个字符，可以包含中文）：</label>
						<textarea class="text-input" cols="300" rows="4" id="descr" name="descr" <%=flagReadonly %>><%=(descr!=null ? descr : "")%></textarea>
					</p>
					
					<p><label>告警手机序列（选填，多个手机号以英文逗号分割）：</label>
                       	<input type="text" class="text-input small-input" id="mobiles" name="mobiles" value="<%=(mobiles!=null ? mobiles : "")%>" <%=flagReadonly %>/></p>
                       	
					<p><label>告警邮箱序列（选填，多个邮箱以英文逗号分割）：</label>
                      <input type="text" class="text-input small-input" id="emails" name="emails" value="<%=(emails!=null ? emails : "")%>" <%=flagReadonly %>/></p>
					
					<% if ( !isRequestPage && !hasErrorInfo && appDesc!=null ) { %>
					<p><label>创建时间：</label>
					<input type="text" class="text-input small-input" id="createTime" name="createTime" value="<%=DateUtil.format(appDesc.getCreateTime())%>" <%=flagReadonly %>/></p>	
					<p><label>应用状态：<a href="/memcloud/scale-create/<%= appDesc.getId() %>.html">申请云资源</a></label>
					<input type="text" class="text-input small-input" id="status" name="status" value="<%=appDesc.getStatus() %>" <%=flagReadonly %>/></p>
					<% } %>

					<% if ( isRequestPage || hasErrorInfo ) { %>
					<p><input class="button" type="submit" value="确定保存" /></p>
					<% } %>

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

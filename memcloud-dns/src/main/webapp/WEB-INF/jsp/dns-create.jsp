<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.github.downgoon.jresty.rest.model.UnifiedResponse,com.github.downgoon.jresty.rest.model.UnifiedResponseCode" %>
<%@page import="java.util.*,com.github.downgoon.jresty.commons.utils.*,io.memcloud.memdns.dao.entry.*" %>
<%@ include file="../conf.jsp"%>
<%
	
	UnifiedResponse  up = (UnifiedResponse)request.getAttribute("model");
	AppConfAudit appConfAudit = null;
	boolean isArgErr = false;//输入参数错误
	if (up.getStatus() >= 400 && up.getStatus()<500) {
		isArgErr = true;
	}
	if ( !isArgErr) {
		appConfAudit = (AppConfAudit)up.getAttachment();
	}
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

<script type="text/javascript" src="/resources/scripts/scale-adminedit.js"></script>
<style type="text/css">
<!--
.STYLE1 {font-size: x-large}
body{ background-color:#f0f0f0; background-image:none;}
-->
</style>
</head>
<body>
	<div id="main-content"  style="margin-left:20px; padding-top:10px;">
	<noscript></noscript>
	<h3>DNS发布结果</h3>
	
	<% if (isArgErr) { %>
	<div class="notification error png_bg"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
        <div>错误代码：<%=up.getStatus() %>；错误信息：<%=up.getMessage() %>。</div>
        <div><%=(up.getDebug()==null ? "" : up.getDebug()) %></div>
  	</div>
  	<% } else {%>
  	<div class="notification success png_bg"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
    <div>发布成功，<a href="/memcloud/dns-help/<%=appConfAudit.getAppId() %>.html">查看DNS与Group对比情况</a></div>
    </div>
  	<% } %>
	
	<div class="clear"></div>
	<%@include file="/WEB-INF/include/foot.jsp" %>
	</div>
</body>
</html>

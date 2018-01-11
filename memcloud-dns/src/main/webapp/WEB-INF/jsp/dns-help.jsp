<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.github.downgoon.jresty.rest.model.UnifiedResponse,com.github.downgoon.jresty.rest.model.UnifiedResponseCode" %>
<%@page import="java.util.*,com.github.downgoon.jresty.commons.utils.*,io.memcloud.memdns.dao.entry.*" %>
<%@ include file="../conf.jsp"%>
<%
	
	UnifiedResponse  up = (UnifiedResponse)request.getAttribute("model");
	boolean isArgErr = false;//输入参数错误
	if (up.getStatus() >= 400 && up.getStatus()<500) {
		isArgErr = true;
	}
	Map<String, Object> attachment = null;
	boolean sync = false;
	int memGroupSize = 0;
	int appConfSize = 0;
	AppDesc appDesc = null;
	AppConf appConf = null;
	List<AppMemGroup> memGroup = null;
	
	if ( !isArgErr) {
		attachment = (Map<String, Object>)up.getAttachment();
		sync = (Boolean)attachment.get("sync");
		memGroupSize = (Integer)attachment.get("memGroupSize");
		appConfSize = (Integer)attachment.get("appConfSize");
		
		appDesc = (AppDesc)attachment.get("appDesc");//not null
		appConf = (AppConf)attachment.get("appConf");//may be null
		memGroup = (List<AppMemGroup>)attachment.get("memGroup");//may be null
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
	<h3>扩容申请</h3>
	
	<% if (isArgErr) { %>
	<div class="notification error png_bg"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
        <div>错误代码：<%=up.getStatus() %>；错误信息：<%=up.getMessage() %>。</div>
        <div><%=(up.getDebug()==null ? "" : up.getDebug()) %></div>
  	</div>
  	<% } %>
  	
  	<% if ( !isArgErr ) { %>
  		<% if (!sync) {%>
  		<div class="notification error png_bg"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
        <div>发布滞后：MemDNS=<%=appConfSize %>;  MemGroup=<%=memGroupSize %></div></div>
  		<% } else {%>
  		<div class="notification success png_bg"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
        <div>发布一致： MemDNS=<%=appConfSize %>; MemGroup <%=memGroupSize %> </div></div>
  		<% } %>
  	<% } %>
  	
  	<% if ( !isArgErr ) { %>
	<div class="content-box">
		<div class="content-box-header">
			<h3>集群分配分片与DNS发布分片对比情况</h3>
			<ul class="content-box-tabs">
				<li></li>
			</ul>
			<div class="clear"></div>
		</div>
		
		<div class="box-content">
			<div class="default-tab" id="tab1">
			</div> 
			<div class="tab-content" id="tab2">
				<form action="#" method="post">
            		<p><label>应用ID：</label>
            		 	<input type="text" class="text-input small-input" id="appId" name="appId" value="<%=appDesc.getId()%>" disabled="disabled"/></p>
            			
            		<p><label>应用名称：</label>
            		 	<input type="text" class="text-input small-input" id="appName" name="appName" value="<%=appDesc.getName()%>" disabled="disabled" /></p>
            		
            		<p><label>对比结论：
            			<% if (!sync) {%>
            				<a href="/memcloud/dns-create/<%=appDesc.getId() %>.html">执行DNS发布</a>
            			<% } %>
            			</label>
            		 	<input type="text" class="text-input small-input" id="sync" name="sync" value="<%=(sync ? "一致":"滞后")%>" disabled="disabled" /></p>
            		
            		<p><label>DNS发布分片数：</label>
            		 	<input type="text" class="text-input small-input" id="appConfSize" name="appConfSize" value="<%=appConfSize%>" disabled="disabled" /></p>
            		 	
            		 <p><label>集群分配分片数：</label>
            		 	<input type="text" class="text-input small-input" id="memGroupSize" name="memGroupSize" value="<%=memGroupSize%>" disabled="disabled" /></p>
            		
            		<div class="clear"></div>
				</form>
			</div>        
		</div>
	</div>
	<% } %>
	
	<div class="clear"></div>
	<%@include file="/WEB-INF/include/foot.jsp" %>
	</div>
</body>
</html>

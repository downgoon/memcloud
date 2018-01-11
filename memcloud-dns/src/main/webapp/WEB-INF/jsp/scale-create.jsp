<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.github.downgoon.jresty.rest.model.UnifiedResponse,com.github.downgoon.jresty.rest.model.UnifiedResponseCode" %>
<%@page import="java.util.*,io.memcloud.memdns.dao.entry.*,com.github.downgoon.jresty.commons.utils.*" %>
<%@ include file="../conf.jsp"%>
<%
	boolean isRequestPage = false;//表示本次页面是作为请求，还是响应
	Long paramId = (Long)request.getAttribute("paramId");
	String shardArg = request.getParameter("shard");
	String memArg = request.getParameter("mem");
	AppDesc appDesc = (AppDesc)request.getAttribute("appDesc");//可能为空
	
	if (shardArg == null && memArg == null && (paramId==null || appDesc!=null) ) {//paramId为空，则页面提醒填写；非空，则不能填写了。
		isRequestPage = true;
	}
	
	
	UnifiedResponse  up = (UnifiedResponse)request.getAttribute("model");
	
	
	boolean hasErrorInfo = false;//表示本次为响应页面，并且输入参数有错误，需要提示错误信息
	if (!isRequestPage && up != null && up.getStatus() >= 400 && up.getStatus() < 500) {
		hasErrorInfo = true;
	}
	
	ScaleoutAppeal appeal = null;//扩容申请
	if (up != null) {
		appeal = (ScaleoutAppeal)up.getAttachment();	
	}
	
	String flagReadonly = ( (!isRequestPage && !hasErrorInfo) ? "readonly=\"readonly\"" : "");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>扩容申请</title>
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
	<h3>扩容申请</h3>
	
	<% if (!isRequestPage && hasErrorInfo) { %>
	<div class="notification error png_bg"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
        <div>错误代码：<%=up.getStatus() %>；错误信息：<%=up.getMessage() %>。</div>
        <div><%=(up.getDebug()==null ? "" : up.getDebug()) %></div>
  	</div>
  	<% } %>
  	
  	<% if (!isRequestPage && !hasErrorInfo && appDesc!=null ) { %>
	<div class="notification success png_bg"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
        <div>成功信息提示：扩容申请已提交，应用ID为<%=appDesc.getId() %></div>
  	</div>
  	<% } %>
  	
	<div class="content-box"><!-- Start Content Box -->
		<div class="content-box-header">
			<h3><%= (isRequestPage ? "填写" : "反馈") %>扩容申请</h3>
			<ul class="content-box-tabs">
				<li></li>
			</ul>
			<div class="clear"></div>
		</div>
		
		<div class="box-content">
			<div class="default-tab" id="tab1">
			</div> 
			<div class="tab-content" id="tab2">
				<form action="<%=(isRequestPage || hasErrorInfo) ? "/memcloud/scale-create.html" : "#" %>" method="post">
					<fieldset>
					<p><label>应用ID：</label>
					<input type="text" class="text-input small-input" id="appId" name="paramId" value="<%=(appDesc==null? "" : appDesc.getId())%>" <%=(appDesc!=null ? "readonly=\"readonly\"" : "") %>/></p>
					
					<p><label>追加的分片数（[1,20]）：</label>
						<input type="text" class="text-input small-input" id="shard" name="shard" value="<%=(shardArg!=null ? shardArg : "")%>" <%=flagReadonly %> /></p>
					
					<p><label>追加的单位容量（256，512，1024）：</label>
						<input type="text" class="text-input small-input" id="mem" name="mem" value="<%=(memArg!=null ? memArg : "")%>" <%=flagReadonly %> /></p>
					

					<% if ( isRequestPage || hasErrorInfo ) { %>
					<p><input class="button" type="submit" value="确定保存" /></p>
					<% } %>

					</fieldset>
					
					<% if ( !isRequestPage && !hasErrorInfo && appeal!=null ) { %>
					<p><label>申请扩容时间：</label>
					<input type="text" class="text-input small-input" id="createTime" name="createTime" value="<%=DateUtil.format(appeal.getCreateTime())%>" <%=flagReadonly %>/></p>	
					<p><label>申请扩容状态：</label>
					<input type="text" class="text-input small-input" id="status" name="status" value="<%=appeal.getStatus() %>" <%=flagReadonly %>/></p>
					<% } %>
					
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

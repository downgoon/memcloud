<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.github.downgoon.jresty.rest.model.UnifiedResponse,com.github.downgoon.jresty.rest.model.UnifiedResponseCode" %>
<%@page import="java.util.*,com.github.downgoon.jresty.commons.utils.*,io.memcloud.memdns.dao.entry.ScaleoutAppeal" %>
<%@ include file="../conf.jsp"%>
<%
	
	UnifiedResponse  up = (UnifiedResponse)request.getAttribute("model");
	boolean isArgErr = false;//输入参数错误
	if (up.getStatus() >= 400 && up.getStatus()<500) {
		isArgErr = true;
	}
	Map<String, Object> attachment = null;
	Integer actionNum = 0;//0: edit; 1: accept; -1:reject
	ScaleoutAppeal appeal = null;
	String verifyResult = "";
	
	if ( !isArgErr) {
		attachment = (Map<String, Object>)up.getAttachment();
		actionNum = (Integer)attachment.get("actionNum");
		appeal = (ScaleoutAppeal)attachment.get("appeal");
		
		if ( appeal.getStatus()==ScaleoutAppeal.STATUS_PASSED ) {
			verifyResult = "通过";
		} else if ( appeal.getStatus()==ScaleoutAppeal.STATUS_REJECT ) {
			verifyResult = "驳回";
		} else {
			verifyResult = "待审";
		}
	}
	 
	
	String flagReadonly = ( (actionNum!=0) ? "readonly=\"readonly\"" : "");
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
  	
  	<% if ( !isArgErr && appeal.getStatus()!=ScaleoutAppeal.STATUS_WAITING ) { %>
	<div class="notification success png_bg"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
        <div>审批结果：<%=verifyResult %></div>
  	</div>
  	<% } %>
  	
  	<% if ( !isArgErr ) { %>
	<div class="content-box">
		<div class="content-box-header">
			<h3><%=(isArgErr ? "输入参数错误提示" : "扩容申请编号："+appeal.getId()) %></h3>
			<ul class="content-box-tabs">
				<li></li>
			</ul>
			<div class="clear"></div>
		</div>
		
		<div class="box-content">
			<div class="default-tab" id="tab1">
			</div> 
			<div class="tab-content" id="tab2">
				<form action="/memcloud/scale-adminedit/<%=appeal.getId() %>.html" method="post">
					
					<p><label>应用ID：</label>
					 	<input type="text" class="text-input small-input" id="appId" name="appId" value="<%=appeal.getAppId()%>" readonly="readonly" disabled="disabled"/></p>
						
					<p><label>应用名称：</label>
					 	<input type="text" class="text-input small-input" id="appName" name="appName" value="<%=appeal.getAppName()%>" readonly="readonly" disabled="disabled" /></p>
					
					<p><label>申请者：</label>
						 	<input type="text" class="text-input small-input" id="userId" name="userId" value="<%=appeal.getUserId()%>" disabled="disabled" /></p>
												 
            		<p><label>申请时间：</label>
            		 	<input type="text" class="text-input small-input" id="createTime" name="createTime" value="<%=DateUtil.format(appeal.getCreateTime())%>" disabled="disabled" /></p>
            		 
            		<p><label>申请分片数：</label>
            		 	<input type="text" class="text-input small-input" id="applyShard" name="applyShard" value="<%=appeal.getShardNum()%>" disabled="disabled" /></p>
            		 	
            		<p><label>申请单位容量：</label>
				 	 	<input type="text" class="text-input small-input" id="applyMem" name="applyMem" value="<%=appeal.getMemSize()%>" disabled="disabled" /></p>
					
					<% if (appeal.getStatus() == ScaleoutAppeal.STATUS_WAITING) { %>	 	 	
					<fieldset>
						 <p><label>审核意见：&nbsp;&nbsp;&nbsp;&nbsp; <a href="/memcloud/mem.html?mem=<%=appeal.getMemSize() %>" target="_blank">查看资源池空闲资源（单位容量大于等于<%=appeal.getMemSize() %>M）</a> </label>
						 	<input type="radio" name="action" value="reject" onclick="scaleoutOnVerify(this);"/>驳回　
							<input type="radio" name="action" value="accept" onclick="scaleoutOnVerify(this);"/>同意
							<input type="radio" name="action" value="edit" checked="checked" style="display: none"/>
						 </p>
						 
						 <p id="pShard" style="display: none;"><label>批复分片数：</label>
            		 		<input type="text" class="text-input small-input" id="shard" name="shard" value="<%=appeal.getShardNum()%>" />
            		 	 </p>
            		 	
            			<p id="pMem" style="display: none;"><label>批复单位容量：</label>
				 	 		<input type="text" class="text-input small-input" id="mem" name="mem" value="<%=appeal.getMemSize()%>" />
				 	 	</p>
				 	 	
						 <p><input class="button" type="submit" value="确定提交" id="btnSubmit" style="display: none;" /></p> 
					</fieldset>
					
					<% } else { %>
						<p><label>审核结果：</label>
						 	<input type="radio" checked="checked" disabled="disabled" /><%=(appeal.getStatus()==ScaleoutAppeal.STATUS_PASSED ? "同意" : "驳回") %>　
						 </p>
						<p><label>批复时间：</label>
				 	 		<input type="text" class="text-input small-input" id="passedTime" name="passedTime" value="<%=DateUtil.format(appeal.getPassedTime())%>" disabled="disabled" />
				 	 	</p>
				 	 	
						<% if (appeal.getStatus()==ScaleoutAppeal.STATUS_PASSED) { %>
						 <p><label>批复分片数：</label>
            		 		<input type="text" class="text-input small-input" id="shard" name="shard" value="<%=appeal.getPassedShard()%>" disabled="disabled" />
            		 	 </p>
            		 	
            			<p><label>批复单位容量：</label>
				 	 		<input type="text" class="text-input small-input" id="mem" name="mem" value="<%=appeal.getPassedMem()%>" disabled="disabled" />
				 	 	</p>
				 	 	<% } %>
				 	 	
				 	 	
					<% } %>
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

<%@page import="com.github.downgoon.jresty.commons.utils.*"%>
<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="com.github.downgoon.jresty.rest.model.UnifiedResponse,com.github.downgoon.jresty.rest.model.UnifiedResponseCode" %>
<%@page import="java.util.*,io.memcloud.memdns.dao.entry.ScaleoutAppeal" %>
<%@ include file="../conf.jsp"%>
<%
	UnifiedResponse  up = (UnifiedResponse)request.getAttribute("model");
	List<ScaleoutAppeal> saList = (List<ScaleoutAppeal>)up.getAttachment();
	if (saList == null) {
		saList = new ArrayList<ScaleoutAppeal>();
	}
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>我的应用->全部应用</title>
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

<script type="text/javascript" src="/resources/scripts/payment-commons.js"></script>
<script type="text/javascript" src="/resources/scripts/app-index.js"></script>
</head>

<body>
<div id="main-content" style="margin-left:20px; padding-top:10px;"> <!-- Main Content Section with everything -->
			<noscript> <!-- Show a notification if the user has disabled javascript -->
			</noscript>
			<h3>我的扩容</h3>
			<!-- 
			<div class="notification error png_bg" id="devErrorIcon" style="display: none"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
                <div id="devErrorInfo"></div>
		  	</div>
			<div class="notification success png_bg" id="devSuccIcon" style="display: none"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
                <div id="devSuccInfo"></div>
		  	</div>
			<div class="notification success png_bg" id="devWaitIcon" style="display: none"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
                <div id="devWaitInfo"></div>
		  	</div>
		  	 -->
		    <div class="content-box"><!-- Start Content Box -->
				<div class="content-box-header">
					<h3>全部扩容</h3>
					<ul class="content-box-tabs">
						<li></li>
					</ul>
					<div class="clear"></div>
				</div>
				
				<div class="box-content">
					<div class="default-tab" id="tab1"> <!-- This is the target div. id must match the href of this div's tab -->
					  <table id="devCPTable">
							<thead><tr><!--列表头 -->
								   <th width="6%">&nbsp;</th>
								   <th width="8%">AppID</th>
								   <th width="13%">应用名称</th>
								   <th width="13%">审核状态</th>
								   <th width="10%">申请的分片</th>
								   <th width="10%">申请的容量</th>
								   <th width="13%">申请时间</th>
								   <th width="17%">操作</th>
							</tr></thead>
						 	
						 	<tbody id="devCPTableBody">
						 		<% for (int i =0; saList!=null && i< saList.size(); i++ ) { 
						 			ScaleoutAppeal sa = saList.get(i);
						 			String statusDesc = "待审";
						 			if (sa.getStatus() == -1) {
						 				statusDesc =  "拒批";
						 			} else if (sa.getStatus() == 1) {
						 				statusDesc =  "通过";
						 			} 
						 		%>
						 		<tr>
									<td>&nbsp;</td>
									<td><%= sa.getAppId() %></td>
									<td><a href="/memcloud/app/<%= sa.getAppId() %>.html"><%= sa.getAppName() %></a></td>
									<td><%=statusDesc %></td>
									<td><%=sa.getShardNum() %></td>
									<td><%=sa.getMemSize() %>M</td>
									<td><%=DateUtil.format(sa.getCreateTime()) %></td>
									<td align="center">
									<!-- Icons -->
									<a href="#" title="Edit"><img src="/resources/images/icons/pencil.png" alt="Edit"/></a>
									<a href="#" title="Delete"><img src="/resources/images/icons/cross.png" alt="Delete"/></a> 
									<a href="#" title="Edit Meta"><img src="/resources/images/icons/hammer_screwdriver.png"alt="Edit Meta" /></a>
									</td>
								</tr>
						 		<% } %>
						 		
							</tbody>
						 
							<tfoot><!--列表尾（翻页） -->
								<tr>
									<td colspan="6">
										<div class="bulk-actions align-left"></div>
										<div class="pagination" id="devCPTablePage">
										</div><!-- End .pagination -->
										<div class="clear"></div>
									</td>
								</tr>
							</tfoot>
						 	
							<tbody id="devCPTableBody"><!--列表体（记录展现区） -->
								<!--记录列表动态生成区 -->
							</tbody>
					  </table>
					</div> <!-- End #tab1 -->
					
				  <div class="tab-content" id="tab2"></div><!-- End #tab2 -->
				  
			</div> <!-- End .content-box-content -->
		</div> <!-- End .content-box -->
			
<%@include file="/WEB-INF/include/foot.jsp" %>

</div><!-- End #main-content-->

<script type="text/javascript">
	
</script>
</body>
</html>

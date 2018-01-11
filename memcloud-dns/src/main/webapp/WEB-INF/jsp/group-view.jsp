<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="com.github.downgoon.jresty.rest.model.UnifiedResponse,com.github.downgoon.jresty.rest.model.UnifiedResponseCode" %>
<%@page import="java.util.List,io.memcloud.memdns.dao.entry.AppMemGroup" %>
<%@ include file="../conf.jsp"%>
<%
	UnifiedResponse  up = (UnifiedResponse)request.getAttribute("model");
	List<AppMemGroup> shardList = (List<AppMemGroup>)up.getAttachment();
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
			<!-- Page Head -->
			<h3>指数应用的集群配置情况</h3>
			<div class="notification error png_bg" id="devErrorIcon" style="display: none"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
                <div id="devErrorInfo"></div>
		  	</div>
			<div class="notification success png_bg" id="devSuccIcon" style="display: none"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
                <div id="devSuccInfo"></div>
		  	</div>
			<div class="notification success png_bg" id="devWaitIcon" style="display: none"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
                <div id="devWaitInfo"></div>
		  	</div>
		    <div class="content-box"><!-- Start Content Box -->
				<div class="content-box-header">
					<h3>MemSharding列表</h3>
					<ul class="content-box-tabs">
						<li></li> <!-- href must be unique and match the id of target div -->
					</ul>
					<div class="clear"></div>
				</div> <!-- End .content-box-header -->
				
				<div class="box-content">
					<div class="default-tab" id="tab1"> <!-- This is the target div. id must match the href of this div's tab -->
					  <table id="devCPTable">
							<thead><tr><!--列表头 -->
								   <th width="6%">&nbsp;</th>
								   <th width="15%">分片号</th>
								   <th width="26%">MasterInstance</th>
								   <th width="26%">SlaveInstance</th>
								   <th width="17%">操作</th>
							</tr></thead>
						 	
						 	<tbody id="devCPTableBody">
						 		<% for (int i =0; shardList!=null && i< shardList.size(); i++ ) { %>
						 		<tr>
									<td>&nbsp;</td>
									<td>S<%= (i+1) %></td>
									<td><a target="_blank" href="/memcloud/stat/<%= shardList.get(i).getMasterIP() %>:<%= shardList.get(i).getMasterPort() %>.html"><%= shardList.get(i).getMasterIP() %>:<%= shardList.get(i).getMasterPort() %></a></td>
									<td><a target="_blank" href="/memcloud/stat/<%= shardList.get(i).getSlaveIP() %>:<%= shardList.get(i).getSlavePort() %>.html"><%= shardList.get(i).getSlaveIP() %>:<%= shardList.get(i).getSlavePort() %></a></td>
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
			
<!-- Start Notifications -->
<!-- End Notifications -->
<%@include file="/WEB-INF/include/foot.jsp" %>

</div><!-- End #main-content-->

<script type="text/javascript">
	//loadAppRecords(pagination_firstpage_index,pagination_page_size);

	//top.window.ttt=111;
	
</script>
</body>
</html>

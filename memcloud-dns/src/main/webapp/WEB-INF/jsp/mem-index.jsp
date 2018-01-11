<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="com.github.downgoon.jresty.rest.model.UnifiedResponse,com.github.downgoon.jresty.rest.model.UnifiedResponseCode" %>
<%@page import="java.util.*,io.memcloud.memdns.dao.entry.MemInstancePeer" %>
<%@ include file="../conf.jsp"%>
<%
	UnifiedResponse  up = (UnifiedResponse)request.getAttribute("model");
	List<MemInstancePeer> peers = (List<MemInstancePeer>)up.getAttachment();
	if (peers == null) {
		peers = new ArrayList<MemInstancePeer>();
	}
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>管理员-资源池</title>
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
<div id="main-content" style="margin-left:20px; padding-top:10px;"> <!-- Main Content Section with everything -->
			<noscript> <!-- Show a notification if the user has disabled javascript -->
			</noscript>
			<!-- Page Head -->
			<h3>资源池的空闲分片</h3>
		    <div class="content-box"><!-- Start Content Box -->
				<div class="content-box-header">
					<h3>空闲分片</h3>
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
								   <th width="15%">序号</th>
								   <th width="13%">内存配置</th>
								   <th width="23%">MasterInstance</th>
								   <th width="13%">SlaveInstance</th>
								   <th width="13%">同步端口</th>
								   <th width="17%">操作</th>
							</tr></thead>
						 	
						 	<tbody id="devCPTableBody">
						 		<% for (int i =0; peers!=null && i< peers.size(); i++ ) { 
						 			MemInstancePeer p = peers.get(i);
						 		%>
						 		<tr>
									<td>&nbsp;</td>
									<td><%= (i+1) %></td>
									<td><%=p.getMem() %>M</td>
									<td><%=p.getMhost() %>:<%=p.getMport() %></td>
									<td><%=p.getShost() %>:<%=p.getSport() %></td>
									<td><%=p.getRepcPort() %></td>
									<td align="center">
									<!-- Icons -->
									<a href="#" title="Edit"><img src="/resources/images/icons/pencil.png" alt="Edit"/></a>
									<a href="#" title="Delete"><img src="/resources/images/icons/cross.png" alt="Delete"/></a> 
									<a href="#" title="Edit Meta"><img src="/resources/images/icons/hammer_screwdriver.png"alt="Edit Meta" /></a>
									</td>
								</tr>
						 		<% } %>
						 		
							</tbody>
						 
							<tfoot>
								<tr>
									<td colspan="6">
										<div class="bulk-actions align-left"></div>
										<div class="pagination" id="devCPTablePage">
										</div><!-- End .pagination -->
										<div class="clear"></div>
									</td>
								</tr>
							</tfoot>
						 	
							<tbody id="devCPTableBody">
							</tbody>
					  </table>
					</div>
					
				  <div class="tab-content" id="tab2"></div>
				  
			</div>
		</div>
			
<%@include file="/WEB-INF/include/foot.jsp" %>

</div><!-- End #main-content-->

<script type="text/javascript">

	
</script>
</body>
</html>

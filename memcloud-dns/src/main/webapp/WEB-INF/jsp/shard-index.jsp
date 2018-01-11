<%@page contentType="text/html;charset=UTF-8" %>
<%@page import="com.github.downgoon.jresty.rest.model.UnifiedResponse,com.github.downgoon.jresty.rest.model.UnifiedResponseCode" %>
<%@page import="java.util.*, io.memcloud.memdns.dao.entry.AppMemGroup,io.memcloud.memdns.dao.entry.AppDesc" %>
<%@page import="io.memcloud.stats.model.MemStatSummary" %>
<%@page import="com.github.downgoon.jresty.commons.utils.*" %>

<%@ include file="../conf.jsp"%>
<%
    boolean isArgErr = false;//输入参数错误
    UnifiedResponse  up = (UnifiedResponse)request.getAttribute("model");

    if (up.getStatus() >= 400 && up.getStatus()<500) {
    	isArgErr = true;
    }
    
    Map<String,Object> attachment = (Map<String,Object>)up.getAttachment();
    List<AppMemGroup> shardList = null;
    Map<Long, AppDesc> appMap = null;
    Map<String, MemStatSummary> summaryMap = null;
    
    if ( !isArgErr && attachment!=null ) {
    	shardList =  (List<AppMemGroup>)attachment.get("shardList");
    	appMap = (Map<Long, AppDesc>)attachment.get("appMap");
    	summaryMap = (Map<String, MemStatSummary>)attachment.get("summaryMap");
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
			<!-- Page Head -->
			<h3>我的监控</h3>

		  	<% if (isArgErr) { %>
				<div class="notification error png_bg"> <a href="#" class="close"><img src="/resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
        			<div>错误代码：<%=up.getStatus() %>；错误信息：<%=up.getMessage() %>。</div>
        			<div><%=(up.getDebug()==null ? "" : up.getDebug()) %></div>
  				</div>
  			<% } %>
		  	
		  	<% if ( !isArgErr ) { %>
		    <div class="content-box"><!-- Start Content Box -->
				<div class="content-box-header">
					<h3>全部实例</h3>
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
								   <th width="5%">序号</th>
								   <th width="15%">MemInstance</th>
								   <th width="5%">M/S</th>
								   <th width="10%">内存使用率</th>
								   <th width="10%">缓存命中率</th>
								   <th width="10%">应用名称</th>
								   <th width="10%">集群</th>
							</tr></thead>
						 	
						 	<tbody id="devCPTableBody">
						 		<% 
						 		int count = 0;
						 		for (int i =0; shardList!=null && i< shardList.size(); i++ ) { 
						 			AppMemGroup g = shardList.get(i);
						 			String masterInst = g.getMasterIP()+":"+g.getMasterPort();
						 			String slaveInst = g.getSlaveIP()+":"+g.getSlavePort();
						 			Long appid = g.getAppId();
						 			AppDesc appDesc = appMap.get(appid);
						 			MemStatSummary sm = summaryMap.get(masterInst);//Master
						 			MemStatSummary ss = summaryMap.get(slaveInst);//Slave
						 		%>
						 		<tr>
									<td>&nbsp;</td>
									<td>N<%=(count+1) %></td>
									<td><a target="_blank" href="/memcloud/stat/<%=masterInst %>.html"><%=masterInst %></a></td>
									<td>M</td>
									<td><%=(sm==null? "--" : HumanizedFormator.percentFormat(sm.getUsedPercentage()) ) %></td>
									<td><%=(sm==null? "--" : HumanizedFormator.percentFormat(sm.getHitPercentage()) ) %></td>
									<td><a href="/memcloud/app/<%=appid %>.html"><%=appDesc.getName() %></a></td>
									<td><a href="/memcloud/group/<%=appid %>.html">集群</a></td>
									
								</tr>
						 		<tr>
									<td>&nbsp;</td>
									<td>N<%=(count+2) %></td>
									<td><a target="_blank" href="/memcloud/stat/<%=slaveInst %>.html"><%=slaveInst %></a></td>
									<td>S</td>
									<td><%=(ss==null? "--": HumanizedFormator.percentFormat(ss.getUsedPercentage()) ) %></td>
									<td><%=(ss==null? "--": HumanizedFormator.percentFormat(ss.getHitPercentage()) ) %></td>
									<td><a href="/memcloud/app/<%=appid %>.html"><%=appDesc.getName() %></a></td>
									<td><a href="/memcloud/group/<%=appid %>.html">集群</a></td>
								</tr>
						 		<% 
						 		count += 2;
						 		} %>
						 		
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
						 	
							<tbody id="devCPTableBody">
							</tbody>
					  </table>
					</div> <!-- End #tab1 -->
					
				  <div class="tab-content" id="tab2"></div><!-- End #tab2 -->
				  
			</div> <!-- End .content-box-content -->
		</div> <!-- End .content-box -->
		<% } %>
			
<%@include file="/WEB-INF/include/foot.jsp" %>

</div><!-- End #main-content-->

<script type="text/javascript">
	
</script>
</body>
</html>

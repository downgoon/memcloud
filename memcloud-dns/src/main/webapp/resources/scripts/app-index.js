/**
 * 页面加载完，立即加载数据集
 * @param {Integer} pageNumber	当前要显示的页码
 * @param {Integer} pageSize  每页显示记录数（默认值10）
 */
function loadAppRecords(pageNumber,pageSize) {

	var pnumber = pagination_firstpage_index;
	var psize = pagination_page_size;
	if(pageNumber > pagination_firstpage_index) {
		pnumber = pageNumber;
	}
	if(pageSize!=null && pageSize>1) {
		psize = pageSize;
	}
	//构建HTTP请求参数
	var request = { "psize" :  psize,
					"pnumber" :  pnumber
				};

	//构建Ajax请求提交表单信息
	$.ajax({url:"/memcloud/app-index.json",
			type: "post",//POST方式提交表单内容
			timeout : 8000,//响应超时间8秒
			async : true,//同步提交（注：0904号从异步修改成同步方式，原因是异步方式在iframe里面列表数据只能显示6条）
			cache : false,//禁止浏览器缓存
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",//明确告诉服务器协商编码方式，以免服务器端出现中文乱码
			beforeSend : function(xmlHttpRequest){//请求发送前的逻辑
//				showWaitingInfo("正在加载版权提供商信息 ...");
//				scroll(0,0);//回到顶部，以提高用户体验//注：0904号把滚动到页面顶部去掉，否则翻页时抖动太大。
            },
			dataType : "json",
			data : request,//HTTP请求数据（表单数据）
			success : function(response,httpStatus) {//HTTP响应成功后的处理逻辑
				if(response["status"]==200) {
					hideAllNotifyInfos();
					removeCopyrightProviderRecords();//清除现有的记录集（如果翻页时失败，老数据依然保留显示，并提示新数据加载失败）
					displayCopyrightProviderRecords(response);//显示新加载的记录集
					removeCopyrightProviderPagination();//清除现有的翻页按钮
					displayCopyrightProviderPagination(response);//显示新的翻页按钮
					adjustIframeWorkspaceHeight();
				} else {
					showErrorInfo("版权提供商信息加载失败，返回码："+response["status"]+"，原因："+response["data"]);
					scroll(0,0);//回到顶部，以提高用户体验					
				}
			}
		});
}


/** 清除当前记录集*/
function removeCopyrightProviderRecords() {
	var holderid = "devCPTableBody";
	var holder = document.getElementById(holderid);
	//参考链接： http://www.w3school.com.cn/htmldom/dom_nodes_access.asp
	var firstChild = holder.firstChild; 
	while(firstChild!=null) {
		holder.removeChild(firstChild);
		firstChild = holder.firstChild;
	}
}

/**
 * 数据加载成功后前端展现记录集
 * （前提：Ajax响应一定属成功才调用该方法）
 * @param responseJSON 响应结果集 
 */
function displayCopyrightProviderRecords(resJSON) {
	var holderid = "devCPTableBody";
	
		
	var itemsCount = 0;  //当前响应数据集包含的页数
	if(resJSON["data"]["items"] != null) {
		itemsCount = resJSON["data"]["items"].length ;
	}
	
	if(itemsCount == 0) {
		showSuccInfo("目前尚未录入版权提供商信息");
		return;
	} 
	
	var hasViewPer = PermissionStore.getInstance().hasUrl("/payment/copyright-provider-view");//是否有查看权限
	var hasUpdatePer = PermissionStore.getInstance().hasUrl("/payment/copyright-provider-update");//是否有修改权限
	
	for(var i=0;i<itemsCount;i++){
		
		var item = resJSON["data"]["items"][i];
		var cpid = item["id"];
		var cpname = item["name"];
		var cplink = null;
		if(hasViewPer) {
			cplink = "/payment/copyright-provider-view.html?id="+cpid;//查看连接 
		}
		var cpperson = item["mysideperson"];
		var cpphone = item["mysidephone"];
		var cpstatus = (item["feestop"]==0 ? "正常" : "暂停");//注：表示是否停止计费，而不是记录的存在性
		
		var cpeditLink = null;
		if(hasUpdatePer) {
			cpeditLink = "/payment/copyright-provider-update.html?id="+cpid;//修改链接
		}
		var cpviewLink = null;
		if(hasViewPer) {
			cpviewLink = "/payment/copyright-provider-view.html?id="+cpid;//查看链接
		}
		
		var cpdelLink = null;//删除链接
		
		appendRecordByHTML(holderid,
						cpid,cpname,cplink,
						cpperson,cpphone,
						cpstatus,
						cpeditLink,cpdelLink,cpviewLink);
	}
	
	return ;
	
}



/**
 * 数据加载成功后前端展现翻页
 * （前提：Ajax响应一定属成功才调用该方法）
 * @param responseJSON 响应结果集 
 */
function displayCopyrightProviderPagination(resJSON) {
	var holderid = "devCPTablePage";
	
	var curPageNum = resJSON["data"]["pnumber"]; //当前页码
	var totalPageCount = resJSON["data"]["totalPage"];//数据库中所含页总数
	//显示首页链接
	//<a href="#" title="First Page">&laquo; First</a>
	if(curPageNum != pagination_firstpage_index) {
		appendPaginationWithClientEvent(holderid, "loadAppRecords(" + pagination_firstpage_index +","+ pagination_page_size +")", "第一页","&laquo; 第一页",null);
	}
	//显示上一页链接
	//<a href="#" title="PreviousPage">&laquo; Previous</a>
	if(curPageNum > pagination_firstpage_index) {
		appendPaginationWithClientEvent(holderid, "loadAppRecords(" + (curPageNum-1) +","+ pagination_page_size +")", "上一页","&laquo; 上一页",null);
	}
	
	//显示普通按钮翻页按钮（参考google翻页按钮的布局原则：当前页尽可能在中间，左/右侧不够时向不够的那一侧靠拢）
	//一次最多显示10个翻页按钮，翻页按钮控制在：[startPaginationIndex,endPaginationIndex)前闭后开的区间
	var startPaginationIndex =  (curPageNum - (pagination_span_size/2));
	var endPaginationIndex = (curPageNum + (pagination_span_size/2 + pagination_span_size%2));
	//此时：endPaginationIndex - startPaginationIndex 恒等于 pagination_span_size
	//左边缺少的
	var leftLack = (startPaginationIndex-pagination_firstpage_index<0 ? pagination_firstpage_index-startPaginationIndex : 0);
	//右边缺少的
	var rightLack = (endPaginationIndex-totalPageCount>0 ? endPaginationIndex-totalPageCount : 0);
	startPaginationIndex = Math.max(startPaginationIndex-rightLack, pagination_firstpage_index);//右边缺少的，得在左边补充，但是不得超过左边边界
	endPaginationIndex = Math.min(endPaginationIndex+leftLack, totalPageCount);//左边缺少的，得在右边补充，但是不得超过右边边界
	
	//<a href="#" class="number" title="1">1</a>
	//<a href="#" class="number current" title="3">3</a>
	for(var i=startPaginationIndex; i<endPaginationIndex; i++) {
		var funCallString = "loadAppRecords(" + i +","+ pagination_page_size +")";
		var classAttr = "number";//表示普通翻页按钮
		if(i == curPageNum) {
			classAttr = "number current";//表示当前页翻页按钮
		}
		appendPaginationWithClientEvent(holderid,funCallString,i+1,i+1,classAttr);//注意：尽管后台翻页从0开始编号，但是显示在前端还是从1编号
	}
	//显示下一页链接
	//<a href="#" title="Next Page">Next &raquo;</a>
	if(curPageNum<totalPageCount-1) {
		appendPaginationWithClientEvent(holderid, "loadAppRecords(" + (curPageNum+1) +","+ pagination_page_size +")", "下一页","下一页 &raquo;",null);
	}
	//显示尾页链接
	//<a href="#" title="LastPage">Last &raquo;</a>
	if(curPageNum != totalPageCount-1) {
		appendPaginationWithClientEvent(holderid, "loadAppRecords(" + (totalPageCount-1) +","+ pagination_page_size +")", "最后页","最后页 &raquo;",null);
	}
	return ;
}

/** 清除当前翻页按钮*/
function removeCopyrightProviderPagination() {
	var holderid = "devCPTablePage";
	var holder = document.getElementById(holderid);
	//参考链接： http://www.w3school.com.cn/htmldom/dom_nodes_access.asp
	var firstChild = holder.firstChild; 
	while(firstChild!=null) {
		holder.removeChild(firstChild);
		firstChild = holder.firstChild;
	}
}

/**
 * 构建连接，并设置属性
 * @param {String} hrefAttr 超级链接地址（必选）
 * @param {String} contentTxt 超级链接内容（必选）
 * @param {String} classAttr 超级链接样式（可选参，可以空）
 * 
 * 样例：
 * <a href="#" title="First Page">&laquo; First</a>
 * <a href="#" title="PreviousPage">&laquo; Previous</a>
 * <a href="#" class="number" title="1">1</a>
 * <a href="#" class="number" title="2">2</a>
 * <a href="#" class="number current" title="3">3</a>
 * <a href="#" class="number" title="4">4</a>
 * <a href="#" title="Next Page">Next &raquo;</a>
 * <a href="#" title="LastPage">Last &raquo;</a>
 */
function generateHyperLink(hrefAttr,titleAttr,contentTxt,classAttr) {
	var hyperlink = document.createElement("a");
	hyperlink.setAttribute("href",hrefAttr);
	hyperlink.setAttribute("title",titleAttr);
	if(!(classAttr==null || classAttr=="")) {//只当不空时，才加class属性
		hyperlink.setAttribute("class",classAttr);
	}
	//设置超级链接内容
	if(contentTxt==null || contentTxt=="") {
//		hyperlink.appendChild(document.createTextNode(""));
		hyperlink.innerHTML = "";
	}else {
//		hyperlink.appendChild(document.createTextNode(contentTxt));//HTML支持不了
		hyperlink.innerHTML = contentTxt;
	}
	return hyperlink;
}


/**
 * 构建连接，并设置属性
 * @param {Object} holder  翻页挂靠点（必选）
 * @param {String} hrefAttr 超级链接地址（必选）  使用的是超级链接
 * @param {String} contentTxt 超级链接内容（必选）
 * @param {String} classAttr 超级链接样式（可选参，可以空）
 */
function appendPagination(holder, hrefAttr, titleAttr, contentTxt, classAttr){
	var hyperlink = generateHyperLink(hrefAttr, titleAttr, contentTxt, classAttr);
	holder.appendChild(hyperlink);
}

/**
 * 构建连接，并设置onclick事件
 * @param {Object} holderID
 * @param {Object} funCallString	函数调用的字符串形式
 * @param {Object} titleAttr
 * @param {Object} contentTxt
 * @param {Object} classAttr
 */
function appendPaginationWithClientEvent(holderID, funCallString, titleAttr, contentTxt, classAttr){
	var holder = document.getElementById(holderID);
	//样式：<a href="#" onclick="loadAppRecords(1,10);" title="First Page">&laquo; First</a>
	
	if(!(classAttr==null || classAttr=="")) {//只当不空时，才加class属性
		holder.innerHTML += "<a href=\"#\" onclick=\"" + funCallString +"\" class=\"" +  classAttr +  "\" title=\"" + titleAttr  +"\">"+ contentTxt +"</a>"
	} else {//class="number"
		holder.innerHTML += "<a href=\"#\" onclick=\"" +funCallString+ "\" title=\""+ titleAttr + "\">" +contentTxt+ "</a>";
	}
}

/** 追加记录
 * 
 * @param {Object} holderID
 * @param {Object} providerID
 * @param {Object} providerName
 * @param {Object} providerLink
 * @param {Object} providerPerson
 * @param {Object} providerPhone
 * @param {Object} providerStatus
 * @param {Object} editLink		修改链接（可空，如果为空，则不显示修改图标）
 * @param {Object} deleteLink	删除链接（可空，如果为空，则不显示删除图标）
 */
function appendRecordByHTML(holderID, providerID, providerName, providerLink, providerPerson, providerPhone, providerStatus, 
		editLink,deleteLink,viewLink){
	
	var holder = document.getElementById(holderID);
	/*
样例：
<tbody id="devCPTableBody">
<tr>
	<td>&nbsp;</td>
	<td>PA0001</td>
	<td><a href="#">索尼影视</a></td>
	<td>某某(010-85651234)</td>
	<td>正常</td>
	<td align="center">
	<!-- Icons -->
	<a href="#" title="Edit"><img src="/resources/images/icons/pencil.png" alt="Edit"/></a>
	<a href="#" title="Delete"><img src="/resources/images/icons/cross.png" alt="Delete"/></a> 
	<a href="#" title="Edit Meta"><img src="/resources/images/icons/hammer_screwdriver.png"alt="Edit Meta" /></a>
	</td>
</tr>
</tbody>
 */
	var record = "<tr>";
	record += "<td>&nbsp;</td>";
	record += "<td>"+providerID+"</td>";
	if(providerLink == null || providerLink == "") {
		record += "<td><a href=\""+ "#" +"\">"+ providerName +"</a></td>";
	} else {
		record += "<td><a href=\""+ providerLink +"\">"+ providerName +"</a></td>";//注：target=_top不好的地方是整个页面（包括左边的导航栏）都要重新加载
	}
	record += "<td>"+providerPerson+"("+providerPhone+")</td>";
	record += "<td>"+providerStatus+"</td>";
	record += "<td align=\"center\">";
	
	var icons = "";
	if(!(editLink==null || editLink=="")) {//修改链接为空时
		icons += "<a href=\""+ editLink +"\" title=\"修改\" ><img src=\"/resources/images/icons/pencil.png\" alt=\"修改\"/></a>";
	}
	if(!(deleteLink==null || deleteLink=="")) {//删除链接为空时
		icons += "<a href=\""+ deleteLink +"\" title=\"删除\"><img src=\"/resources/images/icons/cross.png\" alt=\"删除\"/></a>";
	}
	if(!(viewLink==null || viewLink=="")) {//查询链接为空时
		icons += "<a href=\""+ viewLink +"\" title=\"查看\" ><img src=\"/resources/images/icons/hammer_screwdriver.png\" alt=\"查看\"/></a>";
	}
	if(icons==null || icons=="") {
		icons = "&nbsp;";
	}
	record += icons;
	
	record += "</td>";
	record += "</tr>";
	
//	holder.innerHTML += record;//not supported by IE (ref: http://www.kukandy.com/web/js_18395.shtml)
//	$("#devCPTable").html(record);
	$("#devCPTable").append(record);//append method make innerHTML appended, while html method make innerHTML overwritten

}

/** 前端通用常量配置*/
var pagination_firstpage_index = 0;//翻页起始编号（简称：首页编号）（必须大于等于0）
var pagination_span_size = 10;//翻页时页面上最多显示的翻页图标数（简称：跨大小）（必须大于等于1）
var pagination_page_size = 10;//翻页时一页最多显示的记录数（简称：页大小）（必须：大于等于1）


/** 调整iframe的高度，以显示更多记录（当出现遮盖时调用）*/
//var iframe_workspace_adjusted = false;
function adjustIframeWorkspaceHeight() {
//	if(! iframe_workspace_adjusted) {
		//注：0904号设置iframe的高度，必须在回调函数中设置。这样Ajax既可用异步，又可用同步。
		if(top.document.getElementById("workspace") != null) {
			top.document.getElementById("workspace").height=document.body.scrollHeight;
			iframe_workspace_adjusted = true;
		}
//	}
}

//var iframe_navigation_adjusted = false;
function adjustIframeNavigationHeight() {
//	if(! iframe_navigation_adjusted) {
		//注：0904号设置iframe的高度，必须在回调函数中设置。这样Ajax既可用异步，又可用同步。
		if(top.document.getElementById("navigation") != null) {
			top.document.getElementById("navigation").height=document.body.scrollHeight;
			iframe_navigation_adjusted = true;
		}
//	}
}

/** 除去错误信息，并隐藏显示栏*/
function hideErrorInfo() {
	document.getElementById("devErrorInfo").innerHTML="";
	var erricon = document.getElementById("devErrorIcon");
	erricon.style.display ='none';
}

/** 除去成功信息，并隐藏显示栏目*/
function hideSuccInfo() {
	document.getElementById("devSuccInfo").innerHTML="";
	var succIcon = document.getElementById("devSuccIcon");
	succIcon.style.display ='none';
}

/** 除去等待提示信息，并隐藏等待提示栏目*/
function hideWaitingInfo() {
	document.getElementById("devWaitInfo").innerHTML="";
	var waitIcon = document.getElementById("devWaitIcon");
	waitIcon.style.display ='none';
}

function hideAllNotifyInfos() {
	hideErrorInfo();
	hideSuccInfo();
	hideWaitingInfo();
}

/**
 * @param {Object} errorinfo 待显示的错误信息
 */
function showErrorInfo(errorinfo) {
	hideSuccInfo();
	hideWaitingInfo();//互斥显示
	document.getElementById("devErrorInfo").innerHTML=errorinfo;
//	document.getElementById("devErrorInfo").innerText=errorinfo;
	var erricon = document.getElementById("devErrorIcon");
	erricon.style.display ='';
}

/**
 * @param {Object} succinfo 待显示的成功信息
 */
function showSuccInfo(succinfo) {
	hideErrorInfo();
	hideWaitingInfo();//互斥显示
	document.getElementById("devSuccInfo").innerHTML=succinfo;
//	document.getElementById("devSuccInfo").innerText=succinfo;
	var succIcon = document.getElementById("devSuccIcon");
	succIcon.style.display ='';
}

/**
 * @param {Object} waitinginfo 待显示的等待提示信息
 */
function showWaitingInfo(waitinginfo) {
	hideErrorInfo();
	hideWaitingInfo();//互斥显示
	document.getElementById("devWaitInfo").innerHTML=waitinginfo;
//	document.getElementById("devWaitInfo").innerText=waitinginfo;
	var waitIcon = document.getElementById("devWaitIcon");
	waitIcon.style.display ='';
}

///////////////////////////////////////////////////////////////////////////////////////////
/** 用户权限存器（单例模式）
 *  使用场景：用户登录后，用户所拥有的权限集合被存储在该对象中。该对象提供判断当前用户是否拥有某个URL或名称的访问权限。
 *  注意：如果权限集为空，判断是否拥有权限时，在开发模式下返回true； 生产模式返回 false。
 * */
function PermissionStore() {
	this.store = null;
//	this.devMode = true;//开发模式（因为开发模式下不进行授权判断）
	this.devMode = false;//非开发模式
}
//PermissionStore._instance = null;//假设为隐藏字段（注：这种全局变量再次加载的时候会被覆盖）
//top.window._permission_store_instance = null;//注：这样也是不行的，因为第二个页面引用这段js的时候照样会把覆盖
PermissionStore.getInstance = function() {
	
	if(top.window._permission_store_instance == 'undefined' || top.window._permission_store_instance == null ) {
		top.window._permission_store_instance = new PermissionStore();
//		top.window._permission_store_instance.setPermissions({"data":{"\u7528\u6237\u6743\u9650\u7ba1\u7406":[{"category":"\u7528\u6237\u6743\u9650\u7ba1\u7406","categoryseq":10,"id":237,"name":"\u6dfb\u52a0\u7528\u6237","nameseq":10,"resource":"/payment/user\u002dcreate","type":0},{"category":"\u7528\u6237\u6743\u9650\u7ba1\u7406","categoryseq":10,"id":236,"name":"\u6dfb\u52a0\u7528\u6237","nameseq":10,"resource":"/payment/user\u002dusercreate","type":0},{"category":"\u7528\u6237\u6743\u9650\u7ba1\u7406","categoryseq":10,"id":235,"name":"\u6dfb\u52a0\u7528\u6237","nameseq":10,"resource":"/payment/user\u002dusercreate\u002ehtml","type":1},{"category":"\u7528\u6237\u6743\u9650\u7ba1\u7406","categoryseq":10,"id":239,"name":"\u5168\u90e8\u7528\u6237","nameseq":20,"resource":"/payment/user\u002duserlist","type":0},{"category":"\u7528\u6237\u6743\u9650\u7ba1\u7406","categoryseq":10,"id":238,"name":"\u5168\u90e8\u7528\u6237","nameseq":20,"resource":"/payment/user\u002duserlist\u002ehtml","type":1},{"category":"\u7528\u6237\u6743\u9650\u7ba1\u7406","categoryseq":10,"id":243,"name":"\u4fee\u6539\u7528\u6237","nameseq":30,"resource":"/payment/user\u002dupdate","type":0},{"category":"\u7528\u6237\u6743\u9650\u7ba1\u7406","categoryseq":10,"id":240,"name":"\u4fee\u6539\u7528\u6237","nameseq":30,"resource":"/payment/user\u002duserlist\u002ehtml","type":1},{"category":"\u7528\u6237\u6743\u9650\u7ba1\u7406","categoryseq":10,"id":242,"name":"\u4fee\u6539\u7528\u6237","nameseq":30,"resource":"/payment/user\u002duserupdate","type":0},{"category":"\u7528\u6237\u6743\u9650\u7ba1\u7406","categoryseq":10,"id":241,"name":"\u4fee\u6539\u7528\u6237","nameseq":30,"resource":"/payment/user\u002duserupdate\u002ehtml","type":0}],"\u4ea7\u54c1\u5305\u7ba1\u7406":[{"category":"\u4ea7\u54c1\u5305\u7ba1\u7406","categoryseq":20,"id":245,"name":"\u6dfb\u52a0\u4ea7\u54c1\u5305","nameseq":10,"resource":"/payment/productCategory\u002dcatecreate","type":0},{"category":"\u4ea7\u54c1\u5305\u7ba1\u7406","categoryseq":20,"id":244,"name":"\u6dfb\u52a0\u4ea7\u54c1\u5305","nameseq":10,"resource":"/payment/productCategory\u002dcatecreate\u002ehtml","type":1},{"category":"\u4ea7\u54c1\u5305\u7ba1\u7406","categoryseq":20,"id":246,"name":"\u6dfb\u52a0\u4ea7\u54c1\u5305","nameseq":10,"resource":"/payment/productCategory\u002dcreate","type":0},{"category":"\u4ea7\u54c1\u5305\u7ba1\u7406","categoryseq":20,"id":248,"name":"\u5168\u90e8\u4ea7\u54c1\u5305","nameseq":20,"resource":"/payment/productCategory\u002dcatelist","type":0},{"category":"\u4ea7\u54c1\u5305\u7ba1\u7406","categoryseq":20,"id":247,"name":"\u5168\u90e8\u4ea7\u54c1\u5305","nameseq":20,"resource":"/payment/productCategory\u002dcatelist\u002ehtml","type":1},{"category":"\u4ea7\u54c1\u5305\u7ba1\u7406","categoryseq":20,"id":249,"name":"\u67e5\u770b\u4ea7\u54c1\u5305","nameseq":30,"resource":"/payment/productCategory\u002dcatelist\u002ehtml","type":1},{"category":"\u4ea7\u54c1\u5305\u7ba1\u7406","categoryseq":20,"id":251,"name":"\u67e5\u770b\u4ea7\u54c1\u5305","nameseq":30,"resource":"/payment/productCategory\u002dview","type":0},{"category":"\u4ea7\u54c1\u5305\u7ba1\u7406","categoryseq":20,"id":250,"name":"\u67e5\u770b\u4ea7\u54c1\u5305","nameseq":30,"resource":"/payment/productCategory\u002dview\u002ehtml","type":0}],"\u7248\u6743\u7ba1\u7406":[{"category":"\u7248\u6743\u7ba1\u7406","categoryseq":30,"id":253,"name":"\u6dfb\u52a0\u7248\u6743","nameseq":10,"resource":"/payment/copyright\u002dprovider\u002dcreate","type":0},{"category":"\u7248\u6743\u7ba1\u7406","categoryseq":30,"id":252,"name":"\u6dfb\u52a0\u7248\u6743","nameseq":10,"resource":"/payment/copyright\u002dprovider\u002dcreate\u002ehtml","type":1},{"category":"\u7248\u6743\u7ba1\u7406","categoryseq":30,"id":255,"name":"\u5168\u90e8\u7248\u6743","nameseq":20,"resource":"/payment/copyright\u002dprovider\u002dindex","type":0},{"category":"\u7248\u6743\u7ba1\u7406","categoryseq":30,"id":254,"name":"\u5168\u90e8\u7248\u6743","nameseq":20,"resource":"/payment/copyright\u002dprovider\u002dindex\u002ehtml","type":1},{"category":"\u7248\u6743\u7ba1\u7406","categoryseq":30,"id":256,"name":"\u4fee\u6539\u7248\u6743","nameseq":30,"resource":"/payment/copyright\u002dprovider\u002dindex\u002ehtml","type":1},{"category":"\u7248\u6743\u7ba1\u7406","categoryseq":30,"id":258,"name":"\u4fee\u6539\u7248\u6743","nameseq":30,"resource":"/payment/copyright\u002dprovider\u002dupdate","type":0},{"category":"\u7248\u6743\u7ba1\u7406","categoryseq":30,"id":257,"name":"\u4fee\u6539\u7248\u6743","nameseq":30,"resource":"/payment/copyright\u002dprovider\u002dupdate\u002ehtml","type":0},{"category":"\u7248\u6743\u7ba1\u7406","categoryseq":30,"id":259,"name":"\u67e5\u770b\u7248\u6743","nameseq":30,"resource":"/payment/copyright\u002dprovider\u002dindex\u002ehtml","type":1},{"category":"\u7248\u6743\u7ba1\u7406","categoryseq":30,"id":261,"name":"\u67e5\u770b\u7248\u6743","nameseq":40,"resource":"/payment/copyright\u002dprovider\u002dview","type":0},{"category":"\u7248\u6743\u7ba1\u7406","categoryseq":30,"id":260,"name":"\u67e5\u770b\u7248\u6743","nameseq":30,"resource":"/payment/copyright\u002dprovider\u002dview\u002ehtml","type":0}],"\u8d60\u54c1\u7ba1\u7406":[{"category":"\u8d60\u54c1\u7ba1\u7406","categoryseq":40,"id":264,"name":"\u6dfb\u52a0\u8d60\u54c1","nameseq":10,"resource":"/payment/gift\u002dcreate","type":0},{"category":"\u8d60\u54c1\u7ba1\u7406","categoryseq":40,"id":263,"name":"\u6dfb\u52a0\u8d60\u54c1","nameseq":10,"resource":"/payment/gift\u002dzpcreate","type":0},{"category":"\u8d60\u54c1\u7ba1\u7406","categoryseq":40,"id":262,"name":"\u6dfb\u52a0\u8d60\u54c1","nameseq":10,"resource":"/payment/gift\u002dzpcreate\u002ehtml","type":1},{"category":"\u8d60\u54c1\u7ba1\u7406","categoryseq":40,"id":266,"name":"\u5168\u90e8\u8d60\u54c1","nameseq":20,"resource":"/payment/gift\u002dzplist","type":0},{"category":"\u8d60\u54c1\u7ba1\u7406","categoryseq":40,"id":265,"name":"\u5168\u90e8\u8d60\u54c1","nameseq":20,"resource":"/payment/gift\u002dzplist\u002ehtml","type":0},{"category":"\u8d60\u54c1\u7ba1\u7406","categoryseq":40,"id":270,"name":"\u4fee\u6539\u8d60\u54c1","nameseq":30,"resource":"/payment/gift\u002dupdate","type":0},{"category":"\u8d60\u54c1\u7ba1\u7406","categoryseq":40,"id":267,"name":"\u4fee\u6539\u8d60\u54c1","nameseq":30,"resource":"/payment/gift\u002dzplist\u002ehtml","type":1},{"category":"\u8d60\u54c1\u7ba1\u7406","categoryseq":40,"id":269,"name":"\u4fee\u6539\u8d60\u54c1","nameseq":30,"resource":"/payment/gift\u002dzpupdate","type":0},{"category":"\u8d60\u54c1\u7ba1\u7406","categoryseq":40,"id":268,"name":"\u4fee\u6539\u8d60\u54c1","nameseq":30,"resource":"/payment/gift\u002dzpupdate\u002ehtml","type":0},{"category":"\u8d60\u54c1\u7ba1\u7406","categoryseq":40,"id":273,"name":"\u67e5\u770b\u8d60\u54c1","nameseq":40,"resource":"/payment/gift\u002dview","type":0},{"category":"\u8d60\u54c1\u7ba1\u7406","categoryseq":40,"id":272,"name":"\u67e5\u770b\u8d60\u54c1","nameseq":40,"resource":"/payment/gift\u002dview\u002ehtml","type":0},{"category":"\u8d60\u54c1\u7ba1\u7406","categoryseq":40,"id":271,"name":"\u67e5\u770b\u8d60\u54c1","nameseq":40,"resource":"/payment/gift\u002dzplist\u002ehtml","type":1}],"\u4ed8\u8d39\u4ea7\u54c1\u7ba1\u7406":[{"category":"\u4ed8\u8d39\u4ea7\u54c1\u7ba1\u7406","categoryseq":50,"id":276,"name":"\u6dfb\u52a0\u4ed8\u8d39\u4ea7\u54c1","nameseq":10,"resource":"/payment/monthlyService\u002dcreate","type":0},{"category":"\u4ed8\u8d39\u4ea7\u54c1\u7ba1\u7406","categoryseq":50,"id":275,"name":"\u6dfb\u52a0\u4ed8\u8d39\u4ea7\u54c1","nameseq":10,"resource":"/payment/monthlyService\u002dmscreate","type":0},{"category":"\u4ed8\u8d39\u4ea7\u54c1\u7ba1\u7406","categoryseq":50,"id":274,"name":"\u6dfb\u52a0\u4ed8\u8d39\u4ea7\u54c1","nameseq":10,"resource":"/payment/monthlyService\u002dmscreate\u002ehtml","type":1},{"category":"\u4ed8\u8d39\u4ea7\u54c1\u7ba1\u7406","categoryseq":50,"id":278,"name":"\u5168\u90e8\u4ed8\u8d39\u4ea7\u54c1","nameseq":20,"resource":"/payment/monthlyService\u002dmslist","type":0},{"category":"\u4ed8\u8d39\u4ea7\u54c1\u7ba1\u7406","categoryseq":50,"id":277,"name":"\u5168\u90e8\u4ed8\u8d39\u4ea7\u54c1","nameseq":20,"resource":"/payment/monthlyService\u002dmslist\u002ehtml","type":1},{"category":"\u4ed8\u8d39\u4ea7\u54c1\u7ba1\u7406","categoryseq":50,"id":279,"name":"\u4fee\u6539\u4ed8\u8d39\u4ea7\u54c1","nameseq":30,"resource":"/payment/monthlyService\u002dmslist\u002ehtml","type":1},{"category":"\u4ed8\u8d39\u4ea7\u54c1\u7ba1\u7406","categoryseq":50,"id":282,"name":"\u4fee\u6539\u4ed8\u8d39\u4ea7\u54c1","nameseq":30,"resource":"/payment/monthlyService\u002dupdate","type":0},{"category":"\u4ed8\u8d39\u4ea7\u54c1\u7ba1\u7406","categoryseq":50,"id":281,"name":"\u4fee\u6539\u4ed8\u8d39\u4ea7\u54c1","nameseq":30,"resource":"/payment/monthlyService\u002dvieworupdate","type":0},{"category":"\u4ed8\u8d39\u4ea7\u54c1\u7ba1\u7406","categoryseq":50,"id":280,"name":"\u4fee\u6539\u4ed8\u8d39\u4ea7\u54c1","nameseq":30,"resource":"/payment/monthlyService\u002dvieworupdate\u002ehtml","type":0}]},"status":200});
	}
	return top.window._permission_store_instance;
	
}

PermissionStore.prototype.getPermissions = function() {
	return this.store;
}
PermissionStore.prototype.setPermissions = function(pers) {
	this.store = pers;
}
PermissionStore.prototype.hasName = function(perName) {
	if(this.store == null) {
		return this.devMode;
	}
	var categoryMap = this.store["data"];
	for(var categoryName in categoryMap) {
		var menuArray = categoryMap[categoryName];//当前分类所包含的菜单列表
		for(var i=0; i < menuArray.length; i++) {//循环菜单数组的元素 ，每个元素是一个菜单 
			if(menuArray[i]["name"] == perName) {//menuArray[i]["resource"]
				return true;
			}
		}
	} 
	return false; 
}

PermissionStore.prototype.hasUrl = function(url) {
	if(this.store == null) {
		return this.devMode;
	}
	var categoryMap = this.store["data"];
	for(var categoryName in categoryMap) {
		var menuArray = categoryMap[categoryName];//当前分类所包含的菜单列表
		for(var i=0; i < menuArray.length; i++) {//循环菜单数组的元素 ，每个元素是一个菜单 
			if(menuArray[i]["resource"] == url) {
				return true;
			}
		}
	} 
	return false;
}

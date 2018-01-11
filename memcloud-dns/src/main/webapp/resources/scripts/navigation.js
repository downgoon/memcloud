/** 设置登录者个人信息（用户名）
 * @param	username 	用户名
 * @param	userlink	用户个人账号管理连接
 * @param	linktarget	可选参数
 * */
function setProfileInfo(username,userlink, linktarget) {
	var hyperlink = document.getElementById("devProfile");
	hyperlink.href = userlink;
	if(linktarget != null && linktarget != "") {
		hyperlink.target = linktarget;
	}
	hyperlink.innerHTML = username;
	
	//账户管理连接
	var accountHyperlink = document.getElementById("devAccount");
	accountHyperlink.href = userlink;
	if(linktarget != null && linktarget != "") {
		accountHyperlink.target = linktarget;
	}
}

/** 
 * 初始化登录者个人资料
 * 提取cookie信息并设置个人账号信息
 * @return	true 表示用户已经登录；false表示用户尚未登录
 * */
function initProfileInfo() {
	var uid = Get_Cookie("PPC_UID");
	var uname = Get_Cookie("PPC_NAME");
	if(uname == null || uid == null) {//尚未登录
		setProfileInfo("尚未登录","/memcloud/session-create.html","_top");
		return false;
	}
	uname = decodeURIComponent(uname);
	ulink = "/memcloud/user.html";
	setProfileInfo(uname,ulink);
	return true;
}

/** 初始化登录者权限菜单
 * @param	res	 后台返回的JSON数据
 * */
function initMenuBar(res) {
//	alert("正在加载菜单");
	/*
		<li>
			<a href="#" class="nav-top-item">用户权限管理</a>
			<ul>
				<li><a href="/payment/copyright-provider-index.html" target="workspace">添加用户</a></li>
				<li><a href="#">全部用户</a></li>
				<li><a href="#">修改资料</a></li>
			</ul>
		</li>
	 * */
//	var res = {"data":{"版权管理":[{"ctegory":"版权管理","categoryseq":30,"id":1,"name":"添加版权","nameseq":10,"resource":"/payment/copyright-provider-create.html","type":1}]},"status":200};
	var holder = document.getElementById("main-nav");
	holder.innerHTML = "";//先清除现有的菜单栏，然后重新生成
	
	var categoryMap = res["data"];
	for(var categoryName in categoryMap) {
		var itemHTML = "<li>";
		itemHTML += "<a href=\"#\" class=\"nav-top-item\">";
		itemHTML += categoryName;//分类名称（例如：用户权限管理）
		itemHTML += "</a>";
		itemHTML += "<ul>";
			var menuArray = categoryMap[categoryName];//当前分类所包含的菜单列表
			for(var i=0; i < menuArray.length; i++) {//循环菜单数组的元素 ，每个元素是一个菜单 
				if(menuArray[i]["type"] == 1) {//type==1的表示是UI菜单，否则是后台服务
					itemHTML += "<li><a href=\"" + menuArray[i]["resource"] + "\" target=\"workspace\">" + menuArray[i]["name"] + "</a></li>";
				}
			}
		itemHTML += "</ul>";
		itemHTML += "</li>";
		//生成一个菜单大类
		holder.innerHTML += itemHTML;
	}  
}

/**
 * 导航栏主要有两个操作：1、加载登录者信息（从COOKIE读取）；2、加载权限菜单。
 * */
function onNavigationPageLoad() {
	var token = Get_Cookie("PPC_TOKEN");
	if(token==null || token=="") {//用户尚未登录
		alert("尚未登录");
		return ;
	}
	
	//加载登录者信息
	initProfileInfo();
	
	//加载权限菜单
	var response = {"data":{
							"我的应用":[
							        {"ctegory":"我的应用","categoryseq":10,"id":1010,"name":"全部应用","nameseq":10,"resource":"/memcloud/app.html","type":1},
							        {"ctegory":"我的应用","categoryseq":10,"id":1020,"name":"申请应用","nameseq":20,"resource":"/memcloud/app-create.html","type":1}
							 ],
							"我的监控":[
							        {"ctegory":"我的监控","categoryseq":20,"id":2010,"name":"全部实例","nameseq":10,"resource":"/memcloud/shard.html","type":1}
							  ],
							"我的扩容":[
							        {"ctegory":"我的扩容","categoryseq":30,"id":3010,"name":"全部扩容","nameseq":10,"resource":"/memcloud/scale.html","type":1},
							        {"ctegory":"我的扩容","categoryseq":30,"id":3020,"name":"申请扩容","nameseq":20,"resource":"/memcloud/scale-create.html","type":1}
							  ]
							},
					"status":200};
	initMenuBar(response);
	PermissionStore.getInstance().setPermissions(response);//把用户的当前权限存储在[权限存储器]中
	
	
//	//构建HTTP请求参数，动态获取权限
//	var request = { "uid" :  uid,
//					"groupby" :  true
//				};
//
//	//构建Ajax请求提交表单信息
//	$.ajax({url:"/payment/permission-view.json",
//			type: "post",//POST方式提交表单内容
//			timeout : 8000,//响应超时间8秒
//			async : false,//同步提交（注：异步提交会导致样式适用不了）
//			cache : false,//禁止浏览器缓存
//			contentType : "application/x-www-form-urlencoded; charset=UTF-8",//明确告诉服务器协商编码方式，以免服务器端出现中文乱码
//			beforeSend : function(xmlHttpRequest){//请求发送前的逻辑
//				//正在加载菜单
//            },
//			dataType : "json",
//			data : request,//HTTP请求数据（表单数据）
//			success : function(response,httpStatus) {//HTTP响应成功后的处理逻辑
//				if(response["status"]==200) {
//					initMenuBar(response);
//					PermissionStore.getInstance().setPermissions(response);//把用户的当前权限存储在[权限存储器]中
//					//菜单加载成功
//				} else {
//					//菜单加载失败
//					alert("菜单加载失败");
//				}
//			}
//		});
	
}


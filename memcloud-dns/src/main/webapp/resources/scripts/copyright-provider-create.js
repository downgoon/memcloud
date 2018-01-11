/*
 * 版权提供商相关的前端逻辑，依赖于jQuery1.3.2和payment-commons.js
 * */
var Ku6PaymentCopyrightProviderLast = null;//全局变量，记录当前页面上一次提交的版权提供商名称，以提示不要重复提交表单。
/** 新增版权提供商*/
function addCopyrightProvider() {
	var name = document.getElementById("devName").value;
	if(Ku6PaymentCopyrightProviderLast!=null && Ku6PaymentCopyrightProviderLast==name) {//如果本次提交的和上次提交的一样
		showSuccInfo("您的信息已保存，请不用重复提交");
		setTimeout("hideSuccInfo(this)",2000);
		hideCopyrightNameFailNotify();
		hideCopyrightNameSuccNotify();
		hideMysidepersonFailNotify();
		hideMysidepersonSuccNotify();
		hideMysidephoneFailNotify();
		hideMysidephoneSuccNotify();
		hideNoteFailNotify();
		hideNoteSuccNotify();
		hideYearlyfeeFailNotify();
		hideYearlyfeeSuccNotify();
		hideTimelyfeeFailNotify();
		hideTimelyfeeSuccNotify();
		hideMonthlyfeeFailNotify();
		hideMonthlyfeeSuccNotify();
		top.window.scroll(0,0);
		return ;
	}
	//构建HTTP请求参数
	var request = { "name" :  name,
					"mysideperson" :  document.getElementById("devMysideperson").value,
					"mysidephone" :  document.getElementById("devMysidephone").value,
					"note" :  document.getElementById("devNote").value
				};
	
	if(document.getElementById("devHandlyflagBox").checked==true) {//手工记账
		request.handlyflag = 1;
	}else {//非手工记账
		request.handlyflag = 0;
		if(document.getElementById("devYearlyfeeBox").checked==true) {//年保底费
			request.yearlyfee = document.getElementById("devYearlyfee").value * 100;//前端以元为单位；后端以分为单位。
		} 
		if(document.getElementById("devTimelyfeeBox").checked==true) {//单次授权费
			request.timelyfee = document.getElementById("devTimelyfee").value * 100;
			if(document.getElementById("devTimelyperiod").value != '') {//如果为空，表示不限制时间上限（前端以小时为单位；后端也小时）
				request.timelyperiod = document.getElementById("devTimelyperiod").value;
			}
		} else {
			request.timelyfee = -1;//-1表示禁止单次授权
		}
		if(document.getElementById("devMonthlyfeeBox").checked==true) {//包月授权费
			request.monthlyfee = document.getElementById("devMonthlyfee").value * 100;
		} else {
			request.monthlyfee = -1;//-1表示禁止包月授权
		}
	}

	//构建Ajax请求提交表单信息
	$.ajax({url:"/payment/copyright-provider-create.json",
			type: "post",//POST方式提交表单内容
			timeout : 8000,//响应超时间8秒
			async : true,//异步提交
			cache : false,//禁止浏览器缓存
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",//明确告诉服务器协商编码方式，以免服务器端出现中文乱码
			beforeSend : function(xmlHttpRequest){//请求发送前的逻辑
				showWaitingInfo("正在保存版权提供商信息 ...");
				top.window.scroll(0,0);//回到顶部，以提高用户体验
            },
			dataType : "json",
			data : request,//HTTP请求数据（表单数据）
			success : function(response,httpStatus) {//HTTP响应成功后的处理逻辑
				if(response["status"]==200) {
					Ku6PaymentCopyrightProviderLast = name;//记录上一次提交的版权商名
					showSuccInfo("保存成功。");
					top.window.scroll(0,0);//回到顶部，以提高用户体验
					setTimeout("hideSuccInfo(this)",2000);
				} else {
					showErrorInfo("保存失败，返回码："+response["status"]+"，原因："+response["data"]);
					top.window.scroll(0,0);//回到顶部，以提高用户体验
					setTimeout("hideErrorInfo(this)",2000);
				}
			}
		});
}

function hideCopyrightNameSuccNotify() {
	document.getElementById("devNameSuccNotify").style.display ='none';
}

function hideCopyrightNameFailNotify() {
	document.getElementById("devNameFailNotify").style.display ='none';
}

function hideMysidepersonSuccNotify() {
	document.getElementById("devMysidepersonSuccNotify").style.display ='none';
}

function hideMysidepersonFailNotify() {
	document.getElementById("devMysidepersonFailNotify").style.display ='none';
}

function hideMysidephoneSuccNotify() {
	document.getElementById("devMysidephoneSuccNotify").style.display ='none';
}

function hideMysidephoneFailNotify() {
	document.getElementById("devMysidephoneFailNotify").style.display ='none';
}

/**
 * 隐藏备注提示相关信息
 */
function hideNoteSuccNotify() {
	document.getElementById("devNoteSuccNotify").style.display ='none';
}

function hideNoteFailNotify() {
	document.getElementById("devNoteFailNotify").style.display ='none';
}


function hideYearlyfeeFailNotify() {
	document.getElementById("devYearlyfeeFailNotify").style.display ='none';
}

function hideYearlyfeeSuccNotify() {
	document.getElementById("devYearlyfeeSuccNotify").style.display ='none';
}

function hideTimelyfeeFailNotify() {
	document.getElementById("devTimelyfeeFailNotify").style.display ='none';
}

function hideTimelyfeeSuccNotify() {
	document.getElementById("devTimelyfeeSuccNotify").style.display ='none';
}

function hideMonthlyfeeFailNotify() {
	document.getElementById("devMonthlyfeeFailNotify").style.display ='none';
}

function hideMonthlyfeeSuccNotify() {
	document.getElementById("devMonthlyfeeSuccNotify").style.display ='none';
}

function showMonthlyfeeSuccNotify(succinfo) {
	hideMonthlyfeeFailNotify();
	document.getElementById("devMonthlyfeeSuccNotify").innerHTML=succinfo;
	document.getElementById("devMonthlyfeeSuccNotify").style.display ='';
}

function showMonthlyfeeFailNotify(failinfo) {
	hideMonthlyfeeSuccNotify();
	document.getElementById("devMonthlyfeeFailNotify").innerHTML=failinfo;
	document.getElementById("devMonthlyfeeFailNotify").style.display ='';
}

function showTimelyfeeSuccNotify(succinfo) {
	hideTimelyfeeFailNotify();
	document.getElementById("devTimelyfeeSuccNotify").innerHTML=succinfo;
	document.getElementById("devTimelyfeeSuccNotify").style.display ='';
}

function showTimelyfeeFailNotify(failinfo) {
	hideTimelyfeeSuccNotify();
	document.getElementById("devTimelyfeeFailNotify").innerHTML=failinfo;
	document.getElementById("devTimelyfeeFailNotify").style.display ='';
}


function showYearlyfeeSuccNotify(succinfo) {
	hideYearlyfeeFailNotify();
	document.getElementById("devYearlyfeeSuccNotify").innerHTML=succinfo;
	document.getElementById("devYearlyfeeSuccNotify").style.display ='';
}

function showYearlyfeeFailNotify(failinfo) {
	hideYearlyfeeSuccNotify();
	document.getElementById("devYearlyfeeFailNotify").innerHTML=failinfo;
	document.getElementById("devYearlyfeeFailNotify").style.display ='';
}

function showCopyrightNameSuccNotify(succinfo) {
	hideCopyrightNameFailNotify();
	document.getElementById("devNameFailNotify").style.display ='none';
	document.getElementById("devNameSuccNotify").innerHTML=succinfo;
	document.getElementById("devNameSuccNotify").style.display ='';
}

function showCopyrightNameFailNotify(failinfo) {
	hideCopyrightNameSuccNotify();
	document.getElementById("devNameSuccNotify").style.display ='none';
	document.getElementById("devNameFailNotify").innerHTML=failinfo;
	document.getElementById("devNameFailNotify").style.display ='';
}

function showMysidepersonSuccNotify(succinfo) {
	hideMysidepersonFailNotify();
	document.getElementById("devMysidepersonFailNotify").style.display ='none';
	document.getElementById("devMysidepersonSuccNotify").innerHTML=succinfo;
	document.getElementById("devMysidepersonSuccNotify").style.display ='';
}

function showMysidepersonFailNotify(failinfo) {
	hideMysidepersonSuccNotify();
	document.getElementById("devMysidepersonSuccNotify").style.display ='none';
	document.getElementById("devMysidepersonFailNotify").innerHTML=failinfo;
	document.getElementById("devMysidepersonFailNotify").style.display ='';
}

/** 
 * 显示我方联系人联系方式成功信息栏
 */
function showMysidephoneSuccNotify(succinfo) {
	hideMysidephoneFailNotify();
	document.getElementById("devMysidephoneFailNotify").style.display ='none';
	document.getElementById("devMysidephoneSuccNotify").innerHTML=succinfo;
	document.getElementById("devMysidephoneSuccNotify").style.display ='';
}
/** 
 * 显示我方联系人联系方式错误信息栏
 */
function showMysidephoneFailNotify(failinfo) {
	hideMysidephoneSuccNotify();
	document.getElementById("devMysidephoneSuccNotify").style.display ='none';
	document.getElementById("devMysidephoneFailNotify").innerHTML=failinfo;
	document.getElementById("devMysidephoneFailNotify").style.display ='';
}

/**
 * 在备注提示栏显示失败/成功信息
 */
function showNoteSuccNotify(succinfo) {
	hideNoteFailNotify();
	document.getElementById("devNoteFailNotify").style.display ='none';
	document.getElementById("devNoteSuccNotify").innerHTML=succinfo;
	document.getElementById("devNoteSuccNotify").style.display ='';
}
function showNoteFailNotify(failinfo) {
	hideNoteSuccNotify();
	document.getElementById("devNoteSuccNotify").style.display ='none';
	document.getElementById("devNoteFailNotify").innerHTML=failinfo;
	document.getElementById("devNoteFailNotify").style.display ='';
}


/** Ajax校验版权提供商名称是否已被占用*/
function checkCopyrightName() {
	hideErrorInfo();
	var cpNameValue = document.getElementById("devName").value;
	if(cpNameValue==null || cpNameValue=="") {//是否为空
		showCopyrightNameFailNotify("请输入版权方名称");
		return false;
	}
//	var nameRegExp = new RegExp(/^[\u0391-\uFFE5 a-zA-Z0-9_-]{2,20}$/);
	var nameRegExp = new RegExp(/^[a-zA-Z\u0391-\uFFE5][\u0391-\uFFE5 a-zA-Z0-9_-]{1,19}$/);
	if(!nameRegExp.test(cpNameValue)) {//正则表达式失败
		showCopyrightNameFailNotify("版权方名称只允许中文字符，英文，数字，空格，连接线，下划线等字符，长度2-20");
		return false;
	}
	var pass = true;//如果Ajax响应没到，直接返回true
	//Ajax验证
	$.get("/payment/copyright-provider-checkName.json",
		{"name" : document.getElementById("devName").value,
		 "ClientCharset" : "UTF-8"	//GET方法通过查询参数协商编码
		},
		function(response,httpStatus) {
			if(response["status"]==200) {
				showCopyrightNameSuccNotify("版权商名称验证通过了");
			} else {
				showCopyrightNameFailNotify(response["data"]);
				pass = false;
			}
		},
		"json"
	);
	return pass;
}

/** 校验我方联系人 */
function checkMysideperson() {
	hideErrorInfo();
	var value = document.getElementById("devMysideperson").value;
	if(value==null || value=="") {//是否为空
		showMysidepersonFailNotify("请输入联系人真实姓名");
		return false;
	}
	var valueRegExp = new RegExp(/^[\u0391-\uFFE5]{2,10}$/);
	if(!valueRegExp.test(value)) {//正则表达式失败
		showMysidepersonFailNotify("联系人真实姓名只允许中文，长度2-10个汉字");
		return false;
	}
	showMysidepersonSuccNotify("联系人验证通过了");
	return true;
}


/** 校验我方联系方式 */
function checkMysidephone() {
	hideErrorInfo();
	var value = document.getElementById("devMysidephone").value;
	if(value==null || value=="") {//是否为空
		showMysidephoneFailNotify("请填写手机或固话号码");
		return false;
	}
	var phoneRegExp = new RegExp(/^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/);//固定电话号码格式，区号可选
	var mobileRegExp = new RegExp(/^((\(\d{2,3}\))|(\d{3}\-))?((13\d{9})|(15[389]\d{8}))$/); //手机号码 
	if(!mobileRegExp.test(value) && !phoneRegExp.test(value)) {//正则表达式失败
		showMysidephoneFailNotify("手机或固话号码不存在或格式不正确");
		return false;
	}
	showMysidephoneSuccNotify("联系方式验证通过了");
	return true;
}

/** 
 * 校验备注栏信息
 */
function checkNote() {
	hideErrorInfo();
	var value = document.getElementById("devNote").value;
	if(value==null || value=="") {//是否为空
		showNoteFailNotify("请填写备注栏信息");
		return false;
	}
	var valueRegExp = new RegExp(/^.{10,400}$/);//固定电话号码格式，区号可选
	if(!valueRegExp.test(value)) {//正则表达式失败
		showNoteFailNotify("只允许字数在10-400之间");
		return false;
	}
	showNoteSuccNotify("备注验证通过了");
	return true;
}

/**
 * 分成设置变化时
 */
function onProfitShareChange() {
	var pass = true;//分成整体是否验证通过，默认通过
	
	/* 年保底费数据验证通过*/
	if(document.getElementById("devYearlyfeeBox").checked==false) {//年保底费没勾上
		document.getElementById("devYearlyfee").value=null;
		document.getElementById("devYearlyfee").disabled=true;
	} else {//年保底费勾上了
		document.getElementById("devYearlyfee").disabled=false;
	}
	if(checkYearlyFee()) {//年保底费用验证通过
		if(document.getElementById("devYearlyfeeBox").checked == false) {
			hideYearlyfeeFailNotify();
			hideYearlyfeeSuccNotify();
		} else {
			showYearlyfeeSuccNotify("验证正确了");
		}
	} else {
		showYearlyfeeFailNotify("年保底费必须是整数（单位：元/年）");
		pass = false;
	}
	
	/*单次授权*/
	if(document.getElementById("devTimelyfeeBox").checked==false) {//单次授权没勾上
		document.getElementById("devTimelyfee").value=null;
		document.getElementById("devTimelyfee").disabled=true;
		
		document.getElementById("devTimelyperiod").value=null;
		document.getElementById("devTimelyperiod").disabled=true;
		
	} else {//单次授权勾上了
		document.getElementById("devTimelyfee").disabled=false;
		if(document.getElementById("devTimelyperiod").value == null || document.getElementById("devTimelyperiod").value == "") {
			document.getElementById("devTimelyperiod").value = 48;
		}
		document.getElementById("devTimelyperiod").disabled=false;
	}
	if(checkTimelyfee() && checkTimelyperiod()) {//单次授权通过（包括单价和时间）
		if(document.getElementById("devTimelyfeeBox").checked==false) {//单次授权没勾上
			hideTimelyfeeFailNotify();
			hideTimelyfeeSuccNotify();
		} else {//单次授权勾上了
			showTimelyfeeSuccNotify("单次授权验证通过了");
		}
		
	} else {//单次授权验证未通过
		var timelyNotify = "";
		if(!checkTimelyfee()) {
			timelyNotify += "单次授权单价必须为整数，单位：元/人/次；";
		}
		if(!checkTimelyperiod()) {
			timelyNotify += "单次授权上限时间必须为整数，单位：小时。"
		}
		showTimelyfeeFailNotify(timelyNotify);
		pass = false;
	}
	
	/* 包月授权*/
	if(document.getElementById("devMonthlyfeeBox").checked==false) {//包月授权没勾上
		document.getElementById("devMonthlyfee").value=null;
		document.getElementById("devMonthlyfee").disabled=true;
		
	} else {//包月授权勾上了
		document.getElementById("devMonthlyfee").disabled=false;
	}
	if(checkMonthlyfee()) {//包月验证通过
		if(document.getElementById("devMonthlyfeeBox").checked==false) {
			hideMonthlyfeeFailNotify();
			hideMonthlyfeeSuccNotify();
		} else {
			showMonthlyfeeSuccNotify("包月授权验证通过了");
		}
	} else {//包月验证未通过
		showMonthlyfeeFailNotify("包月授权费用必须是整数，单位：元/人/月");
		pass = false;
	}
	
	/* 手工记帐互斥控制：{手工记账} 覆盖互斥 {年保底费，包月授权，单次授权}*/
	//单次，包月，或年保底授权勾上了，那么手工记账就不能勾了；反之依然。
	if(document.getElementById("devTimelyfeeBox").checked 
			|| document.getElementById("devMonthlyfeeBox").checked
			|| document.getElementById("devYearlyfeeBox").checked) {

		document.getElementById("devHandlyflagBox").checked=false;
		document.getElementById("devHandlyflagBox").disabled=true;
	} else {//如果单次，包月，年保底三都没没勾，则手工记账可勾
		document.getElementById("devHandlyflagBox").disabled=false;
	}
	//手工记账勾上了，那么包月，单次，年保底费就都不能勾了
	if(document.getElementById("devHandlyflagBox").checked)	{
		document.getElementById("devTimelyfeeBox").checked=false;//单次
		document.getElementById("devTimelyfeeBox").disabled=true;
		
		document.getElementById("devMonthlyfeeBox").checked=false;//包月
		document.getElementById("devMonthlyfeeBox").disabled=true;
		
		document.getElementById("devYearlyfeeBox").checked=false;//年保底费
		document.getElementById("devYearlyfeeBox").disabled=true;
	} else {
		document.getElementById("devTimelyfeeBox").disabled=false;
		document.getElementById("devMonthlyfeeBox").disabled=false;
		document.getElementById("devYearlyfeeBox").disabled=false;
	}
	
	/* 确认是否至少选择一种计费方式*/
	if(document.getElementById("devMonthlyfeeBox").checked 
			|| document.getElementById("devTimelyfeeBox").checked
			|| document.getElementById("devYearlyfeeBox").checked
			|| document.getElementById("devHandlyflagBox").checked) {
		document.getElementById("devBillingWayNotify").innerHTML = "";
		document.getElementById("devBillingWayNotify").style.display ='none';
		pass = true;
	} else {
		document.getElementById("devBillingWayNotify").innerHTML = "请至少选择一种计费方式：{年保底费，单次授权，包月授权}或{手工记账}";
		document.getElementById("devBillingWayNotify").style.display ='';
		pass = false;
	}
	return pass;
}

function checkYearlyFee() {
	if (document.getElementById("devYearlyfeeBox").checked == false) {//年保底费没勾上
		return true;
	} else {//年保底费勾上了
		var yearlyFeeReg = new RegExp(/^[+]?\d+$/);//integer (/^[-\+]?\d+$/)
		return yearlyFeeReg.test(document.getElementById("devYearlyfee").value);
	}
}
/** 单次授权单价验证*/
function checkTimelyfee() {
	if (document.getElementById("devTimelyfeeBox").checked == false) {//单次授权没勾上
		return true;
	} else {//单次授权勾上了
		var yearlyFeeReg = new RegExp(/^[+]?\d+$/);//integer
		return yearlyFeeReg.test(document.getElementById("devTimelyfee").value);
	}
}

/** 单次授权时间上限验证*/
function checkTimelyperiod() {
	if (document.getElementById("devTimelyfeeBox").checked == false) {//单次授权没勾上
		return true;
	} else {//单次授权勾上了
		var yearlyFeeReg = new RegExp(/^[+]?\d+$/);//integer
		return yearlyFeeReg.test(document.getElementById("devTimelyperiod").value);
	}
}

/** 包月费用验证*/
function checkMonthlyfee() {
	if (document.getElementById("devMonthlyfeeBox").checked == false) {//包月授权没勾上
		return true;
	} else {//包月授权勾上了
		var yearlyFeeReg = new RegExp(/^[+]?\d+$/);//integer
		return yearlyFeeReg.test(document.getElementById("devMonthlyfee").value);
	}
}
/** 确认是否至少选择一种计费方式*/
function checkBillingWaySpecified() {
	document.getElementById("devBillingWayNotify").style.display ='none';
	if(document.getElementById("devMonthlyfeeBox").checked || document.getElementById("devTimelyfeeBox").checked
			|| document.getElementById("devHandlyflagBox").checked) {
		return true;
	} else {
		document.getElementById("devBillingWayNotify").innerHTML = "请至少选择一种计费方式：{年保底费，单次授权，包月授权}或{手工记账}";
		document.getElementById("devBillingWayNotify").style.display ='';
		return false;
	}
}


/** 表单提交时触发*/
function onFormSubmit() {
	var pass = true;
	pass = checkCopyrightName() && pass;
	pass = checkMysideperson() && pass;
	pass = checkMysidephone() && pass;
	pass = onProfitShareChange() && pass;
	pass = checkNote() && pass;
	if(pass) {//数据验证通过，则提交表单
		addCopyrightProvider();
	} else {
		//数据校验未通过，表单不提交
	}
}


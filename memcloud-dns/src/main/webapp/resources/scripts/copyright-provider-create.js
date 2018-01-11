/*
 * ��Ȩ�ṩ����ص�ǰ���߼���������jQuery1.3.2��payment-commons.js
 * */
var Ku6PaymentCopyrightProviderLast = null;//ȫ�ֱ�������¼��ǰҳ����һ���ύ�İ�Ȩ�ṩ�����ƣ�����ʾ��Ҫ�ظ��ύ����
/** ������Ȩ�ṩ��*/
function addCopyrightProvider() {
	var name = document.getElementById("devName").value;
	if(Ku6PaymentCopyrightProviderLast!=null && Ku6PaymentCopyrightProviderLast==name) {//��������ύ�ĺ��ϴ��ύ��һ��
		showSuccInfo("������Ϣ�ѱ��棬�벻���ظ��ύ");
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
	//����HTTP�������
	var request = { "name" :  name,
					"mysideperson" :  document.getElementById("devMysideperson").value,
					"mysidephone" :  document.getElementById("devMysidephone").value,
					"note" :  document.getElementById("devNote").value
				};
	
	if(document.getElementById("devHandlyflagBox").checked==true) {//�ֹ�����
		request.handlyflag = 1;
	}else {//���ֹ�����
		request.handlyflag = 0;
		if(document.getElementById("devYearlyfeeBox").checked==true) {//�걣�׷�
			request.yearlyfee = document.getElementById("devYearlyfee").value * 100;//ǰ����ԪΪ��λ������Է�Ϊ��λ��
		} 
		if(document.getElementById("devTimelyfeeBox").checked==true) {//������Ȩ��
			request.timelyfee = document.getElementById("devTimelyfee").value * 100;
			if(document.getElementById("devTimelyperiod").value != '') {//���Ϊ�գ���ʾ������ʱ�����ޣ�ǰ����СʱΪ��λ�����ҲСʱ��
				request.timelyperiod = document.getElementById("devTimelyperiod").value;
			}
		} else {
			request.timelyfee = -1;//-1��ʾ��ֹ������Ȩ
		}
		if(document.getElementById("devMonthlyfeeBox").checked==true) {//������Ȩ��
			request.monthlyfee = document.getElementById("devMonthlyfee").value * 100;
		} else {
			request.monthlyfee = -1;//-1��ʾ��ֹ������Ȩ
		}
	}

	//����Ajax�����ύ����Ϣ
	$.ajax({url:"/payment/copyright-provider-create.json",
			type: "post",//POST��ʽ�ύ������
			timeout : 8000,//��Ӧ��ʱ��8��
			async : true,//�첽�ύ
			cache : false,//��ֹ���������
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",//��ȷ���߷�����Э�̱��뷽ʽ������������˳�����������
			beforeSend : function(xmlHttpRequest){//������ǰ���߼�
				showWaitingInfo("���ڱ����Ȩ�ṩ����Ϣ ...");
				top.window.scroll(0,0);//�ص�������������û�����
            },
			dataType : "json",
			data : request,//HTTP�������ݣ������ݣ�
			success : function(response,httpStatus) {//HTTP��Ӧ�ɹ���Ĵ����߼�
				if(response["status"]==200) {
					Ku6PaymentCopyrightProviderLast = name;//��¼��һ���ύ�İ�Ȩ����
					showSuccInfo("����ɹ���");
					top.window.scroll(0,0);//�ص�������������û�����
					setTimeout("hideSuccInfo(this)",2000);
				} else {
					showErrorInfo("����ʧ�ܣ������룺"+response["status"]+"��ԭ��"+response["data"]);
					top.window.scroll(0,0);//�ص�������������û�����
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
 * ���ر�ע��ʾ�����Ϣ
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
 * ��ʾ�ҷ���ϵ����ϵ��ʽ�ɹ���Ϣ��
 */
function showMysidephoneSuccNotify(succinfo) {
	hideMysidephoneFailNotify();
	document.getElementById("devMysidephoneFailNotify").style.display ='none';
	document.getElementById("devMysidephoneSuccNotify").innerHTML=succinfo;
	document.getElementById("devMysidephoneSuccNotify").style.display ='';
}
/** 
 * ��ʾ�ҷ���ϵ����ϵ��ʽ������Ϣ��
 */
function showMysidephoneFailNotify(failinfo) {
	hideMysidephoneSuccNotify();
	document.getElementById("devMysidephoneSuccNotify").style.display ='none';
	document.getElementById("devMysidephoneFailNotify").innerHTML=failinfo;
	document.getElementById("devMysidephoneFailNotify").style.display ='';
}

/**
 * �ڱ�ע��ʾ����ʾʧ��/�ɹ���Ϣ
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


/** AjaxУ���Ȩ�ṩ�������Ƿ��ѱ�ռ��*/
function checkCopyrightName() {
	hideErrorInfo();
	var cpNameValue = document.getElementById("devName").value;
	if(cpNameValue==null || cpNameValue=="") {//�Ƿ�Ϊ��
		showCopyrightNameFailNotify("�������Ȩ������");
		return false;
	}
//	var nameRegExp = new RegExp(/^[\u0391-\uFFE5 a-zA-Z0-9_-]{2,20}$/);
	var nameRegExp = new RegExp(/^[a-zA-Z\u0391-\uFFE5][\u0391-\uFFE5 a-zA-Z0-9_-]{1,19}$/);
	if(!nameRegExp.test(cpNameValue)) {//������ʽʧ��
		showCopyrightNameFailNotify("��Ȩ������ֻ���������ַ���Ӣ�ģ����֣��ո������ߣ��»��ߵ��ַ�������2-20");
		return false;
	}
	var pass = true;//���Ajax��Ӧû����ֱ�ӷ���true
	//Ajax��֤
	$.get("/payment/copyright-provider-checkName.json",
		{"name" : document.getElementById("devName").value,
		 "ClientCharset" : "UTF-8"	//GET����ͨ����ѯ����Э�̱���
		},
		function(response,httpStatus) {
			if(response["status"]==200) {
				showCopyrightNameSuccNotify("��Ȩ��������֤ͨ����");
			} else {
				showCopyrightNameFailNotify(response["data"]);
				pass = false;
			}
		},
		"json"
	);
	return pass;
}

/** У���ҷ���ϵ�� */
function checkMysideperson() {
	hideErrorInfo();
	var value = document.getElementById("devMysideperson").value;
	if(value==null || value=="") {//�Ƿ�Ϊ��
		showMysidepersonFailNotify("��������ϵ����ʵ����");
		return false;
	}
	var valueRegExp = new RegExp(/^[\u0391-\uFFE5]{2,10}$/);
	if(!valueRegExp.test(value)) {//������ʽʧ��
		showMysidepersonFailNotify("��ϵ����ʵ����ֻ�������ģ�����2-10������");
		return false;
	}
	showMysidepersonSuccNotify("��ϵ����֤ͨ����");
	return true;
}


/** У���ҷ���ϵ��ʽ */
function checkMysidephone() {
	hideErrorInfo();
	var value = document.getElementById("devMysidephone").value;
	if(value==null || value=="") {//�Ƿ�Ϊ��
		showMysidephoneFailNotify("����д�ֻ���̻�����");
		return false;
	}
	var phoneRegExp = new RegExp(/^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/);//�̶��绰�����ʽ�����ſ�ѡ
	var mobileRegExp = new RegExp(/^((\(\d{2,3}\))|(\d{3}\-))?((13\d{9})|(15[389]\d{8}))$/); //�ֻ����� 
	if(!mobileRegExp.test(value) && !phoneRegExp.test(value)) {//������ʽʧ��
		showMysidephoneFailNotify("�ֻ���̻����벻���ڻ��ʽ����ȷ");
		return false;
	}
	showMysidephoneSuccNotify("��ϵ��ʽ��֤ͨ����");
	return true;
}

/** 
 * У�鱸ע����Ϣ
 */
function checkNote() {
	hideErrorInfo();
	var value = document.getElementById("devNote").value;
	if(value==null || value=="") {//�Ƿ�Ϊ��
		showNoteFailNotify("����д��ע����Ϣ");
		return false;
	}
	var valueRegExp = new RegExp(/^.{10,400}$/);//�̶��绰�����ʽ�����ſ�ѡ
	if(!valueRegExp.test(value)) {//������ʽʧ��
		showNoteFailNotify("ֻ����������10-400֮��");
		return false;
	}
	showNoteSuccNotify("��ע��֤ͨ����");
	return true;
}

/**
 * �ֳ����ñ仯ʱ
 */
function onProfitShareChange() {
	var pass = true;//�ֳ������Ƿ���֤ͨ����Ĭ��ͨ��
	
	/* �걣�׷�������֤ͨ��*/
	if(document.getElementById("devYearlyfeeBox").checked==false) {//�걣�׷�û����
		document.getElementById("devYearlyfee").value=null;
		document.getElementById("devYearlyfee").disabled=true;
	} else {//�걣�׷ѹ�����
		document.getElementById("devYearlyfee").disabled=false;
	}
	if(checkYearlyFee()) {//�걣�׷�����֤ͨ��
		if(document.getElementById("devYearlyfeeBox").checked == false) {
			hideYearlyfeeFailNotify();
			hideYearlyfeeSuccNotify();
		} else {
			showYearlyfeeSuccNotify("��֤��ȷ��");
		}
	} else {
		showYearlyfeeFailNotify("�걣�׷ѱ�������������λ��Ԫ/�꣩");
		pass = false;
	}
	
	/*������Ȩ*/
	if(document.getElementById("devTimelyfeeBox").checked==false) {//������Ȩû����
		document.getElementById("devTimelyfee").value=null;
		document.getElementById("devTimelyfee").disabled=true;
		
		document.getElementById("devTimelyperiod").value=null;
		document.getElementById("devTimelyperiod").disabled=true;
		
	} else {//������Ȩ������
		document.getElementById("devTimelyfee").disabled=false;
		if(document.getElementById("devTimelyperiod").value == null || document.getElementById("devTimelyperiod").value == "") {
			document.getElementById("devTimelyperiod").value = 48;
		}
		document.getElementById("devTimelyperiod").disabled=false;
	}
	if(checkTimelyfee() && checkTimelyperiod()) {//������Ȩͨ�����������ۺ�ʱ�䣩
		if(document.getElementById("devTimelyfeeBox").checked==false) {//������Ȩû����
			hideTimelyfeeFailNotify();
			hideTimelyfeeSuccNotify();
		} else {//������Ȩ������
			showTimelyfeeSuccNotify("������Ȩ��֤ͨ����");
		}
		
	} else {//������Ȩ��֤δͨ��
		var timelyNotify = "";
		if(!checkTimelyfee()) {
			timelyNotify += "������Ȩ���۱���Ϊ��������λ��Ԫ/��/�Σ�";
		}
		if(!checkTimelyperiod()) {
			timelyNotify += "������Ȩ����ʱ�����Ϊ��������λ��Сʱ��"
		}
		showTimelyfeeFailNotify(timelyNotify);
		pass = false;
	}
	
	/* ������Ȩ*/
	if(document.getElementById("devMonthlyfeeBox").checked==false) {//������Ȩû����
		document.getElementById("devMonthlyfee").value=null;
		document.getElementById("devMonthlyfee").disabled=true;
		
	} else {//������Ȩ������
		document.getElementById("devMonthlyfee").disabled=false;
	}
	if(checkMonthlyfee()) {//������֤ͨ��
		if(document.getElementById("devMonthlyfeeBox").checked==false) {
			hideMonthlyfeeFailNotify();
			hideMonthlyfeeSuccNotify();
		} else {
			showMonthlyfeeSuccNotify("������Ȩ��֤ͨ����");
		}
	} else {//������֤δͨ��
		showMonthlyfeeFailNotify("������Ȩ���ñ�������������λ��Ԫ/��/��");
		pass = false;
	}
	
	/* �ֹ����ʻ�����ƣ�{�ֹ�����} ���ǻ��� {�걣�׷ѣ�������Ȩ��������Ȩ}*/
	//���Σ����£����걣����Ȩ�����ˣ���ô�ֹ����˾Ͳ��ܹ��ˣ���֮��Ȼ��
	if(document.getElementById("devTimelyfeeBox").checked 
			|| document.getElementById("devMonthlyfeeBox").checked
			|| document.getElementById("devYearlyfeeBox").checked) {

		document.getElementById("devHandlyflagBox").checked=false;
		document.getElementById("devHandlyflagBox").disabled=true;
	} else {//������Σ����£��걣������ûû�������ֹ����˿ɹ�
		document.getElementById("devHandlyflagBox").disabled=false;
	}
	//�ֹ����˹����ˣ���ô���£����Σ��걣�׷ѾͶ����ܹ���
	if(document.getElementById("devHandlyflagBox").checked)	{
		document.getElementById("devTimelyfeeBox").checked=false;//����
		document.getElementById("devTimelyfeeBox").disabled=true;
		
		document.getElementById("devMonthlyfeeBox").checked=false;//����
		document.getElementById("devMonthlyfeeBox").disabled=true;
		
		document.getElementById("devYearlyfeeBox").checked=false;//�걣�׷�
		document.getElementById("devYearlyfeeBox").disabled=true;
	} else {
		document.getElementById("devTimelyfeeBox").disabled=false;
		document.getElementById("devMonthlyfeeBox").disabled=false;
		document.getElementById("devYearlyfeeBox").disabled=false;
	}
	
	/* ȷ���Ƿ�����ѡ��һ�ּƷѷ�ʽ*/
	if(document.getElementById("devMonthlyfeeBox").checked 
			|| document.getElementById("devTimelyfeeBox").checked
			|| document.getElementById("devYearlyfeeBox").checked
			|| document.getElementById("devHandlyflagBox").checked) {
		document.getElementById("devBillingWayNotify").innerHTML = "";
		document.getElementById("devBillingWayNotify").style.display ='none';
		pass = true;
	} else {
		document.getElementById("devBillingWayNotify").innerHTML = "������ѡ��һ�ּƷѷ�ʽ��{�걣�׷ѣ�������Ȩ��������Ȩ}��{�ֹ�����}";
		document.getElementById("devBillingWayNotify").style.display ='';
		pass = false;
	}
	return pass;
}

function checkYearlyFee() {
	if (document.getElementById("devYearlyfeeBox").checked == false) {//�걣�׷�û����
		return true;
	} else {//�걣�׷ѹ�����
		var yearlyFeeReg = new RegExp(/^[+]?\d+$/);//integer (/^[-\+]?\d+$/)
		return yearlyFeeReg.test(document.getElementById("devYearlyfee").value);
	}
}
/** ������Ȩ������֤*/
function checkTimelyfee() {
	if (document.getElementById("devTimelyfeeBox").checked == false) {//������Ȩû����
		return true;
	} else {//������Ȩ������
		var yearlyFeeReg = new RegExp(/^[+]?\d+$/);//integer
		return yearlyFeeReg.test(document.getElementById("devTimelyfee").value);
	}
}

/** ������Ȩʱ��������֤*/
function checkTimelyperiod() {
	if (document.getElementById("devTimelyfeeBox").checked == false) {//������Ȩû����
		return true;
	} else {//������Ȩ������
		var yearlyFeeReg = new RegExp(/^[+]?\d+$/);//integer
		return yearlyFeeReg.test(document.getElementById("devTimelyperiod").value);
	}
}

/** ���·�����֤*/
function checkMonthlyfee() {
	if (document.getElementById("devMonthlyfeeBox").checked == false) {//������Ȩû����
		return true;
	} else {//������Ȩ������
		var yearlyFeeReg = new RegExp(/^[+]?\d+$/);//integer
		return yearlyFeeReg.test(document.getElementById("devMonthlyfee").value);
	}
}
/** ȷ���Ƿ�����ѡ��һ�ּƷѷ�ʽ*/
function checkBillingWaySpecified() {
	document.getElementById("devBillingWayNotify").style.display ='none';
	if(document.getElementById("devMonthlyfeeBox").checked || document.getElementById("devTimelyfeeBox").checked
			|| document.getElementById("devHandlyflagBox").checked) {
		return true;
	} else {
		document.getElementById("devBillingWayNotify").innerHTML = "������ѡ��һ�ּƷѷ�ʽ��{�걣�׷ѣ�������Ȩ��������Ȩ}��{�ֹ�����}";
		document.getElementById("devBillingWayNotify").style.display ='';
		return false;
	}
}


/** ���ύʱ����*/
function onFormSubmit() {
	var pass = true;
	pass = checkCopyrightName() && pass;
	pass = checkMysideperson() && pass;
	pass = checkMysidephone() && pass;
	pass = onProfitShareChange() && pass;
	pass = checkNote() && pass;
	if(pass) {//������֤ͨ�������ύ��
		addCopyrightProvider();
	} else {
		//����У��δͨ���������ύ
	}
}


<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.github.downgoon.jresty.rest.model.UnifiedResponse,com.github.downgoon.jresty.rest.model.UnifiedResponseCode" %>
<%@ include file="../conf.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%
	UnifiedResponse  up = (UnifiedResponse) request.getAttribute("model");
	if(up == null) {
		up = new UnifiedResponse();
		up.setStatus(UnifiedResponseCode.RC_ERROR);
		up.setMessage("系统运行异常");
	} else {
		up.setMessage("JSP异常："+ up.getMsg());
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta name="robots" content="all" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>异常提示</title>
</head>
<body>
<div id="footArea">页尾</div>

<!--Attachment for debug: 异常栈信息-->
<br /><br /><br />
<div id="debug_exception_stack" style="display: none;">
	<s:debug></s:debug>
</div>

</body>
</html>

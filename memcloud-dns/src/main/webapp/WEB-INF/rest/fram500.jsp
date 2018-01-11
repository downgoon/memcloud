<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="io.downgoon.passport.model.UnifiedResponse,io.downgoon.passport.model.UnifiedResponseCode" %>
<%@ include file="../conf.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%
	UnifiedResponse  up = (UnifiedResponse)request.getAttribute("model");
	if(up == null) {
		up = new UnifiedResponse();
		up.setStatus(UnifiedResponseCode.RC_ERROR);
		up.setMessage("系统运行异常");
		up.setDebug("jsp页面中提取model响应数据失败");
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<meta name="robots" content="All" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<link href="<%=cssRoot%>style.css" type="text/css" rel="stylesheet" />
</head>
<body>
    <div class="error"><a href="#">
      <h3>调试信息隐藏：<%=up.getMessage()%></h3>
      <p style="display:none;">调试信息：<%=up.getDebug()%></p>
      </a></div>
  </div>
</div>
</body>
</html>

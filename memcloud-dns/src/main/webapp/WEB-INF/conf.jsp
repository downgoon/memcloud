<%
	boolean devMode = true; 
	
	String jsRootVersion = "201101131828"; 
	String jsRoot = "http://js.example.com/comm/plus/" + jsRootVersion + "/";
	
	String cssRootVersion = "201101131832";
	String cssRoot = "http://css.example.com/comm/plus/" + cssRootVersion + "/";
	
	String imageRoot = "http://css.example.com/comm/plus/" + cssRootVersion + "/image/";	
	
	
	if(devMode) {
		jsRoot = "/resources/js/" ;
		cssRoot = "/resources/css/";
		imageRoot = "/resources/image/";		
	}
%>

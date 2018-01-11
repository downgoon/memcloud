function getFlashObj(movieName)
{
	return document[movieName] || window[movieName]||document.getElementById(movieName)||document.getElementsByName(movieName)[0];
}
var dataURLObject={};
function getDataUrl(_objectName,_dataUrl){
	if(!_dataUrl){
		return dataURLObject[_objectName];
	}else{
		dataURLObject[_objectName]=_dataUrl;
		var flashObject =getFlashObj(_objectName);
		if(flashObject)flashObject.AddVideo();
	}
}

function showIndexFlash(width,height,flashSRC,flashObjectName,vars,flashContainerID,dataurl){
		getDataUrl(flashObjectName,dataurl);
            //flash参数设置部分 开始
		var _ivWidth=width;//自定义宽 
		var _ivHeight=height;//自定义高
		var _flashSRC=flashSRC;//flash文件路径
		var _vars="FlashObjectName="+flashObjectName+"&"+vars;//
		var _flashContainerID=flashContainerID;//显示flash内容的DIV id
		var _flashObjectName=flashObjectName;
            
            /////////////////// 结束
            
        //<!-- For version detection, set to min. required Flash Player version, or 0 (or 0.0.0), for no version detection. --> 
        var swfVersionStr = "10.0.2";
        //<!-- To use express install, set to playerProductInstall.swf, otherwise the empty string. -->
        var xiSwfUrlStr = "http://www.adobe.com/go/getflashplayer";
        var flashvars = {};
        var params = {};
        params.quality = "high";
        //params.bgcolor = "#ffffff";
        params.salign = "lt";
        params.FlashVars = _vars;
        params.wmode = "Window";//"Transparent";
        params.devicefont = "false";
        params.allowscriptaccess = "always";
        params.allowfullscreen = "true";
		params.play = "true";
		params.loop = "true";
        var attributes = {};
        attributes.id = _flashObjectName;
        attributes.name = _flashObjectName;
        attributes.align = "middle";
        swfobject.embedSWF(
            _flashSRC, _flashContainerID, 
            _ivWidth, _ivHeight, 
            swfVersionStr, xiSwfUrlStr, 
            flashvars, params, attributes);
		//<!-- JavaScript enabled so display the flashContent div in case it is not replaced with a swf object. -->
		swfobject.createCSS("#flashContent", "display:block;text-align:left;");
       
}
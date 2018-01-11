function changePage(current,count){
	//trvideo
	for(i=1;i<=count;i++){
		para = "#trvideo" + i;
		var page = parseInt((i-1)/4 +1);
		if(page==current){
			jQuery(para)[0].style.display="block";
		}else{
			jQuery(para)[0].style.display="none";
		}
	}
}

function showSort(id){
	jQuery("#sortProv").hide();
	jQuery("#sortCity").hide();
	var idname = "#" + id;
	jQuery(idname).show();
}


function changeFoucs(obj){
		var flag = obj.attr("id");
		obj.addClass("on");
		obj.siblings("a").each(function(i){
			jQuery(this).removeClass();
		});
		obj.parent().parent().siblings().each(function(i){
			if(jQuery(this).attr("id") != ""){
				if(flag.indexOf(jQuery(this).attr("id"))>0){
					jQuery(this).css("display","block");
				}else{
					jQuery(this).css("display","none");
				}
			}
		});
}

function hideDiv(div){
	var id="#" + div;
	jQuery(id).css("display","none");
}
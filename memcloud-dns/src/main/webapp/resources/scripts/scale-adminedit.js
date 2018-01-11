/** 管理员点击审批按钮：同意或驳回 */
function scaleoutOnVerify(radioObj) {
	if (radioObj.value == 'reject' || radioObj.value == 'accept') {
		document.getElementById("btnSubmit").style.display = "block";
		if (radioObj.value == 'accept') {
			document.getElementById("pShard").style.display = "block";
			document.getElementById("pMem").style.display = "block";
		} else {
			document.getElementById("pShard").style.display = "none";
			document.getElementById("pMem").style.display = "none";
			document.getElementById("shard").value = document.getElementById("applyShard").value;
			document.getElementById("mem").value = document.getElementById("applyMem").value;
		}
	}
	
}
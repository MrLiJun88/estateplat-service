function sign(proid,signkey,taskid,signOpinionKey,signNoOptinion) {
	var opinion=$("#"+signOpinionKey).val();
	addNewSign(proid,'',signkey,'',opinion,taskid,signOpinionKey,signNoOptinion);
}

function addNewSign(proid, signid, signkey, opintype,opinion,taskid,signOpinionKey,signNoOptinion) {
var url=window.location.href;

if(url!=null && url.indexOf("&disable=true")<0){
	var platformUrl = '/platform/author.action';
	var urlVars = getUrlVars() ;
	var param = {from:urlVars.from,taskid:urlVars.taskid,proid:urlVars.proid,wiid:urlVars.wiid,rid:urlVars.rid};
	$.post(platformUrl,param,function(data){
		if(data!=null&&data!=''){
			data = $.parseJSON(data);
			for(var i=0;i<data.length;i++){
				var elementName = data[i].elementName;
				if(elementName!=signkey)
					continue;
				var operateType = data[i].operateType;
				if(operateType>0){
					return ;
				}
			}
		}
		if (!signid || signid == null || signid == 'null' || signid == '') {
			createSign(proid, signkey, opintype,opinion,taskid,signOpinionKey,signNoOptinion);
		} else {
			editSign(proid, signid, signkey, opintype,opinion,taskid,signOpinionKey,signNoOptinion);
		}
	});
}
}

function createSign(proid, signkey, opintype,opinion,taskid,signOpinionKey,signNoOptinion) {
	var url=window.location.href;

if(url!=null && url.indexOf("&disable=true")<0){
	opintype = encodeURI(opintype);
	signkey = encodeURI(encodeURI(signkey));
	opinion = encodeURI(opinion);
	var paramString = "signVo.signId=&signVo.signKey=" + signkey+ "&signVo.proId=" + proid + "&opinionType=" + opintype+ "&signVo.signOpinion=" + opinion+ "&taskid=" + taskid+"&signNoOptinion="+signNoOptinion;
	var signUrl = "/estateplat-server/sign?"+paramString;
	var height="540px";
	if(signNoOptinion=="true")
		height="340px";
	var returnvalue = window.showModalDialog(signUrl, null,"dialogWidth:480px;dialogHeight:"+height+"px;help:no;status:no;scroll:no;");

	if (returnvalue!=null) {
		if(returnvalue!="del"){
			if(signOpinionKey!=null && signOpinionKey!=""){
				$("#"+signOpinionKey).val(returnvalue);
			}
			window.location.reload();
		}else
			window.location.reload();
	}
}
}

function editSign(proid, signid, signkey, opintype,opinion,taskid,signOpinionKey,signNoOptinion) {
	var url=window.location.href;

if(url!=null && url.indexOf("&disable=true")<0){
	if (!signid || signid == null || signid == 'null' || signid == '')
		return;
	opintype = encodeURI(opintype);
	signkey = encodeURI(encodeURI(signkey));
	opinion = encodeURI(opinion);
	// var write = contentPane.curLGP.write;
	var paramString = "signVo.signId=" + signid+ "&signVo.signKey=" + signkey+ "&signVo.proId=" + proid + "&opinionType=" + opintype+ "&signVo.signOpinion=" + opinion+ "&taskid=" + taskid+"&signNoOptinion="+signNoOptinion;
	var signUrl = "/estateplat-server/sign?"+paramString;
	var height="540px";
	if(signNoOptinion=="true")
		height="340px";
	var width="480px"
	var returnvalue=window.showModalDialog(signUrl, null, "dialogHeight="+height+";dialogWidth= "+width+";help:no;status:no;scroll:no;");
	if (returnvalue!=null) {
		if(returnvalue!="del"){
			$("#"+signOpinionKey).val(returnvalue);
			window.location.reload();
		}else
			window.location.reload();
	}
}
}

function getUrlVars()  {
	var vars = [], hash;
	var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
	for(var i = 0; i < hashes.length; i++)
	{
		hash = hashes[i].split('=');
		vars.push(hash[0]);
		vars[hash[0]] = hash[1];
	}
	return vars;
}

function initSign(proid) {
	var url="/estateplat-server/sign?"
		$.getJSON(
			url,
			null,
			function (result) {
				if (result)
					setLimitfieldColor(form, result);
			}
		);
}

function setLimitfieldColor(form, result) {
	var data = result.data;
	for (var i = 0; i < data.length; i++) {
		var elementId = "#" + data[i].cptFieldName;
		$("#" + form).contents().find(elementId).each(function () {
			$(this).css("background-color", result.color);
		});
	}
}
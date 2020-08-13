/**
 * 身份证
 */
function ClearIDCard() {
    CVR_IDCard.Name="";
    CVR_IDCard.NameL="";
    CVR_IDCard.Sex="";
    //CVR_IDCard.SexL="";
    CVR_IDCard.Nation="";
    //CVR_IDCard.NationL="";
    CVR_IDCard.Born="";
    //CVR_IDCard.BornL="";
    CVR_IDCard.Address="";
    CVR_IDCard.CardNo="";
    CVR_IDCard.Police="";
    CVR_IDCard.Activity="";
    CVR_IDCard.NewAddr="";

    return true;
}

function ReadIDCardNew(name_input){
    var CVR_IDCard=document.getElementById("CVR_IDCard");
    if(CVR_IDCard==undefined || CVR_IDCard==null){
        try{
            $("body").append('<OBJECT classid="clsid:10946843-7507-44FE-ACE8-2B3483D179B7"	  codebase="CVR100.cab#version=3,0,3,3"	  id="CVR_IDCard"           name="CVR_IDCard"	  width=0	  height=0	  align=center	  hspace=0	  vspace=0> </OBJECT>');

        }catch(e){

        }
    }
    ClearIDCard();
    ReadIDCard(name_input);
}
function ReadIDCard(name_input) {
    var objCard = new ActiveXObject("GtMap.GxFrameActiveX.IDCard.IDCard");
    if (objCard.ReadCard()) {
        var pName = objCard.Name;
        var pCardNo = objCard.ID;
        var pSex = objCard.Sex;
        var pNation = objCard.Native;
        var pBorn = objCard.BirthDate;
        var pAddress = objCard.Address;
        var pPolice = objCard.Organ;
        var pActivity = objCard.End;
        var pActivityLFrom = objCard.Begin;
        var pActivityLTo = objCard.End;
        var base64ofPicture = objCard.base64ofPicture;
    }else{
        alert("请检查身份证读卡器是否安装成功！");
        return;
    }
    if (pName == null || pName == "") {
        alert("读取身份证失败，请拿起身份证重新放置！");
        return true;
    }
    $("#" +name_input).val(trimStr(pName));
    return true;
}


function DoStopRead() {
    CVR_IDCard.DoStopRead;
    return true;
}
//检测设备state 0 为正常 -1不正常
function DoCheckReader() {
    var State=CVR_IDCard.GetState;
    return State;
    // return true;
}
//
function trimStr(str){return str.replace(/(^\s*)|(\s*$)/g,"");}
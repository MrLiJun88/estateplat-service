function createXmxx(xmProid){
//        alert($.cookie('lpb_cookie'));
    //        djsjEditXm
    var ljzFlag="";
    var passCheck="0";
    var ljzPassCheck="0";
    var bdcXmRelList="";
    var dcbIndexs=new Array();
    var bdcdyhs = new Array();
    var djIds=new Array();
    var zjgcdyFw = "1";
    debugger;
    if($.cookie('lpb_cookie')!=null){

        var jsonStr=JSON.parse($.cookie('lpb_cookie'));
        $.each(jsonStr,function(idx,item){
            var fwmc= idx.substring(0,idx.indexOf("("));
            var zdzhh= idx.substring(idx.indexOf("(")+1,idx.indexOf(")"));
            //                alert(item.ljz);
            //                alert(item.value);
            //                alert(item.property);

            if(item.ljz=="true"){
                ljzFlag="true";
                //                    bdcXmRelList+="&bdcXmRelList["+i+"].qjid="+zdzhh;
                dcbIndexs.push(item.value);
            }
            else{
                var info=item.property;
                for(var i=0;i<info.length;i++){
                    djIds.push(info[i].substring(0,info[i].indexOf("$")));
                    bdcdyhs.push(info[i].substring(info[i].indexOf("$")+1))
                }

//                        alert(bdcdyhs);
//                        alert(djIds);

            }
        });
        if(ljzFlag!=null && ljzFlag!="" && ljzFlag=="true"){
            $.ajax({
                url: bdcdjUrl+'/wfProject/checkLjz?sfyc=1&proid=' + proid + "&dcbIndexs=" + dcbIndexs,
                type: 'post',
                dataType: 'json',
                cache: false,
                success: function (data) {
                    passCheck=checkinfo(data,passCheck);
                    if(passCheck!=null && passCheck!="" && passCheck=="1"){
                        if(bdcdyhs!=null && bdcdyhs!=""){
                            $.ajax({
                                url: bdcdjUrl+'/wfProject/checkMulBdcXm?proid='+ proid +"&bdcdyhs="+bdcdyhs,
                                type: 'post',
                                dataType: 'json',
                                success: function (data) {
                                    passCheck=checkinfo(data,passCheck);
                                    if(passCheck!=null && passCheck!="" && passCheck=="1") {
//                                            alert("!!!!");
                                        if (zjgcdyFw != null && zjgcdyFw != "" && zjgcdyFw == "2") {
                                            createZjjzwxx(proid,dcbIndexs,djIds,bdcdyhs);
                                        } else {
                                            initProject(proid,dcbIndexs,djIds,bdcdyhs);
                                        }
                                    }
                                }
                            });
                        }
                        else{
//                                alert("!!!!");
                            if (zjgcdyFw != null && zjgcdyFw != "" && zjgcdyFw == "2") {
                                createZjjzwxx(proid,dcbIndexs,djIds,bdcdyhs);
                            } else {
                                initProject(proid,dcbIndexs,djIds,bdcdyhs);
                            }
                        }
                    }
                }
            });
        }
        else{
            if(bdcdyhs!=null && bdcdyhs!=""){

                $.ajax({
                    url: bdcdjUrl+'/wfProject/checkMulBdcXm?proid='+ proid +"&bdcdyhs="+bdcdyhs,
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        passCheck=checkinfo(data,passCheck);
                        if(passCheck!=null && passCheck!="" && passCheck=="1") {
//                                alert("!!!!");
                            if (zjgcdyFw != null && zjgcdyFw != "" && zjgcdyFw == "2") {
                                createZjjzwxx(proid,dcbIndexs,djIds,bdcdyhs);
                            } else {
                                initProject(proid,dcbIndexs,djIds,bdcdyhs);
                            }
                        }
                    }
                });

            }
        }

    }
    else{
        alert("请至少选择一条数据！");
        return;
    }
}
function initProject(proid,dcbIndexs,djIds,bdcdyhs){
    var zjgcdyFw = "1";
    $.ajax({
        type: 'post',
        url: bdcdjUrl+'/wfProject/initVoFromOldData?sfyc=1&bdclx=TDFW&gwc=1&proid='+proid+"&djIds="+djIds+"&bdcdyhs="+bdcdyhs+"&dcbIndexs="+dcbIndexs,
        success: function (data) {

            if (data == '成功') {
                if (zjgcdyFw != null && zjgcdyFw != "" && zjgcdyFw == "1") {
                    createZjjzwxx(proid,dcbIndexs,djIds,bdcdyhs);
                }
                refreshPage();

            } else {
                alert(data);
            }
            //去掉遮罩
            setTimeout($.unblockUI, 10);
        },
        error: function (_ex) {
            //去掉遮罩
            alert("error");
            setTimeout($.unblockUI, 10);
            alert("保存失败!失败原因:" + _ex);
        }
    });
}
function checkinfo(data,passCheck){
    var alertSize = 0;
    var confirmSize = 0;
    if (data.length > 0) {
        var islw = false;
        $("#csdjAlertInfo,#csdjConfirmInfo").html("");
        $.each(data, function (i, item) {
            if (item.checkModel == "confirm") {
                confirmSize++;
                $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info + '\')">查看</span>' + item.checkMsg + '</div>');
            } else if (item.checkModel == "alert") {
                alertSize++;
                if (isNotBlank(item.wiid)) {
                    islw = true;
                    confirmCreateLw(item, "${bdcdjUrl}", "${sflw}");
                }else {
                    if(item.checkCode=='199'){
                        $("#csdjAlertInfo").append('<div class="alert alert-info"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="bdcPic(\'' + item.info + '\')" >匹配</span>' + item.checkMsg + '</div>');
                    }else{
                        $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info + '\')" >查看</span>' + item.checkMsg + '</div>');
                    }
                }
            }
        });
        if (!islw) {
            $("#tipPop").show();
            $("#modal-backdrop").show();
        }
    }
    if (alertSize == 0 && confirmSize == 0) {
        passCheck="1";
    } else if (alertSize == 0 && confirmSize > 0) {
        $("span[name='hlBtn']").click(function () {
            $(this).parent().remove();
            if ($("#csdjConfirmInfo > div").size() == 0) {
                passCheck="1";
            }
        })
    }
    return passCheck;
}

function openProjectInfo(proid) {
    if (proid && proid != undefined) {
        $.ajax({
            url: bdcdjUrl+"/qllxResource/getViewUrl?proid=" + proid,
            type: 'post',
            success: function (data) {
                if(data&& data != undefined){
                    openWin(data);
                }
            },
            error: function (data) {
                tipInfo("查看失败！");
            }
        });
    }
}
function createZjjzwxx(proid,dcbIndexs,djIds,bdcdyhs){
    $.blockUI({message: "请稍等……"});
    $.ajax({
        type:"post",
        url: bdcdjUrl+'/bdcZjjzw/createZjjzwxx?proid='+proid+"&dcbIndexs="+dcbIndexs+"&djIds="+djIds+"&bdcdyhs="+bdcdyhs,
        success:function (data) {
            //去掉遮罩
            setTimeout($.unblockUI, 10);
            if (data == '成功') {
                alert(data);
                window.parent.hideModel();
                window.parent.resourceRefresh();
            }else{
                tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
            }
        },
        error:function (XMLHttpRequest, textStatus, errorThrown) {
            /*if(XMLHttpRequest.readyState==4){
                setTimeout($.unblockUI, 10);
                alert("选择失败!");
            }*/

            //去掉遮罩
            setTimeout($.unblockUI, 10);
            tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
        }
    });
}

function refreshPage(){
    $.cookie('lpb_cookie', '', { expires: -1 });
    mulSelectJson={};
    window.opener.refresh();
    window.close();
}

function clearLpbCookie(){
//        $.cookie('lpb_cookie', null);
    $.cookie('lpb_cookie', '', { expires: -1 });
    mulSelectJson={};
//        $("#ywsjMulXx").html("<span>已选择</span>");
    alert("清空成功！");
}
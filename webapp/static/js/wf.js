$(document).ready(function () {
    loadLeftMenu();
    $("body").attr("data-spy", "scroll").attr("data-target", ".bs-docs-sidebar");
});
function loadLeftMenu() {
    var winHeight = $(window).height();
    var leftMenuHieght = 0;
    if (winHeight > 0) {
        leftMenuHieght = winHeight - 40;
    }
    $(".content").css({height: leftMenuHieght + "px"});
}
function sjdCz(cellvalue, options, rowObject) {
    return '<div style="margin-left:8px;">' +
        '<div><a class="detail" href="javascript:detailSjdxx(\'' + cellvalue + '\')">详细</a><button type="button"  class="btn btn-primary" onclick="printSjd(\'' + cellvalue + '\')">打印</button></div>' +
        '</div>'
}
function sqrxxCz(cellvalue, options, rowObject) {
    return '<div style="margin-left:8px;">' +
        '<div><a class="detail" href="javascript:detailQlrxx(\'' + cellvalue + '\')">详细</a><button type="button"  class="btn btn-danger" onclick="delSqrxx(\'' + cellvalue + '\')">删除</button></div>' +
        '</div>'
}
function printSjd(proid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl+"/bdcdjSlxx/bdcdjSlxOpenSjd?proid=" + proid,
        dataType: "json",
        success: function (result) {
            var sjxxNum = result.sjxxNum;
            var djzxdm = result.djzxdm;
            var proid = result.proid;
            var url = reportUrl+"/ReportServer?reportlet=print%2Fbdc_sjd.cpt&op=write&proid=" + proid + "&sjxxnum=" + sjxxNum + "&djzxdm=" + djzxdm;
            openWin(url);
        }
    });
}
function detailSjdxx(proid) {
    var url = bdcdjUrl+'/bdcdjSlxx/bdcdjSjxx?proid=' + proid+'&taskid='+taskid+'&from='+from+'&rid='+rid+'&wiid='+wiid;
    // showComWindow(url, "权利人信息");
    showIndexModel(url,"收件单信息",1000,800,false);
}
function detailQlrxx(sqrid) {
    var url = bdcdjUrl+'/bdcdjSqrxx?sqrid=' + sqrid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
    // showComWindow(url, "权利人信息");
    showIndexModel(url,"申请人信息",1000,800,false);
}
function ggxxCz(cellvalue, options, rowObject) {
    return '<div style="margin-left:0px;">' +
        '<div><a class="detail" href="javascript:editGgxx(\'' + rowObject.GGID+ '\')">详细</a><button type="button"  class="btn btn-info" onclick="fbGgxx(\'' +rowObject.GGID+ '\')">发布</button><button type="button"  class="btn btn-danger" onclick="delGgxx(\'' +rowObject.GGID+ '\')">删除</button></div>' +
        '</div>';
}
function delSqrxx(sqrid) {
    if(confirm("确定删除申请人吗?")) {
        $.blockUI({message: "正在删除，请稍等……"});
        $.ajax({
            url: bdcdjUrl + "/bdcdjSqrxx/delSqrxx?sqrid=" + sqrid,
            type: 'POST',
            dataType: 'json',
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        alert("删除成功");
                        var dataUrl = bdcdjUrl + "/bdcdjSqrxx/getSqrxxPagesJson?wiid=" + wiid;
                        tableReload("qlr-grid-table", dataUrl, '', '', '');
                    }
                }
            },
            error: function (data) {
                alert("删除失败");
            }
        });
    }
}
function detailGgxx(qlrid) {
    openWin(bdcdjUrl+'/bdcdjSqrxx?qlrid=' + qlrid);
}
function tableReload(table, Url, data, colModel, loadComplete) {
    var index = 0;
    var jqgrid = $("#" + table);
    if (colModel == '' && loadComplete == '') {
        jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
    } else if (loadComplete == '' && colModel != '') {
        jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data, colModel: colModel});
    } else if (loadComplete != '' && colModel != '') {
        jqgrid.setGridParam({
            url: Url,
            datatype: 'json',
            page: 1,
            postData: data,
            colModel: colModel,
            loadComplete: loadComplete
        });
    } else if (loadComplete != '' && colModel == '') {
        jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data, loadComplete: loadComplete});
    }
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}

function rendererZjlx(cellvalue, options, rowObject) {
    var value = cellvalue;
    $.each(zjlxListJosn, function (i, item) {
        if (item.dm == cellvalue) {
            value = item.mc;
        }
    });
    return value;
}
function rendererQlrlx(cellvalue, options, rowObject) {
    var value = cellvalue;
    if (value == "qlr") {
        value = "权利人";
    } else if (value == "ywr") {
        value = "义务人";
    }else{
        value="";
    }
    return value;
}
function rendererSqrlx(cellvalue, options, rowObject) {
    var value = cellvalue;
    if (value == "1") {
        value = "权利人";
    } else if (value == "2") {
        value = "义务人";
    }else{
        value="";
    }
    return value;
}
function rendererGyfs(cellvalue, options, rowObject) {
    var value = cellvalue;
    if (value == "0") {
        value = "单独所有";
    } else if (value == "1") {
        value = "共同共有";
    } else if (value == "2") {
        value = "按份共有";
    } else if (value == "3") {
        value = "其他共有";
    } else {
        value = "";
    }
    return value;
}
function bdcdyxxCz(cellvalue, options, rowObject) {
    return '<div style="margin-left:0px;">' +
        '<div><a class="detail" href="javascript:detailBdcdyxx(\'' + rowObject.BDCDYID + '\')">详细</a><button type="button"  class="btn btn-info"onclick="djbxx(\'' + rowObject.BDCDYH + '\')">登记历史</button><button type="button"  class="btn btn-danger" onclick="delBdcdyxx(\'' +  rowObject.BDCDYID + '\')">删除</button></div>' +
        '</div>'
}
function fsssCz(cellvalue, options, rowObject) {
    var value = cellvalue;
    if (value !=""&&value!=null&&value!=undefined) {
        value = "附属设施";
    } else {
        value = "主房";
    }
    return value;
}
function detailBdcdyxx(bdcdyid) {
    var url = bdcdjUrl+'/bdcdjBdcdyxx?bdcdyid=' + bdcdyid+'&wiid='+wiid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
    showIndexModel(url,"不动产单元信息",1000,800,false);
}
var refreshBdcdyxx = function () {
    var dataUrl = bdcdjUrl+"/bdcdjBdcdyxx/getBdcdyxxPagesJson?wiid="+wiid;
    tableReload("bdcdy-grid-table", dataUrl, '', '', '');
}
function djbxx(bdcdyh) {
    var url =analysisUrl+"/cxURI/getBdcQlxxByBdcdyh?bdcdyh=" + bdcdyh;
    openWin(url);
}
function delBdcdyxx(bdcdyid) {
    $.blockUI({message: "正在删除，请稍等……"});
    $.ajax({
        url: bdcdjUrl+"/bdcdjBdcdyxx/delBdcdyxx?bdcdyid=" + bdcdyid+"&wiid="+wiid,
        type: 'POST',
        dataType: 'json',
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (isNotBlank(data)) {
                if (data.msg == "success") {
                    alert("删除成功");
                    var dataUrl = bdcdjUrl+"/bdcdjBdcdyxx/getBdcdyxxPagesJson?wiid="+wiid;
                    tableReload("bdcdy-grid-table", dataUrl, '', '', '');
                    var qlxxUrl =bdcdjUrl+ "/bdcdjSlxx/getQlxxPagesJson?wiid="+wiid;
                    tableReload("qlxx-grid-table", qlxxUrl, '', '', '');
                    var yqlxxUrl = bdcdjUrl+"/bdcdjSlxx/getYqlxxPagesJson?wiid="+wiid;
                    tableReload("yqlxx-grid-table", yqlxxUrl, '', '', '');
                }
            }
        },
        error: function (data) {
            alert("删除失败");
        }
    });
}
function qllxCz(cellvalue, options, rowObject) {
    return '<div style="margin-left:8px;">' +
        '<div><a class="detail" href="javascript:detailQlxx(\'' + cellvalue + '\')">详细</a><button type="button"  class="btn btn-danger" onclick="delQlxx(\'' + cellvalue + '\')">删除</button><button type="button"  class="btn btn-success"onclick="glSqr(\'' + cellvalue + '\',\'' + rowObject.QLLXDM + '\',\'' + rowObject.PROID + '\')">关联申请人</button></div>' +
        '</div>'
}

function detailQlxx(proid) {
    var url = bdcdjUrl+'/bdcdjQlxx?proid=' + proid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
    // showComWindow(url, "权利信息");
    showIndexModel(url,"权利信息",1000,550,false);
}

function delQlxx(proid) {

    $.blockUI({message: "正在删除，请稍等……"});
    $.ajax({
        url: bdcdjUrl+"/bdcdjQlxx/delQl?proid=" + proid+"&wiid="+wiid,
        type: 'POST',
        dataType: 'json',
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (isNotBlank(data)) {
                if (data.msg == "success") {
                    alert("删除成功");
                    var qlxxUrl =bdcdjUrl+ "/bdcdjSlxx/getQlxxPagesJson?wiid="+wiid;
                    tableReload("qlxx-grid-table", qlxxUrl, '', '', '');
                    var yqlxxUrl = bdcdjUrl+"/bdcdjSlxx/getYqlxxPagesJson?wiid="+wiid;
                    tableReload("yqlxx-grid-table", yqlxxUrl, '', '', '');
                }
            }
        },
        error: function (data) {
            alert("删除失败");
        }
    });

}
function yqllxCz(cellvalue, options, rowObject) {
    return '<div align="center">' +'<div><a class="detail" href="javascript:detailYqlxx(\'' + rowObject.PROID + '\')">详细</a></div>' +
        '</div>'
}
function detailYqlxx(proid) {
    var url = bdcdjUrl+'/bdcdjQlxx?proid=' + proid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
    showIndexModel(url,"原权利信息",1000,550,false);
}
function glSqr(qlid,qllxdm,proid) {
    var url = bdcdjUrl+'/bdcdjQlxx/glSqr?proid=' + proid + '&wiid=' + wiid + '&qlid=' + qlid+ '&qllxdm=' + qllxdm+'&taskid='+taskid+'&from='+from+'&rid='+rid;
    showIndexModel(url,"关联申请人",1000,800,false);
}
function sjclCz(cellvalue, options, rowObject) {
    return '<div style="margin-left:0px;">' +
        '<div><a class="detail" href="javascript:editSjcl(\'' + rowObject.SJCLID+ '\')">详细</a><button type="button"  class="btn btn-success" onclick="fileUp(\'' + rowObject.CLMC + '\')">上传</button><button type="button"  class="btn btn-danger" onclick="delSjcl(\'' + rowObject.CLMC + '\',\'' + rowObject.SJCLID+ '\')">删除</button></div>' +
        '</div>'
}
function openWin(url, name) {
    var w_width = screen.availWidth - 10;
    var w_height = screen.availHeight - 32;
    window.open(url, name, "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
}
function saveSlxx() {
    var proid = $("#proid").val();
    var mjdw = $("#mjdw").val();
    $.blockUI({message: "请稍等……"});
    $.ajax({
        url: bdcdjUrl+"/bdcdjSlxx/saveSlxx",
        type: 'POST',
        dataType: 'json',
        data: $("#slxxForm").serialize(),
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (isNotBlank(data)) {
                if (data.msg == "success") {
                    alert("保存成功");
                    $.ajax({
                        type:'get',
                        async:true,
                        url:bdcdjUrl+'/wfProject/updateWorkFlow?proid=' + proid,
                        success:function (data) {
                        },
                    });
                }
            }
        },
        error: function (data) {
            alert("保存失败");
        }
    });

}
function openSjd() {
    // $.ajax({
    //     type: "GET",
    //     url: bdcdjUrl+"/bdcdjSlxx/bdcdjSlxOpenSjd?wiid=" + wiid,
    //     dataType: "json",
    //     success: function (result) {
    //         var sjxxNum = result.sjxxNum;
    //         var djzxdm = result.djzxdm;
    //         var proid = result.proid;
    //         var url = reportUrl+"/ReportServer?reportlet=print%2Fbdc_sjd.cpt&op=write&proid=" + proid + "&sjxxnum=" + sjxxNum + "&djzxdm=" + djzxdm;
    //         openWin(url);
    //     }
    // });
    var proid = $("#proid").val();
    var url = bdcdjUrl+'/bdcdjSjdxx/bdcdjSjdList?wiid=' + wiid +'&taskid='+taskid+'&from='+from+'&rid='+rid+'&proid='+proid;
    showIndexModel(url,"收件单列表",1000,800,false);
}
function openSqs() {
    var proid = $("#proid").val();
    var url = bdcdjUrl+'/bdcdjSqsxx/bdcdjSqsList?wiid=' + wiid +'&taskid='+taskid+'&from='+from+'&rid='+rid+'&proid='+proid;
    showWindow(url,"申请书列表",1000,800);
}
function openSpb() {
    var proid = $("#proid").val();
    var url = bdcdjUrl+'/bdcdjSpbxx/bdcdjSpbList?wiid=' + wiid +'&taskid='+taskid+'&from='+from+'&rid='+rid+'&proid='+proid;
    showIndexModel(url,"审批表列表",1000,800,false);
}
function openBdcqz() {
    var ModalUrl = "C:/GTIS/zsPrint.fr3";
    printFormByWiid(ModalUrl, wiid, "zs");
}
function openBdcqzm() {
    var ModalUrl = "C:/GTIS/zmsPrint.fr3";
    printFormByWiid(ModalUrl, wiid, "zms");
}
function printFormByWiid(ModalUrl,wiid,zslx){
    var  DataUrl=bdcdjUrl+"/bdcPrint/printAllBdcqzs?wiid="+wiid+"&zslx="+zslx+"&hiddeMode=false";
    printSheet(ModalUrl,DataUrl,false,false);
}
function printSheet(ModalUrl,DataUrl,DesignMode,hiddeMode){
    window.location.href = "eprt://" + DataUrl;
}
//检查zsbh
function checkAllZsbh(wiid,zslx){
    var check='';
    $.ajax({
        url: bdcdjUrl+"/fraJax/checkAllZsbh?wiid="+wiid+"&zslx=" + zslx,
        type: 'GET',
        async:false,
        success: function (result) {
            setTimeout($.unblockUI, 10);
            if (isNotBlank(result)) {
                if(result=='error'){
                   alert("创建失败！");
                }else if(result=='success'){
                    check='true';
                }else{
                    alert(result);
                }
            }
        },
        error: function (data) {
            alert("保存失败");
        }
    });
    return check;
}
function addQlr() {
    showIndexModel(bdcdjUrl+'/bdcdjSqrxx/addSqr?wiid=' + wiid+'&taskid='+taskid+'&from='+from+'&rid='+rid,"申请人信息",1000,800,false);
}
function addQlxx() {
    showIndexModel(bdcdjUrl+'/bdcdjQlxx/addQl?wiid=' + wiid+'&taskid='+taskid+'&from='+from+'&rid='+rid,"新增权利信息",1000,800,false);
}
function addFsss() {
    var proid = $("#proid").val();
    var iWidth = 1200;
    var iHeight = 800;
    var iTop = (window.screen.availHeight - 30 - iHeight) / 2;
    var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;
    var win = window.open(bdcdjUrl+'/bdcFwfsss?wiid=' + wiid+'&taskid='+taskid+'&from='+from+'&rid='+rid+'&proid='+proid, "新增附属设施", "width=" + iWidth + ", height=" + iHeight + ",top=" + iTop + ",left=" + iLeft + ",toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no,alwaysRaised=yes,depended=yes");
}
function addBdcdy() {
    debugger;
    var proid = $("#proid").val();
    var url=bdcdjUrl+"/bdcdjBdcdyxx/addBdcdy?proid=" + proid + "&wiid=" + wiid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
    showIndexModel(url,"增加不动产单元",1000,550,false);
}
function addGgxx() {
    var proid = $("#proid").val();
    var url = bdcdjUrl+"/bdcdjSlxx/bdcGgxx?proid=" + proid + "&wiid="+ wiid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
    showIndexModel(url,"公告信息",1000,550,false);
}
function addSjcl() {
    debugger;
    var proid = $("#proid").val();
    var sjxxid = $("#sjxxid").val();
    $.ajax({
        type:'post',
        url:'/estateplat-server/bdcdjSlxx/creatSjcl',
        data:{proid: proid,wiid:wiid},
        success: function (data) {
                if (data.msg == "success") {
                    var url = bdcdjUrl+"/bdcdjSlxx/bdcSjclxx?proid=" + proid + "&sjxxid="+ sjxxid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
                    showWindow(url,"申请材料",1000,800);
                }
        },
        error: function (data) {
            alert("添加失败,请重新尝试！");
        }
    });
}
function fileUp(clmc) {
    debugger;
    var proid = $("#proid").val();
    setTimeout(function(){
        $.ajax({
            type:'post',
            url:'/estateplat-server/bdcdjSlxx/creatSjcl',
            data:{proid: proid,wiid:wiid},
            success:function (data) {
                var w_width=screen.availWidth-21;
                var w_height= screen.availHeight-47;
                var url=  "/estateplat-server/createSjdcl/createAllFileFolder?proid="+proid+"&clmc="+encodeURI(clmc);
                var title="收件单材料";
                var windowDia=window.showModalDialog(url, title, "dialogHeight="+w_height+"px;dialogWidth= "+w_width+"px");
                if(windowDia=='ok'){
                    if (isNotBlank(data)) {
                        if (data.msg == "success") {
                            $.ajax({
                                type: 'post',
                                url: '/estateplat-server/createSjdcl/saveSjclFs',
                                data: {proid: proid},
                                success: function (data) {
                                    var sjclxxUrl = bdcdjUrl + "/bdcdjSlxx/getSjclPagesJson?wiid=" + wiid;
                                    tableReload("sjclxx-grid-table", sjclxxUrl, '', '', '');
                                    // var  sjdxxSave=document.getElementById("sjdxxSaveButton");
                                    // if(sjdxxSave!=null){
                                    //     sjdxxSave.onclick();
                                    // }
                                    location.reload();
                                },
                                error: function (_ex) {
                                }
                            });
                        }
                    }
                }
        },
            error:function (_ex) {
            }
        });
    },2000);
}
function delSjcl(clmc,sjclid) {
    debugger;
    var proid = $("#proid").val();
    var sjxxid = $("#sjxxid").val();
    $.blockUI({message: "正在删除，请稍等……"});
    $.ajax({
        type:'post',
        url:'/estateplat-server/bdcdjSlxx/creatSjcl',
        data:{proid: proid,wiid:wiid},
        success: function (data) {
            if (isNotBlank(data)) {
                if (data.msg == "success") {
                    $.ajax({
                        url: bdcdjUrl+"/createSjdcl/delete?proid=" + proid+"&sjclid="+sjclid+"&sjxxid="+sjxxid+"&clmc="+encodeURI(clmc),
                        type: 'POST',
                        dataType: 'json',
                        success: function (data) {
                            setTimeout($.unblockUI, 10);
                            if (isNotBlank(data)) {
                                if (data.msg == "success") {
                                    alert("删除成功");
                                    // var sjclxxUrl = bdcdjUrl+"/bdcdjSlxx/getSjclPagesJson?wiid="+wiid;
                                    // tableReload("sjclxx-grid-table", sjclxxUrl, '', '', '');
                                    var contentFrame=window.parent.document.getElementById("contentFrame");
                                    if(contentFrame!=null){
                                        var frameMain=contentFrame.contentWindow.document.getElementById("frameMain");
                                        if(frameMain!=null){
                                            var contentPane=frameMain.contentWindow;
                                            if(contentPane!=null)
                                                contentPane.refreshGrid('sjclxx');
                                        }
                                    }
                                    //加入一个调用收件单父页面的保存功能
                                    var  sjdxxSave=document.getElementById("sjdxxSaveButton");
                                    if(sjdxxSave!=null){
                                        sjdxxSave.onclick();
                                    }
                                    location.reload();//刷新父页面，并关闭子页面。
                                    //去掉遮罩
                                    setTimeout($.unblockUI, 10);
                                }
                            }
                        },
                        error: function (data) {
                            alert("删除失败,请重新选择数据！");
                            setTimeout($.unblockUI, 10);
                            // var sjclxxUrl = bdcdjUrl+"/bdcdjSlxx/getSjclPagesJson?wiid="+wiid;
                            // tableReload("sjclxx-grid-table", sjclxxUrl, '', '', '');
                        }
                    });
                }
            }
        },
        error: function (data) {
            alert("删除失败,请重新尝试！");
        }
    });

}
function delGgxx(ggid) {
    var proid = $("#proid").val();
    $.blockUI({message: "正在删除，请稍等……"});
    $.ajax({
        url: bdcdjUrl+"/bdcdjSlxx/delGgxx?proid=" + proid+"&ggid="+ggid,
        type: 'POST',
        dataType: 'json',
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (isNotBlank(data)) {
                if (data.msg == "success") {
                    alert("删除成功");
                    // var ggxxUrl = bdcdjUrl+"/bdcdjSlxx/getGgxxPagesJson?wiid="+wiid;
                    // tableReload("ggxx-grid-table", ggxxUrl, '', '', '');
                    var contentFrame=window.parent.document.getElementById("contentFrame");
                    if(contentFrame!=null){
                        var frameMain=contentFrame.contentWindow.document.getElementById("frameMain");
                        if(frameMain!=null){
                            var contentPane=frameMain.contentWindow;
                            if(contentPane!=null)
                                contentPane.refreshGrid('ggxx');
                        }
                    }
                    //去掉遮罩
                    setTimeout($.unblockUI, 10);
                }
            }
        },
        error: function (data) {
            alert("删除失败");
            setTimeout($.unblockUI, 10);
            // var ggxxUrl = bdcdjUrl+"/bdcdjSlxx/getGgxxPagesJson?wiid="+wiid;
            // tableReload("ggxx-grid-table", ggxxUrl, '', '', '');
        }
    });
}
function editGgxx(ggid) {
    var proid = $("#proid").val();
    var url = bdcdjUrl+"/bdcdjSlxx/editGgxx?ggid=" + ggid + "&wiid="+ wiid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
    showIndexModel(url,"公告信息",1000,550,false);
}
function editSjcl(sjclid) {
    var proid = $("#proid").val();
    $.ajax({
        type:'post',
        url:'/estateplat-server/bdcdjSlxx/creatSjcl',
        data:{proid: proid,wiid:wiid},
        success: function (data) {
            if (isNotBlank(data)) {
                if (data.msg == "success") {
                    var url = bdcdjUrl+"/bdcdjSlxx/editSjcl?sjclid=" + sjclid +"&proid=" + proid + "&wiid="+ wiid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
                    showWindow(url,"收件材料",1000,550);
                }
            }
        },
        error: function (data) {
            alert("删除失败,请重新尝试！");
        }
    });
}

function chooseBdcdy() {
    var proid = $("#proid").val();
    window.open(bdcdjUrl+'/selectBdcdy?proid='+proid,
        "选择不动产单元","height=500, width=700, top=100, left=300, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
}
function refreshGrid(gridId){
    var wiid =  $("#wiid").val();
    var dataUrl;
    var tableId;
    if(gridId=='sqr'){
        tableId="qlr-grid-table";
        dataUrl = bdcdjUrl + "/bdcdjSqrxx/getSqrxxPagesJson?wiid=" + wiid;
    }else if(gridId=='bdcdy'){
        tableId="bdcdy-grid-table";
        dataUrl = bdcdjUrl + "/bdcdjBdcdyxx/getBdcdyxxPagesJson?wiid=" + wiid;
    }else if(gridId=='qlxx'){
        tableId="qlxx-grid-table";
        dataUrl = bdcdjUrl + "/bdcdjSlxx/getQlxxPagesJson?wiid=" + wiid;
    }else if(gridId=='yqlxx'){
        tableId="yqlxx-grid-table";
        dataUrl = bdcdjUrl + "/bdcdjSlxx/getYqlxxPagesJson?wiid=" + wiid;
    }else if(gridId=='sjclxx'){
        tableId="sjclxx-grid-table";
        dataUrl = bdcdjUrl + "/bdcdjSlxx/getSjclPagesJson?wiid=" + wiid;
    }else if(gridId=='ggxx'){
        tableId="ggxx-grid-table";
        dataUrl = bdcdjUrl + "/bdcdjSlxx/getGgxxPagesJson?wiid=" + wiid;
    }else if(gridId=='zs'){
        tableId = "zsxx-grid-table";
        dataUrl = bdcdjUrl + "/bdcdjSlxx/getZsxxPagesJson?wiid=" + wiid;
    }else if(gridId=='sjxx'){
        tableId = "sjxx-grid-table";
        dataUrl = bdcdjUrl + "/bdcdjSlxx/getBdcXmxxPagesJson?wiid=" + wiid;
    }
    tableReload(tableId, dataUrl, '', '', '');

}
function upFile(clmc) {
    var proid = $("#proid").val();
    $.ajax({
        type: 'post',
        url: '/estateplat-server/bdcdjSlxx/creatSjcl',
        data: {proid: proid, wiid: wiid},
        success: function (data) {
            var w_width = screen.availWidth - 21;
            var w_height = screen.availHeight - 47;
            var url = bdcdjUrl + "/createSjdcl/createAllFileFolder?proid=" + proid + "&clmc=";

            var title = "收件单材料";

            var windowDia = window.showModalDialog(url, title, "dialogHeight=" + w_height + "px;dialogWidth= " + w_width + "px");

            if (windowDia == 'ok') {
                var data = {proid: proid};
                $.ajax({
                    type: 'post',
                    url: '/estateplat-server/createSjdcl/saveSjclFs',
                    data: data,
                    success: function (data) {
                        window.close();
                        var sjclxxUrl = bdcdjUrl+"/bdcdjSlxx/getSjclPagesJson?wiid="+wiid;
                        tableReload("sjclxx-grid-table", sjclxxUrl, '', '', '');
                        // var  sjdxxSave=document.getElementById("sjdxxSaveButton");
                        // if(sjdxxSave!=null){
                        //     sjdxxSave.onclick();
                        // }
                        location.reload();//刷新父页面，并关闭子页面。
                    },
                    error: function (_ex) {

                    }
                });

            }
        },
        error: function (_ex) {

        }
    });
}
function fileView() {
    var wiid = $("#wiid").val();
    var proid = $("#proid").val();
    var w_width = screen.availWidth - 21;
    var w_height = screen.availHeight - 47;
    var url = bdcdjUrl + "/bdcdjSlxx/browseFile?proid=" + proid ;
    var title = "材料预览";
    var windowDia = window.showModalDialog(url, title, "dialogHeight=" + w_height + "px;dialogWidth= " + w_width + "px");
}
function creatBdcqz() {
    var proid = $("#proid").val();
    var url = bdcdjUrl+"/bdcdjSlxx/creatBdcqz?proid=" + proid + "&wiid="+ wiid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
    showIndexModel(url,"公告信息",1000,550,false);
}
function zsxxCz(cellvalue, options, rowObject) {
        return '<div align="center">' + '<div><a class="detail" href="javascript:detailZsxx(\'' + rowObject.PROID+ '\',\'' +rowObject.ZSID+ '\',\'' +rowObject.BDCQZH+ '\')">详细</a></div>' +
            '</div>'
    }
 function detailZsxx(proid, zsid, bdcqzh) {
        var url = "";
        var title = "";
        if (bdcqzh != null && bdcqzh.indexOf("不动产权") > -1) {
            url = reportUrl + "/ReportServer?reportlet=edit%2Fbdc_bdcqz.cpt&op=write&proid=" + proid+"&zsid=" + zsid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
            title = "不动产权证";
        } else {
            url = reportUrl + "/ReportServer?reportlet=edit%2Fbdcqzms.cpt&op=write&proid=" + proid+"&zsid=" + zsid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
            title = "不动产权证明";
        }
        showIndexModel(url, title, 1000, 550, false);
    }
function ValidateIdCard(sId){
    debugger;
    var aCity = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江 ", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北 ", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏 ", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外 " }
    var iSum = 0;
    var info = "";
    // if (!/^d{17}(d|x)$/i.test(sId)) return 'Error:非法证号';
    sId = sId.replace(/x$/i, "a");
    if(sId==null || sId=="") return "身份证号为空！";
    if (aCity[parseInt(sId.substr(0, 2))] == null) return "身份证号格式不正确！";
    sBirthday = sId.substr(6, 4) + "-" + Number(sId.substr(10, 2)) + "-" + Number(sId.substr(12, 2));
    var d = new Date(sBirthday.replace(/-/g, "/"))
    if (sBirthday != (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate())) return "身份证号格式不正确！";
    for (var i = 17; i >= 0; i--) iSum += (Math.pow(2, i) % 11) * parseInt(sId.charAt(17 - i), 11)
    if (iSum % 11 != 1) return "身份证号格式不正确！";
    // return aCity[parseInt(sId.substr(0, 2))] + "," + sBirthday + "," + (sId.substr(16, 1) % 2 ? "男" : "女")
}
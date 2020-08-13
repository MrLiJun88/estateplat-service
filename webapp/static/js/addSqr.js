function qllxCz(cellvalue, options, rowObject) {
    return '<div >' +
        '<div><button type="button"  class="btn btn-primary" onclick="glQlxx(\'' + rowObject.QLID + '\',\'' + rowObject.QLLXDM + '\',\'' + rowObject.PROID + '\',\'' + "ywr" + '\')">义务人</button><button type="button"  class="btn btn-primary" onclick="glQlxx(\'' + rowObject.QLID+ '\',\'' + rowObject.QLLXDM + '\',\'' + rowObject.PROID + '\',\'' + "qlr" + '\')">权利人</button></div>'+
        '</div>'
}
function checkSqr() {
    var check='';
    $.ajax({
        url: bdcdjUrl+"/bdcdjSqrxx/checkSqr?sqrid=" + sqrid,
        type: 'GET',
        async:false,
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (isNotBlank(data)) {
                if (data.msg == "success") {
                    check='true';
                }else{
                    alert("请先保存申请人");
                }
            }
        },
        error: function (data) {
            alert("请先保存申请人");
        }
    });
    return check;
}
function glQlxx(qlid,qllxdm,proid,qlrlx) {
    var check=checkSqr();
    if(check=='true') {
        var wiid = $("#wiid").val();
        $.blockUI({message: "正在关联，请稍等……"});
        $.ajax({
            url: bdcdjUrl + "/bdcdjSqrxx/glSqr?sqrid=" + sqrid + "&proids=" + proid + "&qlids=" + qlid + "&qllxdms=" + qllxdm + "&qlrlx=" + qlrlx,
            type: 'POST',
            dataType: 'json',
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        alert("关联成功");
                        $.ajax({
                            type: 'get',
                            async: true,
                            url: bdcdjUrl+"/wfProject/updateWorkFlow?wiid="+ wiid,
                            success: function (data) {

                            }
                        });
                        var dataUrl = bdcdjUrl + "/bdcdjSlxx/getQlxxPagesJson?wiid=" + wiid;
                        tableReload("qlxx-grid-table", dataUrl, '', '', '');
                    }
                }
            },
            error: function (data) {
                alert("关联失败");
            }
        });
    }
}
function glQlr(qlrlx){
    if ($mulData.length == 0) {
        alert("请至少选择一条数据！");
        return;
    }
    var check=checkSqr();
    if(check=='true') {
        var wiid = $("#wiid").val();
        var qlids = "";
        var proids = "";
        var qllxdms = "";
        for (var i = 0; i < $mulData.length; i++) {
            qlids += $mulData[i].QLID + ",";
            proids += $mulData[i].PROID + ",";
            qllxdms += $mulData[i].QLLXDM + ",";
        }
        if (qlids != null && qlids != "") {
            qlids = qlids.substring(0, qlids.length - 1);
        }
        if (proids != null && proids != "") {
            proids = proids.substring(0, proids.length - 1);
        }
        if (qllxdms != null && qllxdms != "") {
            qllxdms = qllxdms.substring(0, qllxdms.length - 1);
        }
        $.blockUI({message: "正在关联，请稍等……"});
        $.ajax({
            url: bdcdjUrl + "/bdcdjSqrxx/glSqr?sqrid=" + sqrid + "&proids=" + proids + "&qlids=" + qlids + "&qllxdms=" + qllxdms + "&qlrlx=" + qlrlx,
            type: 'POST',
            dataType: 'json',
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        alert("关联成功");
                        $.ajax({
                            type: 'get',
                            async: true,
                            url: bdcdjUrl+"/wfProject/updateWorkFlow?wiid="+ wiid,
                            success: function (data) {

                            }
                        });
                        var dataUrl = bdcdjUrl + "/bdcdjSlxx/getQlxxPagesJson?wiid=" + wiid;
                        tableReload("qlxx-grid-table", dataUrl, '', '', '');
                    }
                }
            },
            error: function (data) {
                alert("关联失败");
            }
        });
    }
}
function saveQlrxx() {
    $.blockUI({message: "请稍等……"});
    $.ajax({
        url: bdcdjUrl+"/bdcdjSqrxx/saveSqrxx",
        type: 'POST',
        data: $("#sqrForm").serialize(),
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (data != null && data != "") {
                if (data.msg == "success") {
                    alert("保存成功");
                    //window.parent.hideModel();
                    var contentFrame=window.parent.document.getElementById("contentFrame");
                    if(contentFrame!=null){
                        var frameMain=contentFrame.contentWindow.document.getElementById("frameMain");
                        if(frameMain!=null){
                            var contentPane=frameMain.contentWindow;
                            if(contentPane!=null)
                                contentPane.refreshGrid('sqr');
                        }
                    }
                }
            }
        },
        error: function (data) {
            alert("保存失败");
        }
    });
}
function addQlrxx() {
    $.ajax({
        url: bdcdjUrl+"/bdcdjSqrxx/saveSqrxx",
        type: 'POST',
        data: $("#sqrForm").serialize(),
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (data != null && data != "") {
                if (data.msg == "success") {
                    alert("保存成功");
                    var contentFrame=window.parent.document.getElementById("contentFrame");
                    if(contentFrame!=null){
                        var frameMain=contentFrame.contentWindow.document.getElementById("frameMain");
                        if(frameMain!=null){
                            var contentPane=frameMain.contentWindow;
                            if(contentPane!=null)
                                contentPane.refreshGrid('sqr');
                        }
                    }
                    document.location.reload();
                }
            }
        },
        error: function (data) {
            alert("保存失败");
        }
    });
}

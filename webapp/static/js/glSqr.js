function glSqrCz(cellvalue, options, rowObject) {
    return '<div >' +
        '<div><button type="button"  class="btn btn-primary" onclick="glSqrxx(\'' + cellvalue + '\',\'' + "ywr" + '\')">义务人</button><button type="button"  class="btn btn-primary" onclick="glSqrxx(\'' + cellvalue + '\',\'' + "qlr" + '\')">权利人</button></div>'+
        '</div>'
}
function qlrxxCz(cellvalue, options, rowObject) {
    return '<div >' +
        '<div><button type="button"  class="btn btn-primary" onclick="delQlrxx(\'' + rowObject.QLRID + '\')">删除</button></div>' +
        '</div>'
}
function rendererQlrlx(cellvalue, options, rowObject) {
    if (cellvalue == 'qlr' || cellvalue == "" || cellvalue == null)
        return '<span class="label label-success" onclick="setQlrlx(\'' + rowObject.QLRID + '\')">权利人</span>&nbsp;';
    else if (cellvalue == 'ywr')
        return '<span class="label label-danger" onclick="setQlrlx(\'' + rowObject.QLRID + '\')">义务人</span>&nbsp;';
}
//设置权利人类型
function setQlrlx(qlrid) {
    var proid = $("#proid").val();
    $.ajax({
        type: "POST",
        async: false,
        url: bdcdjUrl+"/bdcdjQlrxx/setQlrlx?qlrid=" + qlrid,
        success: function (data) {
            if (isNotBlank(data)&&data.msg == "success") {
                alert("设置成功");
                $.ajax({
                    type: 'get',
                    async: true,
                    url: bdcdjUrl+"/wfProject/updateWorkFlow?proid="+ proid,
                    success: function (data) {

                    }
                });
                var dataUrl = bdcdjUrl + "/bdcdjQlxx/getQlrxxPagesJson?proid=" + proid;
                tableReload("qlr-grid-table", dataUrl, '', '', '');
            }
        },
        error: function (data) {
            alert("设置失败");
        }
    });
}
function glSqrxx(sqrid,qlrlx) {
    var wiid = $("#wiid").val();
    var qlid = $("#qlid").val();
    var qllxdm = $("#qllxdm").val();
    var proid = $("#proid").val();
    $.blockUI({message: "正在关联，请稍等……"});
    $.ajax({
        url: bdcdjUrl+"/bdcdjSqrxx/glSqr?sqrid=" + sqrid+"&proids=" + proid+"&qlids=" + qlid+"&qllxdms=" + qllxdm+"&qlrlx="+qlrlx,
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
                    var dataUrl = bdcdjUrl + "/bdcdjQlxx/getQlrxxPagesJson?proid=" + proid;
                    tableReload("qlr-grid-table", dataUrl, '', '', '');
                }
            }
        },
        error: function (data) {
            alert("关联失败");
        }
    });
}
function delQlrxx(qlrid) {
    var proid = $("#proid").val();
    if(confirm("确定删除权利人吗?")) {
        $.blockUI({message: "正在删除，请稍等……"});
        $.ajax({
            url: bdcdjUrl + "/bdcdjQlrxx/delQlrxx?qlrid=" + qlrid,
            type: 'POST',
            dataType: 'json',
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        alert("删除成功");
                        $.ajax({
                            type: 'get',
                            async: true,
                            url: '${bdcdjUrl}/wfProject/updateWorkFlow?wiid=' + wiid,
                            success: function (data) {

                            }
                        });
                        var dataUrl = bdcdjUrl + "/bdcdjQlxx/getQlrxxPagesJson?proid=" + proid;
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
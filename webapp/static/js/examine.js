var confirmCreateLw = function (data, bdcdjUrl, sflw,ftlType) {
    var xzwh = "";
    var examineInfo = "";
    var wiid = data.wiid;
    if (data.hasOwnProperty("xzwh")) {
        xzwh = data.xzwh;
    }
    if (data.hasOwnProperty("examineInfo")) {
        examineInfo = data.examineInfo;
    }
    if (sflw == "true") {
        //showConfirmDialog("提示信息", "" + data.checkMsg + "," + "是否创建例外", "createLw", "'" + wiid + "','" + xzwh + "','" + examineInfo + "','" + bdcdjUrl + "'", "closeBlockUI", "");
        //showConfirmSeeDialog("提示信息", "" + data.checkMsg + "," +  "是否创建例外", data.checkPorids[0], "createLw", "'" + wiid + "','" + xzwh + "','" + examineInfo + "','" + bdcdjUrl + "'", "closeBlockUI", "");
        if(ftlType == "djsjMultiselectList"){
            $.ajax({
                url: bdcdjUrl + '/promptView/organizeData',
                type: 'post',
                dataType: 'json',
                data: {info: JSON.stringify(data.info)},
                success: function (data) {
                    if (data && data != 'undefined') {
                        hadleTipData(data);
                    }
                    if (valite.length == 0){
                        gdDisplayTip(gdproids, qlids);
                    }else {
                        $("#bdcdyTipPop").show();
                        $("#modal-backdrop").show();
                        mulGdDisplayTip();
                    }
                },
                error: function (data) {

                }
            });
        }
        else if(ftlType == "djsjBdcdyList"){
            $("#csdjAlertInfo,#csdjConfirmInfo").html("");
            $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + data.checkPorids[0] + '\')" >查看</span>' + data.checkMsg + '</div>');
            $("#tipPop").show();
            $("#modal-backdrop").show();
        }

    } else {
        tipInfo(data.checkMsg);
        closeBlockUI();
    }
};

function tipInfo(msg) {
    // bootbox.dialog({
    //     message: "<h3><b>" + msg + "</b></h3>",
    //     title: "",
    //     buttons: {
    //         main: {
    //             label: "关闭",
    //             className: "btn-primary"
    //         }
    //     }
    // });
    $.Prompt(msg,1500);
}

var createLw = function (wiid, xzwh, examineInfo, bdcdjUrl) {
    if (isBlank(xzwh)) {
        alert("查封文号为空，无法创建例外");
        return false;
    }
    $.ajax({
        url: bdcdjUrl + '/examine/creatWiid',
        type: 'post',
        dataType: 'json',
        data: {xzwh: xzwh, wiid: wiid, examineInfo: examineInfo},
        success: function (data) {
            if (data.hasOwnProperty("lwsqUrl")) {
                var lwsqUrl = data.lwsqUrl;
                window.parent.parent.$('#handleModel').modal('hide');
                window.parent.parent.showDynamicModel('exceptionModel', lwsqUrl, "增加例外")
            }
        },
        error: function (data) {

        }
    });
};

var closeBlockUI = function () {
    setTimeout($.unblockUI, 10);
};

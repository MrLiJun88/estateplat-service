/**
 * Created by jhj on 2017/5/23.
 */
function addBorderToDiffHistoryZd() {
    $.ajax({
        url: bdcdjUrl+"/bdcZddb/getHistoryZd",
        type: 'POST',
        data: {proid: '${proid!}', zdbInfo: 'false'},
        success: function (data) {
            if (isNotBlank(data)) {
                addBorder(data);
            }
        },
        error: function (jqXHR, exception) {
            $.Prompt("获取字段对比失败",1500);
        }
    });
}

function initHistoryZd() {
    $.ajax({
        url: bdcdjUrl+"/bdcZddb/getHistoryZd",
        type: 'POST',
        data: {proid: '${proid!}', zdbInfo: 'true'},
        success: function (data) {
            if (isNotBlank(data)) {
                showHover(data);
            }
        },
        error: function (jqXHR, exception) {
            $.Prompt("获取字段对比失败",1500);
        }
    });
}

function showHover(data) {
    var zdmc = data.zdmc;
    var zdvalue = data.zdvalue;
    for (var i = 0; i < zdmc.length; i++) {
        var mc = String(zdmc[i]);
        var djValue = $("#" + mc).val();
        $("#" + mc).attr("data-toggle", "tooltip");
        $("#" + mc).attr("title", "原权籍数据为：" + zdvalue["" + mc]);
        $("#" + mc).tooltip();
    }
}

function addBorder(data) {
    var zdmc = data.zdmc;
    var zdvalue = data.zdvalue;
    for (var i = 0; i < zdmc.length; i++) {
        var mc = String(zdmc[i]);
        var djValue = $("#" + mc).val();
        if (djValue != undefined && djValue != zdvalue["" + mc]) {
            $("#" + mc).css("border-color", "red");
        }
    }
}
//初始化是否有共有比例并改变共有方式
function changGyfs() {
    var blnumber = 0;
    $("input[name='fbcz']").each(function (i, n) {
        var qlrid = $(n).val();
        var qlbl = $("#qlbl_" + qlrid).val();
        if (qlbl.replace(/\s/g, "") == "") {
            qlbl = 0;
        }
        blnumber = blnumber + parseInt(qlbl);
    });
    if (blnumber != 1 && blnumber != 0) {
        $("#afgy").attr("checked", "checked");
        $.Prompt("权利人比例之和不为1！",1500);
    }
}
//验证共有比例是否为整数
function ValidateGybl(gybl) {
    var gz = /^\d+(\.\d{2})?$/;
    if (!gz.test(gybl))
        return "请正确输入两位小数！";
}
//计算权利人数量并对分别持证方式进行更改
function changFbcz() {
    var a = 0;
    $("input[name='fbcz']").each(function (i, n) {
        ++a;
    });
    if (a == 1) {
        $("#sqfbcz").attr("disabled", true);
    }
    else {
        $("#sqfbcz").attr("disabled", false);
    }
}
//删除行
function qlrDel(o) {
    debugger;
    var row = "tr_" + o;
    $.blockUI({message: "请稍等......"});
    $.ajax({
        url: bdcdjUrl+"/bdcdjSpbxx/delBdcQlr",
        type: 'POST',
        dataType: 'json',
        data: $.param({s: o}),
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (isNotBlank(data)) {
                if (data.msg == "success") {
                    $("tr[id='" + row + "']").remove();//删除当前行
                    i = i - 2;
                    $("#sqrqk").attr("rowspan", sqrQk(i));
                    changFbcz();
                }
            }
        },
        error: function (data) {
            $.Prompt("删除失败",1500);
        }
    });
}

//重定义申请人情况合并行数
function sqrQk(i) {
    return parseInt($("#rowsize").val()) + i;
}
//初始化一些监听事件
function changelisten() {
    //验证身份证证件号合法性
    $("input[name='qlrzjh']").change(function () {
        var index = $(this).attr('id');
        var y = index.indexOf("_");
        var id = index.substr(y + 1);
        var qlrsfzjzl = $("#qlrsfzjzl_" + id).val();
        var sId = $("#qlrzjh_" + id).val();
        if (qlrsfzjzl == 1) {
            var msg = ValidateIdCard(sId);
            if (msg != null) {
                $.Prompt(msg,1500);
            }
        }
    });
    //填写共有比例计算权利人共有比例是否总计为1
    $("input[name='qlbl']").change(function () {
        var gybl = $(this).val();
        var msg = ValidateGybl(gybl);
        if (msg != null) {
            $.Prompt(msg,1500);
        }
        else {
            changGyfs();
        }
    });
    ///计算权利人数量并对分别持证进行更改
    changFbcz();
    //验证共有方式
    var gyfsArr=new Array();
    $("select[name='qlrGyfs']").each(function (i, n) {
        var errorMsg="共有方式错误";
        var gyfs = $(n).val();
        var qlrSize=$("#bdcQlrSize").val();
        if(qlrSize<2&&gyfs==2){
            showError(n,errorMsg);
        }
        if(qlrSize==2){
            var gyfsObj=new Object();
            gyfsObj.id=n;
            gyfsObj.gyfs=gyfs;
            gyfsArr.push(gyfsObj);
        }
    });
    checkGyfs(gyfsArr);
}
function checkGyfs(gyfsArr){
    var errorMsg="共有方式错误";
    if(gyfsArr.length>0&&(gyfsArr[0].gyfs!=gyfsArr[1].gyfs)){
        for(var i=0;i<gyfsArr.length;i++){
            showError(gyfsArr[i].id,errorMsg);
        }
    }
}

function showError(n,errorMsg){
    var qlrid=n.id.split('_')[1];
    $("#error_"+qlrid).css("background-color", " #f2dede");
    $("#error_"+qlrid).css("border-color", "#ebccd1");
    $("#error_"+qlrid).css("border", "1px solid transparent");
    $("#error_"+qlrid).css("border-radius", "4px");
    $("#error_"+qlrid).css("width", "80px");
    $("#error_"+qlrid).css("color", " #c33");
    $("#error_"+qlrid).css("display", "inline-block");
    $("#error_"+qlrid).html(errorMsg);
}





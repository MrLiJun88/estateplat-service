/**
 * Created by jhj on 2017/5/17.
 */

$(document).ready(function () {
    //重定义行
    $("#sqrqk").attr("rowspan", sqrQk(i));
    changelisten();
});

//保存前验证共有方式，持证方式,共有比例是否为1
function beforeSave() {
    $("input[name='fbcz']").each(function (i, n) {
        var qlrid = $(n).val();
        var qlbl = $("#qlbl_" + qlrid).val();
        if (qlbl.replace(/\s/g, "") != "") {
            var msg = ValidateGybl(qlbl);
            if (msg != null || msg != undefined) {
                return msg;
            }
        }
    });
    var msggyfs = changGyfs();
    if (msggyfs != null || msggyfs != undefined) {
        return msggyfs;
    }
    var sqfbcz = $("input[name='sqfbcz']:checked").val();
    var gyfs = $("input[name='gyfs']:checked").val();
    var a = 0;
    $("input[name='fbcz']").each(function (i, n) {
        ++a;
    });
    if (a == 1 && sqfbcz == "是") {
        return "持证方式选择错误！";
    }
    if (a != 1 && gyfs == 0) {
        return "共有方式选择错误！";
    }
}

//验证共有比例是否为整数
function ValidateGybl(gybl) {
    var gz = /^0+(\.\d{1,2})?$/;
    if (!gz.test(gybl)) {
        return "请正确输入以0开头的两位小数！";
    }
}

function changelisten() {
    //验证权利人身份证证件号合法性
    $("input[name='qlrzjh']").change(function () {
        var index = $(this).attr('id');
        var y = index.lastIndexOf("_");
        var id = index.substr(y + 1);
        var qlrsfzjzl = $("#qlr_qlrsfzjzl_" + id).val();
        var sId = $("#qlr_qlrzjh_" + id).val();
        if (qlrsfzjzl == 1) {
            var msg = ValidateIdCard(sId);
            if (msg != null) {
                $.Prompt(msg,1500);
            }
        }
    });
    //验证义务人身份证证件号合法性
    $("input[name='ywrzjh']").change(function () {
        var index = $(this).attr('id');
        var y = index.lastIndexOf("_");
        var id = index.substr(y + 1);
        var qlrsfzjzl = $("#qlr_qlrsfzjzl_" + id).val();
        var sId = $("#qlr_qlrzjh_" + id).val();
        if (qlrsfzjzl == 1) {
            var msg = ValidateIdCard(sId);
            if (msg != null) {
                $.Prompt(msg,1500);
            }
        }
    });

    //集成身份证读卡器qlrmc
    $("input[name='qlrmc']").dblclick(function () {
        var index = $(this).attr('id');
        var y = index.lastIndexOf("_");
        var id = index.substr(y + 1);
        var qlrzjh = "qlr_qlrzjh_" + id;
        ReadIDCardNew(index, qlrzjh);
    });

    //填写共有比例计算权利人共有比例是否总计为1
    $("input[name='qlbl']").change(function () {
        var gybl = $(this).val();
        var msg = ValidateGybl(gybl);
        if (msg != null) {
            $.Prompt(msg,1500);
            return false;
        }
        var msggyfs = changGyfs();
        if (msggyfs != null) {
            $.Prompt(msggyfs,1500);
        }
    });
    ///计算权利人数量并对分别持证进行更改
    changFbcz();
    limGyfs();
}

function changGyfs() {
    var blnumber = 0;
    $("input[name='fbcz']").each(function (i, n) {
        var qlrid = $(n).val();
        var qlbl = $("#qlbl_" + qlrid).val();
        if (qlbl.replace(/\s/g, "") == "") {
            qlbl = 0;
        }
        else {
            $("#afgy").attr("checked", "checked");
        }
        blnumber = blnumber + parseFloat(qlbl);
    });
    if (blnumber != 1 && blnumber != 0) {
        return "权利人比例之和不为1！";
    }
}
//计算权利人数量并对分别持证进行更改
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

//计算权利人数量并对共有方式进行权限限制
function limGyfs() {
    var a = 0;
    $("input[name='fbcz']").each(function (i, n) {
        ++a;
    });
    if (a == 1) {
        $("[name='gyfs']").attr("disabled", true);
    }
    else {
        $("[name='gyfs']").attr("disabled", false);
    }
}

//重定义申请人情况合并行数
function sqrQk(i) {
    return parseInt($("#rowsize").val()) + i;
}

/**
 * Created by jhj on 2017/5/23.
 */
//保存按钮
function saveBdcSqsxx() {
    var arry = [];
    var a = 1;
    $.blockUI({message: "请稍等......"});
    $("input[name='qlrid']").each(function (i, n) {
        var id = $(n).val();
        var o = new Object();
        o['qlrid'] = $(n).val();
        o['proid'] = $("#proid").val();
        o['qlrlx'] = $("#qlrlx_" + id).val();
        o['qlbl'] = $("#qlbl_" + id).val();
        o['qlrmc'] = $("#qlrmc_" + id).val();
        o['qlrsfzjzl'] = $("#qlrsfzjzl_" + id).val();
        o['qlrzjh'] = $("#qlrzjh_" + id).val();
        o['qlrtxdz'] = $("#qlrtxdz_" + id).val();
        o['qlryb'] = $("#qlryb_" + id).val();
        o['qlrfddbr'] = $("#qlrfddbr_" + id).val();
        o['qlrfddbrdh'] = $("#qlrfddbrdh_" + id).val();
        o['qlrdlr'] = $("#qlrdlr_" + id).val();
        o['qlrdlrdh'] = $("#qlrdlrdh_" + id).val();
        o['qlrdljg'] = $("#qlrdljg_" + id).val();
        o['qlrdlrzjzl'] = $("#qlrdlrzjzl_" + id).val();
        o['qlrdlrzjh'] = $("#qlrdlrzjh_" + id).val();
        o['gyfs'] = $("#gyfs_" + id).val();
        o['qygyr'] = $("#qygyr_" + id).val();
        o['sxh'] = a;
        ++a;
        arry.push(o);
    });
    var s = JSON.stringify(arry);
    $.ajax({
        url: bdcdjUrl+"/bdcdjSqsxx/saveBdcSqsxx",
        type: 'POST',
        data: $("#sqsForm").serialize() + '&' + $.param({s: s}),
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (isNotBlank(data)) {
                if (data.msg == "success") {
                    window.location.reload();
                    $.Prompt("保存成功",1500);
                }
            }
        },
        error: function (data) {
            $.Prompt("保存失败",1500);
        }
    });
}

$(document).ready(function () {
    //重定义行
    $("#sqrqk").attr("rowspan", sqrQk(i));
    changelisten();
    initHistoryZd();
    addBorderToDiffHistoryZd();
});

//权利人增加行
function qlrAdd() {
    var proid =$("#proid").val();
    var qlrSize=$("#bdcQlrSize").val();
    $.ajax({
        url: bdcdjUrl+"/bdcdjSqsxx/addQlr",
        type: 'POST',
        data: {proid: proid, qlrlx: 'qlr',bdcQlrSize:qlrSize},
        success: function (data) {
            if(data){
                setTimeout($.unblockUI, 10);
                window.location.reload();
            }
        },
        error: function (jqXHR, exception) {
            $.Prompt("添加权利人失败",1500);
        }
    });
}
//义务人增加行
function ywrAdd() {
    var proid =$("#proid").val();
    var ywrSize=$("#bdcYwrSize").val();
    $.ajax({
        url: bdcdjUrl+"/bdcdjSqsxx/addQlr",
        type: 'POST',
        async:false,
        data: {proid: proid, qlrlx: 'ywr',bdcQlrSize:ywrSize},
        success: function (data) {
            if(data){
                setTimeout($.unblockUI, 10);
                window.location.reload();
            }
        },
        error: function (jqXHR, exception) {
            $.Prompt("添加义务人失败",1500);
        }
    });
}

//删除行
function qlrDel(o) {
    var row = "tr_" + o;
    $.blockUI({message: "请稍等......"});
    $.ajax({
        url: bdcdjUrl+"/bdcdjSqsxx/delBdcQlr",
        type: 'POST',
        dataType: 'json',
        data: $.param({s: o}),
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (isNotBlank(data)) {
                if (data.msg == "success") {
                    setTimeout($.unblockUI, 10);
                    window.location.reload();
                }
            }
        },
        error: function (data) {
            $.Prompt("删除失败",1500);
        }
    });
}

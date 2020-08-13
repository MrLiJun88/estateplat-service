/**
 * Created by songhaowen on 2017/5/16.
 */
//保存按钮
function saveBdcSqsxx() {
    var msgarry = [];//放入验证消息
    $("input[name='qlrzjh']").each(function (i, n) {
        var index = $(n).attr('id');
        var y = index.lastIndexOf("_");
        var id = index.substr(y + 1);
        var qlrsfzjzl = $("#qlrsfzjzl_" + id).val();
        var sId = $("#qlrzjh_" + id).val();
        if (qlrsfzjzl == 1) {
            var zjmsg = ValidateIdCard(sId);
            if (zjmsg != null && zjmsg != undefined) {
                msgarry.push(zjmsg);
            }
        }
    });
    if (msgarry != null && msgarry != undefined && msgarry.length > 0) {
        $.Prompt(msgarry[0],1500);
        return false;
    }
    var ms = beforeSave();
    if (ms != null && ms != undefined) {
        $.Prompt(ms,1500);
        return false;
    }
    save();
}
function save(){
    var arry = []
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
        o['sxh'] = a;
        ++a;
        arry.push(o);
    });
    var s = JSON.stringify(arry);
    $.ajax({
        url: bdcdjUrl+"/bdcdjBatchSqsxx/saveBdcSqsxx",
        type: 'POST',
        dataType: 'json',
        data: $("#sqsForm").serialize() + '&' + $.param({s: s}),
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (isNotBlank(data)) {
                if (data.msg == "success") {
                    $.Prompt("保存成功",1500);
                    window.location.reload();
                }
            }
        },
        error: function (data) {
            $.Prompt("保存失败",1500);
        }
    });
}

//申请书打印
function printSqs() {
    var proid = $("#proid").val();
    var url = reportUrl + "/ReportServer?reportlet=print%2Fbdc_fdcq_sqs_pl.cpt&op=write&proid=" + proid;
    showIndexModel(url, "打印申请书", 1000, 550, false);
}
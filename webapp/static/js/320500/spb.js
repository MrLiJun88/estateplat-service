/**
 * Created by jhj on 2017/5/23.
 */
//保存按钮
function saveBdcSpbxx() {
    var arry=new Array();
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
        o['sxh'] = a;
        ++a;
        arry.push(o);
    });
    var s = JSON.stringify(arry);
    $.ajax({
        url: bdcdjUrl+"/bdcdjSpbxx/saveBdcSpbxx",
        type: 'POST',
        dataType: 'json',
        data: $("#spbForm").serialize() + '&' + $.param({s: s}),
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (isNotBlank(data)) {
                if (data.msg == "success") {
                    //保存签名
                    $.ajax({
                        url: "${bdcdjUrl}/sign/updateSignOpinon",
                        type: 'POST',
                        dataType: 'json',
                        data: $("#spbForm").serialize(),
                        success: function () {
                            $.Prompt("保存成功",1500);
                            window.location.reload();
                        },
                        error: function () {
                            window.location.reload();
                        }
                    });
                }
            }
        },
        error: function (data) {
            $.Prompt("保存失败",1500);
        }
    });
}

$(document).ready(function () {
    $("#sqrqk").attr("rowspan", sqrQk(i));
    changelisten();
    initHistoryZd();
    addBorderToDiffHistoryZd();
});

//权利人增加行
function qlrAdd() {
    var row_num = "tr_" + e;
    var tr = '<tr id="' + row_num + '" >'
        + '<input type="hidden" id="fbcz" name="fbcz" value="' + e + '" />'
        + '<input type="hidden" id="qlrid" name="qlrid" value="' + e + '" />'
        + '<input type="hidden" id="qlrlx_' + e + '"  name="qlrlx" value="qlr"/>'
        + '<th   style="text-align:center; background: #EEEEEE;"  colspan="3" >'
        + '<label name="">权利人姓名（名称）</label></th>'
        + '<td colspan="3">'
        + '<input type="text" id="qlrmc_' + e + '" name="qlrmc" value="" /></td>'
        + '<th   style="text-align:center; background: #EEEEEE;"  colspan="2" >'
        + '<label name="">共有比例</label></th>'
        + '<td colspan="2" style="border-right-style:none;">'
        + '<input type="text" id="qlbl_' + e + '" name="qlbl" value="" /></td>'
        + '<td colspan="1" style="border-left-style:none;">'
        + '<a  onclick="qlrDel(' + e + ')"><i class="ace-icon glyphicon glyphicon-remove"></i></a> '
        + '</td></tr>'
        + '<tr id="' + row_num + '" >'
        + '<th   style="text-align:center; background: #EEEEEE;"  colspan="3" >'
        + '<label name="">身份证件种类</label></th>'
        + '<td colspan="3">'
        + '<select name="qlrsfzjzl" id="qlrsfzjzl_' + e + '"> <option value="1" >身份证</option> <option value="2">港澳台身份证</option> <option value="3">护照</option><option value="4">户口簿</option><option value="5">军官证（士兵证</option><option value="6">组织机构代码</option><option value="7">营业执照</option><option value="8">其它</option></select>'
        + '<th   style="text-align:center; background: #EEEEEE;"  colspan="2" >'
        + '<label name="">证件号</label></th>'
        + '<td colspan="3">'
        + '<input type="text" id="qlrzjh_' + e + '" name="qlrzjh" value="" />'
        + '</td></tr>';
    $("#tr_qlr").before(tr);//确定增加行数的位置
    i = i + 2;
    ++e;
    $("#sqrqk").attr("rowspan", sqrQk(i));
    initSpb();
}
//义务人增加行
function ywrAdd() {
    var row_num = "tr_" + e;
    var tr = '<tr id="' + row_num + '" >'
        + '<input type="hidden" id="qlrid" name="qlrid" value="' + e + '" />'
        + '<input type="hidden" id="qlrlx_' + e + '" name="qlrlx" value="ywr" />'
        + '<th   style="text-align:center; background: #EEEEEE;"  colspan="3" >'
        + '<label name="">义务人姓名（名称）</label></th>'
        + '<td colspan="3">'
        + '<input type="text" id="qlrmc_' + e + '" name="qlrmc" value="" /></td>'
        + '<th   style="text-align:center; background: #EEEEEE;"  colspan="2" >'
        + '<label name="">共有比例</label></th>'
        + '<td colspan="2" style="border-right-style:none;">'
        + '<input type="text" id="qlbl_' + e + '" name="qlbl" value=" " /></td>'
        + '<td colspan="1" style="border-left-style:none;">'
        + '<a  onclick="qlrDel(' + e + ')"><i class="ace-icon glyphicon glyphicon-remove"></i></a> '
        + '</td></tr>'
        + '<tr id="' + row_num + '" >'
        + '<th   style="text-align:center; background: #EEEEEE;"  colspan="3" >'
        + '<label name="">身份证件种类</label></th>'
        + '<td colspan="3">'
        + '<select name="qlrsfzjzl" id="qlrsfzjzl_' + e + '"> <option value="1" >身份证</option> <option value="2">港澳台身份证</option> <option value="3">护照</option><option value="4">户口簿</option><option value="5">军官证（士兵证</option><option value="6">组织机构代码</option><option value="7">营业执照</option><option value="8">其它</option></select>'
        + '<th   style="text-align:center; background: #EEEEEE;"  colspan="2" >'
        + '<label name="">证件号</label></th>'
        + '<td colspan="3">'
        + '<input type="text" id="qlrzjh_' + e + '" name="qlrzjh" value="" />'
        + '</td></tr>';
    $("#tr_ywr").before(tr);//确定增加行数的位置
    i = i + 2;
    ++e;
    $("#sqrqk").attr("rowspan", sqrQk(i));
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
                    $("tr[id='" + row + "']").remove();//删除当前行
                    i = i - 6;
                    $("#sqrqk").attr("rowspan", sqrQk(i));
                    changFbcz();
                    limGyfs();
                }
            }
        },
        error: function (data) {
            $.Prompt("删除失败",1500);
        }
    });
}
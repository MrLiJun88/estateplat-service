<@com.html title="土地分割信息录入" import="ace,public,init">
<script>
    var bdcdjUrl = "${bdcdjUrl!}";
    var proid = "${proid!}";
    e = 2;
    function bdcSplit() {
        $.blockUI({message: "请稍等......"});
        var arry = [];
        var tdmj = 0;
        $("input[name='tdmj']").each(function (i, n) {
            var o = new Object();
            ++i;
            o['tdmj'] = $("#tdmj_" + i).val();
            o['tdzl'] = $("#tdzl_" + i).val();
            arry.push(o);
            tdmj = tdmj + parseFloat($("#tdmj_" + i).val());
        });
        if (tdmj != ${bdcSpxx.zdzhmj!}) {
            setTimeout($.unblockUI, 10);
            tipInfo("分割面积之和不等于总面积，请检查！");
        } else {
            var json = JSON.stringify(arry);
            $.ajax({
                url: bdcdjUrl + "/bdcFghb/bdcSplit",
                type: 'POST',
                dataType: 'json',
                async: 'false',
                data: {json:json,proid:proid},
                success: function (data) {
                    setTimeout($.unblockUI, 10);
                    if (data != null && data != "" && data.msg == "success") {
                        showFghbCkxx(data.proid);
                    } else {
                        tipInfo("分割失败!");
                    }
                },
                error: function (data) {
                    setTimeout($.unblockUI, 10);
                    tipInfo("分割失败!");
                }
            });
        }
    }

    function showFghbCkxx(proid) {
        $.ajax({
            url: bdcdjUrl + "/BdcGdxxMul/getCqWiidByProid?proid=" + proid,
            type: 'POST',
            dataType: 'json',
            async: 'false',
            data: {proids: proid},
            success: function (data) {
                if (data != null && data != "") {
                    window.open(bdcdjUrl + "/bdcFghb/ckxx?wiid=" + data.wiid);
                }
            },
            error: function (data) {
                tipInfo("error!");
            }
        });
    }

    //删除行
    function del(i) {
        var row = "tr_" + i;
        $("tr[id='" + row + "']").remove();//删除当前行
    }

    //增加行
    function add() {
        var $tr = $('<tr  id=\"tr_' + e + '\"></tr>');

        var $thTdmj = $('<th colspan=\"1\" style=\"text-align:right\"></th>');
        $thTdmj.html('<label>拆分土地面积：</label>');
        var $tdTdmj = $('<td colspan=\"1\" ></td>');
        $tdTdmj.html('<input type=\"text\" id=\"tdmj_' + e + '\" name=\"tdmj\" value=\"\" />');

        var $thTdzl = $('<th colspan=\"1\" style="text-align:right"></th>');
        $thTdzl.html('<label>拆分土地坐落：</label>');
        var $tdTdzl = $('<td colspan=\"1\" ></td>');
        $tdTdzl.html('<input type=\"text\" id=\"tdzl_' + e + '\" name=\"tdzl\" value=\"\" />');

        var $thDelBtn = $('<th colspan=\"1\" style=\"border-left-style:none;text-align:center"></th>');
        $thDelBtn.html('<a name=\"del\" id=\"del\" onclick=\"del(' + e + ')\"><i  class=\"ace-icon glyphicon glyphicon-remove\"></i></a>');

        $tr.append($thTdmj);
        $tr.append($tdTdmj);
        $tr.append($thTdzl);
        $tr.append($tdTdzl);
        $tr.append($thDelBtn);
        $("#tr_end").before($tr);
        ++e;
    }
</script>
<div class="main-container">
    <div id="formBody" class="formBody" style="margin-top: 50px">
        <div class="page-content" style="margin-top: 50px">
            <@f.form id="tdSplitForm" name="tdSplitForm">
                <button type="button" class="btn btn-primary save" onclick="bdcSplit()">确定</button>
                <@f.hidden id="proid" name="proid" value="${proid!}"/>
                    <@f.table>
                    <@f.tr style="border:none;height:0px;">
                        <@f.td style="border:none;height:0px;width:100px;"></@f.td>
                        <@f.td style="border:none;height:0px;width:230px;"></@f.td>
                        <@f.td style="border:none;height:0px;width:100px;"></@f.td>
                        <@f.td style="border:none;height:0px;width:330px;"></@f.td>
                        <@f.td style="border:none;height:0px;width:30px;"></@f.td>
                    </@f.tr>
                    <@f.tr>
                        <@f.th colspan='1' style="text-align:right">
                            <@f.label class="leftLabel" name="原土地总面积："></@f.label>
                        </@f.th>
                        <@f.td colspan='1'>
                            <@f.text id="zdzhmj" name="zdzhmj" value="${bdcSpxx.zdzhmj!}" readonly="true"></@f.text>
                        </@f.td>
                        <@f.th colspan='1' style="text-align:right">
                            <@f.label class="leftLabel" name="原土地坐落："></@f.label>
                        </@f.th>
                        <@f.td colspan='1'>
                            <@f.text id="zl" name="zl" value="${bdcSpxx.zl!}" readonly="true"></@f.text>
                        </@f.td>
                        <@f.th colspan="1" style="border-left-style:none;text-align:center">
                            <a  id="add" name="add" onclick="add()"><i class="ace-icon glyphicon glyphicon-plus"></i></a>
                        </@f.th>
                    </@f.tr>
                        <@f.tr style="height:30px"></@f.tr>
                    <@f.tr id = "tr_1">
                        <@f.th colspan='1' style="text-align:right">
                            <@f.label class="leftLabel" name="拆分土地面积："></@f.label>
                        </@f.th>
                        <@f.td colspan='1'>
                            <@f.text id="tdmj_1" name="tdmj"></@f.text>
                        </@f.td>
                        <@f.th colspan='1' style="text-align:right">
                            <@f.label class="leftLabel" name="拆分土地坐落："></@f.label>
                        </@f.th>
                        <@f.td colspan='1'>
                            <@f.text id="tdzl_1" name="tdzl"></@f.text>
                        </@f.td>
                        <@f.th colspan="1" style="border-left-style:none;text-align:center;">
                            <a id="del" name="del" onclick="del('1')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
                        </@f.th>
                    </@f.tr>
                    <@f.tr id="tr_end"></@f.tr>
                    </@f.table>
            </@f.form>
        </div>
    </div>
</div>
</@com.html>
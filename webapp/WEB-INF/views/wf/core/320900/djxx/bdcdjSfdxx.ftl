<@com.html title="不动产登记业务管理系统" import="ace,public,init">
    <#include "../../../common/rightsManagement.ftl">
    <#include "../../../common/fieldColorManagement.ftl">
    <@script name="static/js/wf.js"></@script>
<script type="text/javascript">
    var bdcdjUrl = "${bdcdjUrl!}";
    var reportUrl = "${reportUrl!}";
    var wiid ="${wiid!}";
    var etlUrl = "${etlUrl!}";
    var portalUrl = "${portalUrl!}";
    var platformUrl="${path_platform!}"
    var analysisUrl ="${analysisUrl!}";
    var taskid ="${taskid!}";
    var from="${from!}";
    var rid="${rid!}";
    var proid = "${proid!}";
    e = 1;
    function getID(name) {
        var y = name.indexOf("_");
        var id = name.substr(y + 1);
        return id;
    }
    function saveSfxx() {
        debugger;
        $.blockUI({message: "请稍等……"});
        var arry = [];
        $("input[name='sfxmid']").each(function (i, n) {
            var id = $(n).val();
            var o = new Object();
            o['sfxmid'] = $(n).val();
            o['sfxmmc'] = $("#sfxmmc_" + id).val();
            o['sl'] = $("#sl_" + id).val();
            o['je'] = $("#je_" + id).val();
            o['bz'] = $("#bz_" + id).val();
            o['dw'] = $("#dw_" + id).val();
            o['qlrlx'] = $("#qlrlx_" + id).val();
            o['sfxxid'] = $("#sfxxid").val();
            arry.push(o);
        });
        var s = JSON.stringify(arry);
        $.ajax({
            url: "${bdcdjUrl}/bdcdjSfdxx/saveSfxx",
            type: 'POST',
            dataType: 'json',
            data: $("#sfxxForm").serialize() + '&' + $.param({s: s}),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        alert("保存成功");
                        window.location.reload();
                    }
                }
            },
            error: function (data) {
                alert("保存失败");
            }
        });
    }
    function getValue(sfxmmc, bz, jzmj, sl) {
        var value = bz * sl;
        if (sfxmmc != "土地登记费" || bz != "他项抵押/出租(个人)" || bz != "他项抵押/出租(企业)") {
            value = bz * sl;
        }
        if (sfxmmc == "土地登记费" && bz == "党政机关,人民团体,全额预算事业单位") {
            value = 10 * sl;
        }
        if (sfxmmc == "土地登记费" && bz == "企业,自收自支事业单位" && jzmj <= 1000) {
            value = 110 * sl;
        }
        if (sfxmmc == "土地登记费" && bz == "企业,自收自支事业单位" && jzmj > 1000) {
            if ((jzmj - 1000) / 500 <= 1) {
                var value = sl * 110 + sl * 40
            } else {
                var a = (jzmj - 1000) / 500;
                var b = Math.ceil(a);
                value = sl * 110 + b * 40 * sl;
                var je = sl * 110 + b * 40 * sl;
                if (je > 40000) {
                    value = 40000;
                }
            }
        }
        if (sfxmmc == "土地登记费" && bz == "差额预算事业单位" && jzmj <= 2500) {
            value = 160 * sl;
        }
        if (sfxmmc == "土地登记费" && bz == "差额预算事业单位" && jzmj > 2500) {
            if ((jzmj - 2500) / 500 <= 1) {
                value = sl * 160 + sl * 20
            } else {
                var a = (jzmj - 2500) / 500;
                var b = Math.ceil(a);
                value = sl * 160 + b * 20 * sl;
                var je = sl * 160 + b * 20 * sl;
                if (je > 10000) {
                    value = 10000;
                }
            }
        }
        if (sfxmmc == "土地登记费" && bz == "个人住宅" && jzmj <= 100) {
            value = 18 * sl;
        }
        if (sfxmmc == "土地登记费" && bz == "个人住宅" && jzmj > 100) {
            if ((jzmj - 100) / 50 <= 1) {
                value = sl * 18 + sl * 5
            } else {
                var a = (jzmj - 100) / 50;
                var b = Math.ceil(a);
                value = sl * 18 + b * 5 * sl;
                var je = sl * 18 + b * 5 * sl;
                if (je > 35) {
                    value = 35;
                }
            }
        }
        if (sfxmmc == "土地登记费" && bz == "私房用于生产经营" && jzmj <= 500) {
            value = 90 * sl;
        }
        if (sfxmmc == "土地登记费" && bz == "私房用于生产经营" && jzmj > 500) {
            if ((jzmj - 500) / 100 <= 1) {
                value = sl * 90 + sl * 20
            } else {
                var a = (jzmj - 500) / 100;
                var b = Math.ceil(a);
                value = sl * 90 + b * 20 * sl;
                var je = sl * 90 + b * 20 * sl;
                if (je > 200) {
                    value = 200;
                }
            }
        }
        if (sfxmmc == "土地登记费" && bz == "他项抵押/出租(企业)" && jzmj <= 1000) {
            value = 50 * sl;
        }
        if (sfxmmc == "土地登记费" && bz == "他项抵押/出租(企业)" && jzmj > 1000) {
            if ((jzmj - 1000) / 500 <= 1) {
                value = sl * 50 + sl * 20
                var je = sl * 50 + b * 20 * sl;
            } else {
                var a = (jzmj - 1000) / 500;
                var b = Math.ceil(a);
                value = sl * 50 + b * 20 * sl;
                var je = sl * 50 + b * 20 * sl;
            }
            if (je > 200) {
                value = 200;
            }
        }
        if (sfxmmc == "土地登记费" && bz == "他项抵押/出租(个人)" && jzmj <= 100) {
            value = 18 * sl;
        }
        if (sfxmmc == "土地登记费" && bz == "他项抵押/出租(个人)" && jzmj > 100) {
            if ((jzmj - 100) / 50 <= 1) {
                value = sl * 18 + sl * 5
                var je = sl * 18 + b * 5 * sl;
            } else {
                var a = (jzmj - 100) / 50;
                var b = Math.ceil(a);
                value = sl * 18 + b * 5 * sl;
                var je = sl * 18 + b * 5 * sl;
            }
            if (je > 35) {
                value = 35;
            }
        }
        return value;
    }
    function selectChange() {
        $("select[name='sfxmmc']").change(function () {
            debugger;
            var index = $(this).attr('id');
            var id = getID(index);
            var sfxmmc = $("#sfxmmc_" + id).val();
            var jzmj = $("#jzmj").val();
            var sl = $("#sl_" + id).val();
            var qlrlx = $("#qlrlx_" + id).val();
            $.ajax({
                url: "${bdcdjUrl}/bdcdjSfdxx/getBzAndDw?sfxmmc=" + sfxmmc + "&proid=" + proid,
                type: 'POST',
                dataType: 'json',
                success: function (data) {
                    if (data != null && data != "") {
                        $("#bz_" + id).val(data.qlrbz);
                        $("#dw_" + id).val(data.qlrdw);
                        var bz = $("#bz_" + id).val();
                        $("#je_" + id).val(getValue(sfxmmc, bz, jzmj, sl));
                        //初始化总计金额
                        countZj(qlrlx);
                    }
                },
            });
        });
//        sl改变时重新计算缴费金额
        $("input[name='mrsl']").change(function () {
            var index = $(this).attr('id');
            var id = getID(index);
            var sfxmmc = $("#sfxmmc" + id).val();
            var jzmj = $("#jzmj").val();
            var sl = $("#sl_" + id).val();
            var bz = $("#bz_" + id).val();
            var qlrlx = $("#qlrlx_" + id).val();
            $("#je_" + id).val(getValue(sfxmmc, bz, jzmj, sl));
            //初始化总计金额
            countZj(qlrlx);
        });
        //qlrsfxmbz改变时重新计算缴费金额
        $("select[name='bz']").change(function () {
            var index = $(this).attr('id');
            var id = getID(index);
            var sfxmmc = $("#sfxmmc_" + id).val();
            var jzmj = $("#jzmj").val();
            var sl = $("#sl_" + id).val();
            var bz = $("#bz_" + id).val();
            var qlrlx = $("#qlrlx_" + id).val();
            $("#je_" + id).val(getValue(sfxmmc, bz, jzmj, sl));
            //初始化总计金额
            countZj(qlrlx);
        });
        //权利人je改变时重新计算缴费金额
        $("input[name='je_qlr']").change(function () {
            //初始化总计金额
            countZj("qlr");
        });
        //义务人je改变时重新计算缴费金额
        $("input[name='je_ywr']").change(function () {
            //初始化总计金额
            countZj("ywr");
        });
    }
    $(document).ready(function () {
        selectChange();
        //初始化权利人总计金额
        countZj("qlr");
        //初始化ywr总计金额
        countZj("ywr");
    });
    //重新计算对应的总计金额
    function countZj(qlrlx) {
        var sum = 0;
        $("input[name='je_" + qlrlx + "']").each(function (i, n) {
            debugger;
            var r = /^-?\d+$/;　//正整数
            if ($(this).val() != '' && !r.test($(this).val())) {
                $(this).val("");  //正则表达式不匹配置空
            } else if ($(this).val() != '') {
                sum += parseInt($(this).val());
            }
        });
        $("#zj_" + qlrlx).val(sum);
    }
    //增加权利人收费项目
    function qlrsfAdd() {
        debugger;
        var row_num = "tr_" + e;
        var tr = '<tr id="' + row_num + '" >'
                + '<input type="hidden" id="sfxmid" name="sfxmid" value="' + e + '" />'
                + '<input type="hidden" id="qlrlx_' + e + '"  name="qlrlx" value="qlr"/>'
                + '<td><select  id="sfxmmc_' + e + '" name="sfxmmc"  showFieldName="mc" valueFieldName="mc" source="qlrxtsfxmmcList"defaultValue="" >'
                + '<#list qlrxtsfxmmcList as item> <option value="${item.mc!}">${item.mc!}</option></#list>'
                + '</select></td>'
                + '<td><select id="bz_' + e + '" name="sfxmbz"  showFieldName="SFXMBZ" valueFieldName="SFXMBZ" source="sfbzList"defaultValue="" >'
                + '<#list sfbzList as item> <option value="${item.SFXMBZ!}">${item.SFXMBZ!}</option></#list>'
                + '</select></td>'
                + '<td><input type="text" id="sl_' + e + '" name="mrsl" value="1" style="text-align:center" /></td>'
                + '<td><select id="dw_' + e + '" name="dw"  showFieldName="DW" valueFieldName="DW" source="sfdwList"defaultValue="" >'
                + '<#list sfdwList as item> <option value="${item.DW!}">${item.DW!}</option></#list>'
                + '</select></td>'
                + '<td colspan="2" style="border-right:none">'
                + '<input type="text" id="je_' + e + '" name="je_qlr" value=""  /></td>'
                + '<td style="border-left:none">'
                + '<a  onclick="sfDel(' + e + ')"><i class="ace-icon glyphicon glyphicon-remove"></i></a> '
                + '</td></tr>';
        ++e;
        $("#tr_qlr").before(tr);//确定增加行数的位置
        selectChange();
    }
    //增加义务人收费项目
    function ywrsfAdd() {
        debugger;
        var row_num = "tr_" + e;
        var tr = '<tr id="' + row_num + '" >'
                + '<input type="hidden" id="sfxmid" name="sfxmid" value="' + e + '" />'
                + '<input type="hidden" id="qlrlx_' + e + '"  name="qlrlx" value="ywr"/>'
                + '<td><select  id="sfxmmc_' + e + '" name="sfxmmc"  showFieldName="mc" valueFieldName="mc" source="ywrxtsfxmmcList"defaultValue="" >'
                + '<#list ywrxtsfxmmcList as item> <option value="${item.mc!}">${item.mc!}</option></#list>'
                + '</select></td>'
                + '<td><select id="bz_' + e + '" name="sfxmbz"  showFieldName="SFXMBZ" valueFieldName="SFXMBZ" source="ywrsfbzList"defaultValue="" >'
                + '<#list ywrsfbzList as item> <option value="${item.SFXMBZ!}">${item.SFXMBZ!}</option></#list>'
                + '</select></td>'
                + '<td><input type="text" id="sl_' + e + '" name="mrsl" value="1" style="text-align:center" /></td>'
                + '<td><select id="dw_' + e + '" name="dw"  showFieldName="DW" valueFieldName="DW" source="ywrsfdwList"defaultValue="" >'
                + '<#list ywrsfdwList as item> <option value="${item.DW!}">${item.DW!}</option></#list>'
                + '</select></td>'
                + '<td colspan="2" style="border-right:none">'
                + '<input type="text" id="je_' + e + '" name="je_ywr" value=""  /></td>'
                + '<td style="border-left:none">'
                + '<a  onclick="sfDel(' + e + ')"><i class="ace-icon glyphicon glyphicon-remove"></i></a> '
                + '</td></tr>';
        ++e;
        $("#tr_ywr").before(tr);//确定增加行数的位置
        selectChange();
    }
    //删除行
    function sfDel(o) {
        debugger;
        var row = "tr_" + o;
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: "${bdcdjUrl}/bdcdjSfdxx/delSfdXm",
            type: 'POST',
            dataType: 'json',
            data: $.param({s: o}),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        debugger;
                        $("tr[id='" + row + "']").remove();//删除当前行
//                      重新计算权利人总计金额
                        countZj("qlr");
                        //重新计算ywr总计金额
                        countZj("ywr");
//                        window.location.reload();
                    }
                }
            },
            error: function (data) {
                alert("删除失败");
//                    window.location.reload();
            }
        });
    }
</script>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-info save" onclick="saveSfxx()">保存</button>
    </div>
    <div class="rightToolTop">
        <div class='btn-group'>
            <a class='btn btn-success' class="dropdown-toggle" data-toggle="dropdown" onclick="#">打印</a>
            <#--<ul class='dropdown-menu'>-->
                <#--<li><a onclick="#">收费单 </a></li>-->
            <#--</ul>-->
        </div>
    </div>
</div>
<div class="main-container">
    <div class="space-10"></div>
    <@f.contentDiv title="不动产登记收费单">
        <@f.form id="sfxxForm" name="sfxxForm">
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
            <@f.hidden id="proid"  name="proid" value="${proid!}"/>
            <@f.hidden id="sfxxid"  name="sfxxid" value="${bdcSfxx.sfxxid!}"/>
            <@f.table style="width:650px">
                <@f.tr style="border:none;height:0px;">
                    <@f.td  style="border:none;height:0px;width:160px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:185px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:60px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:100px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:60px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:55px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:30px;"></@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.td colspan="2" style="border:none;">
                        <img src="${bdcdjUrl!}/bdcPrint/getEwm?proid=${proid}"style="width:70px;height:70px;"/>
                    </@f.td>
                    <@f.td colspan="5" style="border:none;height:0px;">
                </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="权利人姓名（名称）"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qlrmc" name="qlrmc" value="${comqlr!}"></@f.text>
                    </@f.td>
                    <@f.th colspan="5">
                        <@f.label name="土地面积"></@f.label>
                    </@f.th>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登记类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="sqlxmc" name="sqlxmc" value="${sqlxMc!}"></@f.text>
                    </@f.td>
                    <@f.th rowspan="3">
                        <@f.label name="其中"></@f.label>
                    </@f.th>
                    <@f.th>
                        <@f.label name="独用面积"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="dytdmj" name="dytdmj" value="${bdcFdcq.dytdmj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="坐落"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zl" name="zl" value="${bdcSpxx.zl!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="建筑面积"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="jzmj" name="jzmj" value="${bdcFdcq.jzmj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcdyh" name="bdcdyh" value="${bdcSpxx.bdcdyh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="宗地面积"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="zdzhmj" name="zdzhmj" value="${bdcSpxx.zdzhmj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="收费项目"></@f.label>
                    </@f.th>
                    <@f.th>
                        <@f.label name="标准"></@f.label>
                    </@f.th>
                    <@f.th>
                        <@f.label name="数量"></@f.label>
                    </@f.th>
                    <@f.th>
                        <@f.label name="单位"></@f.label>
                    </@f.th>
                    <@f.th colspan="2" style="border-right:none">
                        <@f.label name="应缴金额"></@f.label>
                    </@f.th>
                    <@f.th style="border-left:none">
                        <a onclick="qlrsfAdd()"><i class="ace-icon glyphicon glyphicon-plus"></i></a>
                    </@f.th>
                </@f.tr>
                <#if (qlrbdcSfxmList?size > 0)>
                    <#list qlrbdcSfxmList as qlrsf>
                        <@f.tr id="tr_${qlrsf.sfxmid!}">
                            <@f.hidden id="sfxmid_${qlrsf.sfxmid!}" name="sfxmid" value="${qlrsf.sfxmid!}"/>
                            <@f.hidden id="qlrlx_${qlrsf.sfxmid!}" name="qlrlx" value="qlr"/>
                            <@f.td>
                                <@f.select  id="sfxmmc_${qlrsf.sfxmid!}" name="sfxmmc"  showFieldName="mc" valueFieldName="mc" source="qlrxtsfxmmcList"defaultValue="${qlrsf.sfxmmc!}"  ></@f.select>
                            </@f.td>
                            <@f.td>
                                <@f.select id="bz_${qlrsf.sfxmid!}" name="bz"  showFieldName="SFXMBZ" valueFieldName="SFXMBZ" source="sfbzList"defaultValue="${qlrsf.bz!}" ></@f.select>
                            </@f.td>
                            <@f.td>
                                <@f.text id="sl_${qlrsf.sfxmid!}" name="mrsl" value="${qlrsf.sl!}" style="text-align:center" ></@f.text>
                            </@f.td>
                            <@f.td>
                                <@f.select   id="dw_${qlrsf.sfxmid!}" name="dw"  showFieldName="DW" valueFieldName="DW" source="sfdwList"defaultValue="${qlrsf.dw!}" ></@f.select>
                            </@f.td>
                            <@f.td colspan="2" style="border-right:none">
                                <@f.text id="je_${qlrsf.sfxmid!}" name="je_qlr" value="${qlrsf.je!}"  ></@f.text>
                            </@f.td>
                            <@f.td style="border-left:none">
                                <a onclick="sfDel('${qlrsf.sfxmid!}')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
                            </@f.td>
                        </@f.tr>
                    </#list>
                <#else>
                    <@f.tr id="tr_def1">
                        <@f.hidden id="sfxmid" name="sfxmid" value="def1"/>
                        <@f.hidden id="qlrlx_def1" name="qlrlx" value="qlr"/>
                        <@f.td>
                            <@f.select  id="sfxmmc_def1" name="sfxmmc"  showFieldName="mc" valueFieldName="mc" source="qlrxtsfxmmcList"defaultValue="" ></@f.select>
                        </@f.td>
                        <@f.td>
                            <@f.select id="bz_def1" name="bz"  showFieldName="SFXMBZ" valueFieldName="SFXMBZ" source="sfbzList"defaultValue="" ></@f.select>
                        </@f.td>
                        <@f.td>
                            <@f.text id="sl_def1" name="mrsl" value="1" style="text-align:center"></@f.text>
                        </@f.td>
                        <@f.td>
                            <@f.select   id="dw_def1" name="dw"  showFieldName="DW" valueFieldName="DW" source="sfdwList"defaultValue="" ></@f.select>
                        </@f.td>
                        <@f.td colspan="2" style="border-right:none">
                            <@f.text id="je_def1" name="je_qlr" value="" ></@f.text>
                        </@f.td>
                        <@f.td style="border-left:none">
                            <a onclick="sfDel('def1')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
                        </@f.td>
                    </@f.tr>
                </#if>
                <@f.tr id="tr_qlr">
                    <@f.th>
                        <@f.label name="合计"></@f.label>
                    </@f.th>
                    <@f.td colspan="6">
                        <@f.text id="zj_qlr" name="zj" value=""></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="义务人姓名（名称）"></@f.label>
                    </@f.th>
                    <@f.td colspan="6">
                        <@f.text id="qlrmc" name="qlrmc" value="${comywr!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="收费项目"></@f.label>
                    </@f.th>
                    <@f.th>
                        <@f.label name="标准"></@f.label>
                    </@f.th>
                    <@f.th>
                        <@f.label name="数量"></@f.label>
                    </@f.th>
                    <@f.th>
                        <@f.label name="单位"></@f.label>
                    </@f.th>
                    <@f.th colspan="2" style="border-right:none">
                        <@f.label name="应缴金额"></@f.label>
                    </@f.th>
                    <@f.th style="border-left:none">
                        <a onclick="ywrsfAdd()"><i class="ace-icon glyphicon glyphicon-plus"></i></a>
                    </@f.th>
                </@f.tr>
                <#if (ywrbdcSfxmList?size > 0)>
                    <#list ywrbdcSfxmList as ywrsf>
                        <@f.tr id="tr_${ywrsf.sfxmid!}">
                            <@f.hidden id="sfxmid_${ywrsf.sfxmid!}" name="sfxmid" value="${ywrsf.sfxmid!}"/>
                            <@f.hidden id="qlrlx_${ywrsf.sfxmid!}" name="qlrlx" value="ywr"/>
                            <@f.td>
                                <@f.select id="sfxmmc_${ywrsf.sfxmid!}" name="sfxmmc"  showFieldName="mc" valueFieldName="mc" source="ywrxtsfxmmcList"defaultValue="${ywrsf.sfxmmc!}" ></@f.select>
                            </@f.td>
                            <@f.td>
                                <@f.select id="bz_${ywrsf.sfxmid!}" name="bz"  showFieldName="SFXMBZ" valueFieldName="SFXMBZ" source="ywrsfbzList"defaultValue="${ywrsf.bz!}" ></@f.select>
                            </@f.td>
                            <@f.td>
                                <@f.text id="sl_${ywrsf.sfxmid!}" name="mrsl" value="${ywrsf.sl!}" style="text-align:center"></@f.text>
                            <#--<@f.select id="mrsl" name="mrsl"  showFieldName="sl" valueFieldName="sl" source="qlrxtsfxmmcList"defaultValue="" ></@f.select>-->
                            </@f.td>
                            <@f.td>
                                <@f.select id="dw_${ywrsf.sfxmid!}" name="dw"  showFieldName="DW" valueFieldName="DW" source="ywrsfdwList"defaultValue="${ywrsf.dw!}" ></@f.select>
                            </@f.td>
                            <@f.td colspan="2" style="border-right:none">
                                <@f.text id="je_${ywrsf.sfxmid!}" name="je_ywr" value="${ywrsf.je!}" ></@f.text>
                            </@f.td>
                            <@f.td style="border-left:none">
                                <a onclick="sfDel('${ywrsf.sfxmid!}')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
                            </@f.td>
                        </@f.tr>
                    </#list>
                <#else>
                    <@f.tr id="tr_def2">
                        <@f.hidden id="sfxmid" name="sfxmid" value="def2"/>
                        <@f.hidden id="qlrlx_def2" name="qlrlx" value="ywr"/>
                        <@f.td>
                            <@f.select  id="sfxmmc_def2" name="sfxmmc"  showFieldName="mc" valueFieldName="mc" source="qlrxtsfxmmcList"defaultValue="" ></@f.select>
                        </@f.td>
                        <@f.td>
                            <@f.select id="bz_def2" name="bz"  showFieldName="SFXMBZ" valueFieldName="SFXMBZ" source="sfbzList"defaultValue="" ></@f.select>
                        </@f.td>
                        <@f.td>
                            <@f.text id="sl_def2" name="mrsl" value="1" style="text-align:center"></@f.text>
                        </@f.td>
                        <@f.td>
                            <@f.select   id="dw_def2" name="dw"  showFieldName="DW" valueFieldName="DW" source="sfdwList"defaultValue="" ></@f.select>
                        </@f.td>
                        <@f.td colspan="2" style="border-right:none">
                            <@f.text id="je_def2" name="je_ywr" value="" ></@f.text>
                        </@f.td>
                        <@f.td style="border-left:none">
                            <a onclick="sfDel('def2')"><i class="ace-icon glyphicon glyphicon-remove"></i></a>
                        </@f.td>
                    </@f.tr>
                </#if>
                <@f.tr id="tr_ywr">
                    <@f.th>
                        <@f.label name="合计"></@f.label>
                    </@f.th>
                    <@f.td colspan="6">
                        <@f.text id="zj_ywr" name="zj" value=""></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="发票号"></@f.label>
                    </@f.th>
                    <@f.td colspan="6">
                        <@f.text id="fph" name="fph" value="${bdcSfxx.fph!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="备注"></@f.label>
                    </@f.th>
                    <@f.td colspan="6">
                        <@f.text id="bz" name="bz" value="${bdcSfxx.bz!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr style="border:none">
                    <@f.td  style="text-align:right; background: #FFFFFF;border:none"  colspan="4">
                        <@f.label name="签名"></@f.label>
                    </@f.td>
                    <@f.td style="border:none" colspan="3">
                        <@f.text id="sjr" name="sjr" value="${bdcSjxx.sjr!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr style="border:none">
                    <@f.td style="text-align:right; background: #FFFFFF;border:none" colspan="4">
                        <@f.label name="日期"></@f.label>
                    </@f.td>
                    <@f.td style="border:none" colspan="3">
                        <@f.date id="sjrq" name="sjrq" value="${sjrq!}"></@f.date>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <#--<@f.contentDiv>-->
        <#--<section id="sfxx">-->
            <#--<div class="row">-->
                <#--<div class="col-xs-2 demonstrative">-->
                    <#--收费明细列表：-->
                <#--</div>-->
                <#--<div class="col-xs-8 ">-->
                <#--</div>-->
                <#--<div class="rowLabel col-xs-2">-->
                <#--</div>-->
            <#--</div>-->
            <#--<@p.list tableId="sfxx-grid-table" pageId="sfxx-grid-pager" keyField="QLRID" dataUrl="${bdcdjUrl}/bdcdjSlxx/getSqrxxPagesJson?wiid=${wiid!}">-->
                <#--<@p.field fieldName="XH" header="序号" width="3%"/>-->
                <#--<@p.field fieldName="SFXM" header="收费项目" width="5%"/>-->
                <#--<@p.field fieldName="BZ" header="标准" width="5%"/>-->
                <#--<@p.field fieldName="SL" header="数量" width="5%"/>-->
                <#--<@p.field fieldName="DW" header="单位" width="5%"/>-->
                <#--<@p.field fieldName="JCJE" header="基础金额" width="5%"/>-->
                <#--<@p.field fieldName="SSJE" header="实收金额" width="5%"/>-->
                <#--<@p.field fieldName="CZ" header="操作" width="5%"  renderer="sqrxxCz"/>-->
            <#--</@p.list>-->
            <#--<table id="sfxx-grid-table"></table>-->
            <#--<div id="sfxx-grid-pager"></div>-->
        <#--</section>-->
    <#--</@f.contentDiv>-->
</div>
</@com.html>
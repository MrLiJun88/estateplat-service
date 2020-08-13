<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<#--<#include "../../../common/rightsManagement.ftl">-->
    <#include "../../../common/fieldColorManagement.ftl">
    <@script name="static/js/wf.js"></@script>
    <@script name="static/js/sign.js"></@script>
<script type="text/javascript" xmlns="http://www.w3.org/1999/html">
    i = 1;
    e = 1;
    var arry = [];
    var bdcdjUrl = "${bdcdjUrl!}";
    var reportUrl = "${reportUrl!}";
    var wiid = "${wiid!}";
    var etlUrl = "${etlUrl!}";
    var proid = "${proid!}";
    var portalUrl = "${portalUrl!}";
    var platformUrl = "${path_platform!}"
    var analysisUrl = "${analysisUrl!}";
    var taskid = "${taskid!}";
    var from = "${from!}";
    var rid = "${rid!}";
    //保存按钮                         f
    function saveBdcCfShbxx() {
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
            url: "${bdcdjUrl}/bdcdjSpbxx/saveBdcCfShbxx",
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
                                alert("保存成功");
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
                alert("保存失败");
            }
        });
    }
    function initSpb() {
        //验证身份证证件号合法性
        $("input[name='qlrzjh']").change(function () {
            debugger;
            var index = $(this).attr('id');
            var y = index.indexOf("_");
            var id = index.substr(y + 1);
            var qlrsfzjzl = $("#qlrsfzjzl_" + id).val();
            var sId = $("#qlrzjh_" + id).val();
            if (qlrsfzjzl == 1) {
                var msg = ValidateIdCard(sId);
                if (msg != null) {
                    alert(msg);
                }
            }
        });
        //填写共有比例计算权利人共有比例是否总计为1
        $("input[name='qlbl']").change(function () {
            var gybl = $(this).val();
            var msg = ValidateGybl(gybl);
            if (msg != null) {
                alert(msg);
            }
            else {
                changGyfs();
            }
        });
        //计算权利人数量并对分别持证方式进行更改
        changFbcz();
    }
    $(document).ready(function () {
        $("#sqrqk").attr("rowspan", sqrQk(i));
        initSpb();
        initHistoryZd();
    });

    function initHistoryZd(){
        $.ajax({
            url: "${bdcdjUrl}/bdcZddb/getHistoryZd",
            type: 'POST',
            data: {proid: '${proid!}'},
            success: function (data) {
                if (isNotBlank(data)) {
                    showHover(data)
                }
            },
            error: function (jqXHR, exception) {
                alert("获取字段对比失败");
            }
        });
    }

    function showHover(data){
        var zdmc=data.zdmc;
        var zdvalue=data.zdvalue;
        for(var i=0;i<zdmc.length;i++){
            var mc=String(zdmc[i]);
            $("#"+mc).css("border-color","red");
            $("#"+mc).attr("data-toggle","tooltip");
            $("#"+mc).attr("title","原权籍数据为："+zdvalue[""+mc]);
            $("#"+mc).tooltip();
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
            alert("权利人比例之和不为1！")
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
        debugger;
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

    //初审意见签名
    //    function csyjQm() {
    //        var proid = $("#proid").val();
    //        var url=bdcdjUrl+"/sign?proid=" + proid + "&wiid=" + wiid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
    //        showIndexModel(url,"初审意见签名",500,300,false);
    //    }
    //重定义申请人情况合并行数
    function sqrQk(i) {
        return parseInt($("#rowsize").val()) + i;
    }

    function openCfshp(){
        var url = reportUrl + "/ReportServer?reportlet=print%2Fbdc_cfshb.cpt&op=write&proid=" + proid;
        openWin(url);
    }


</script>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-info save" onclick="saveBdcCfShbxx()">保存</button>
    </div>
    <div class="rightToolTop">
        <div class='btn-group'>
            <a class='btn btn-success' class="dropdown-toggle" data-toggle="dropdown" onclick="openCfshp()">打印</a>
        <#--<ul class='dropdown-menu'>-->
        <#--<li><a onclick="openSpb()">审批表 </a></li>-->
        <#--</ul>-->
        </div>
    </div>
</div>
<div class="main-container">
    <@f.contentDiv  title="不动产查封登记审核表">
        <@f.form id="spbForm" name="spbForm">
            <@f.hidden id="proid" name="proid" value="${bdcXm.proid!}"/>
            <@f.hidden id="wiid" name="wiid" value="${bdcXm.wiid!}"/>
            <@f.hidden id="qllx" name="qllx" value="${bdcXm.qllx!}"/>
            <@f.hidden id="spxxid" name="spxxid" value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="qlid" name="qlid" value="${qllxVo.qlid!}"/>
            <@f.hidden id="ywh" name="ywh" value="${bdcXm.bh!}"/>
            <@f.hidden id="rowsize" name="rowsize" value="${rowsize!}"/>
            <@f.hidden id="csSignId"  name="csSignId" value="${csrSign.signId!}"/>
            <@f.hidden id="fsSignId"  name="fsSignId" value="${fsrSign.signId!}"/>
            <@f.hidden id="hdSignId"  name="hdSignId" value="${hdrSign.signId!}"/>
            <@f.hidden id="csSignKey"  name="csSignKey" value="csr"/>
            <@f.hidden id="fsSignKey"  name="fsSignKey" value="fsr"/>
            <@f.hidden id="hdSignKey"  name="hdSignKey" value="hdr"/>
            <div class="secondTitle" style="width:650px">
                <section id="jbxx">
                    <div class="row">
                        <div class=" col-xs-4 demonstrative">
                        </div>
                        <div class=" col-xs-4">
                        </div>
                        <div class="col-xs-4">
                            <img src="${bdcdjUrl!}/bdcPrint/getTxm?proid=${proid}"/>
                        </div>
                    </div>
                </section>
            </div>
            <@f.table style="width:650px">
                <@f.tr style="border:none;height:0px;">
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                </@f.tr>
                <#--<@f.tr>
                    <@f.td colspan="3" style="border:none; ">
                        <img src="${bdcdjUrl!}/bdcPrint/getTxm?proid=${proid}"/>
                    </@f.td>
                </@f.tr>-->
                <@f.tr>
                    <@f.th  colspan="3">
                        <@f.label name="登记类型"></@f.label>
                    </@f.th>
                    <@f.td colspan="10">
                        <@f.select id="djlx" name="djlx"  showFieldName="mc" valueFieldName="dm" source="djlxList" defaultValue="${bdcXm.djlx!}" noEmptyValue="true" ></@f.select>
                    </@f.td>
                </@f.tr>

                <@f.tr>
                    <@f.th rowspan="8" colspan="1">
                        <@f.label name="                    不动产情况                   "></@f.label>
                    </@f.th>
                    <@f.th colspan="3">
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="bdcdyh" name="bdcdyh" value="${bdcSpxx.bdcdyh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="权利人"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="qlr" name="qlr" value="${qlr!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="坐落"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="zl" name="zl" value="${bdcSpxx.zl!}"></@f.text>
                    </@f.td>
                </@f.tr>

                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="不动产类型"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.select id="bdclx" name="bdclx"  showFieldName="MC" valueFieldName="DM" source="bdclxList" defaultValue="${bdcSpxx.bdclx!}" ></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="面积"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text id="mj" name="mj" value="${bdcSpxx.mj!}" ></@f.text>
                    </@f.td>

                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="用途"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.select id="yt" name="yt"  showFieldName="mc" valueFieldName="dm" source="fwytList" defaultValue="${bdcSpxx.yt!}" ></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="用途（纯土地）"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.select id="zdzhyt" name="zdzhyt"  showFieldName="MC" valueFieldName="DM" source="zdzhytList" defaultValue="${bdcSpxx.zdzhyt!}" ></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="不动产权证号"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="ybdcqzh" name="ybdcqzh" value="${bdcXm.ybdcqzh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th rowspan="5" colspan="1" >
                        <@f.label name="                    查封情况                   "></@f.label>
                    </@f.th>
                    <@f.th colspan="3">
                        <@f.label name="查封类型"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.select  id="cflx" name="cflx" showFieldName="mc" valueFieldName="dm" source="cflxList" defaultValue="${bdcCf.cflx!}"></@f.select>
                    </@f.td>
                    <@f.th colspan="3">
                        <@f.label name="查封范围"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text  id="cffw" name="cffw" value="${bdcCf.cffw!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="查封日期（起）"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.date  id="cfksqx" name="cfksqx" value="${cfksqx!}"></@f.date>
                    </@f.td>
                    <@f.th colspan="3">
                        <@f.label name="查封日期（止）"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.date  id="cfjsqx" name="cfjsqx" value="${cfjsqx!}"></@f.date>
                    </@f.td>
                </@f.tr>
                <@f.tr >
                    <@f.th colspan="3">
                        <@f.label name="查封文号"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="cfwh" name="cfwh" value="${bdcCf.cfwh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr >
                    <@f.th colspan="3">
                        <@f.label name="查封机关"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="cfjg" name="cfjg" value="${bdcCf.cfjg!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr >
                    <@f.th colspan="3">
                        <@f.label name="查封文件"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="cfwj" name="cfwj" value="${bdcCf.cfwj!}"></@f.text>
                    </@f.td>
                </@f.tr>

                <@f.tr>
                    <@f.th rowspan="6" colspan="1" >
                        <@f.label name="不动产登记审批情况"></@f.label>
                    </@f.th>
                    <@f.th rowspan="2" colspan="2">
                        <@f.label name="初审"></@f.label>
                    </@f.th>
                    <@f.td colspan="10" rowspan="1" height="90px">
                        <@f.textarea  id="csyj" name="csyj" value="${csrSign.signOpinion!}"></@f.textarea>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.td colspan="1" >
                        <@f.label name="初审人"></@f.label>
                    </@f.td>
                    <@f.td colspan="4" >
                        <@f.img src="${platformUrl!}/tag/signtag!image.action?signVo.signId=${csrSign.signId!}"width="145" height="50" signId="${csrSign.signId!}" />
                    </@f.td>
                    <@f.td colspan="1" >
                        <a onclick="sign('${proid!}','csr','${taskid!}','csyj','false')"><i
                                class="ace-icon glyphicon glyphicon-edit">签名</i></a>
                    </@f.td>
                    <@f.td colspan="4">
                        <@f.text  id="csyjqm" name="csyjqm" value="${csrq!}" style="text-align:center"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th rowspan="2" colspan="2">
                        <@f.label name="复审"></@f.label>
                    </@f.th>
                    <@f.td colspan="10" rowspan="1" height="90px">
                        <@f.textarea  id="fsyj" name="fsyj" value="${fsrSign.signOpinion!}"></@f.textarea>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.td colspan="1" >
                        <@f.label name="复审人"></@f.label>
                    </@f.td>
                    <@f.td colspan="4" >
                        <@f.img src="${platformUrl!}/tag/signtag!image.action?signVo.signId=${fsrSign.signId!}"width="145" height="50" signId="${fsrSign.signId!}" />
                    </@f.td>
                    <@f.td colspan="1" >
                        <a onclick="sign('${proid!}','fsr','${taskid!}','fsyj','false')"><i
                                class="ace-icon glyphicon glyphicon-edit">签名</i></a>
                    </@f.td>
                    <@f.td colspan="4">
                        <@f.text  id="fsyjqm" name="fsyjqm" value="${fsrq!}" style="text-align:center"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th rowspan="2" colspan="2">
                        <@f.label name="核定"></@f.label>
                    </@f.th>
                    <@f.td colspan="10" rowspan="1" height="90px">
                        <@f.textarea  id="hdyj" name="hdyj" value="${hdrSign.signOpinion!}"></@f.textarea>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.td colspan="1" >
                        <@f.label name="核定人"></@f.label>
                    </@f.td>
                    <@f.td colspan="4" >
                        <@f.img src="${platformUrl!}/tag/signtag!image.action?signVo.signId=${hdrSign.signId!}"width="145" height="50" signId="${hdrSign.signId!}" />
                    </@f.td>
                    <@f.td colspan="1" >
                        <a onclick="sign('${proid!}','hdr','${taskid!}','hdyj','false')"><i
                                class="ace-icon glyphicon glyphicon-edit">签名</i></a>
                    </@f.td>
                    <@f.td colspan="4">
                        <@f.text  id="hdyjqm" name="hdyjqm" value="${hdrq!}" style="text-align:center"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3" height="100px">
                        <@f.label name="备注"></@f.label>
                    </@f.th>
                    <@f.td colspan="10" >
                        <@f.textarea  id="bz" name="bz" value="${bdcXm.bz!}"></@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
</div>
</@com.html>
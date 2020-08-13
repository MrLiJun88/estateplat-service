<@com.html title="不动产登记业务管理系统" import="ace,public,init">
    <#include "../../../batch/qtdjxx/defaultOpinion.ftl">
    <@script name="static/js/wf.js"></@script>
    <@script name="static/js/sign.js"></@script>
<script type="text/javascript" xmlns="http://www.w3.org/1999/html">
    i = 1;
    e = 1;
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
    //初始化一些监听事件
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
                    alert(msg);
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
                    alert(msg);
                }
            }
        });
        //填写共有比例计算权利人共有比例是否总计为1
//            $("input[name='qlbl']").change(function () {
//                var gybl=$(this).val();
//                var msg=ValidateGybl(gybl);
//                if(msg!=null){
//                    alert(msg);
//                }
//                else{
//                    var msggyfs= changGyfs();
//                    if(msggyfs!=null){
//                        alert(msggyfs);
//                    }
//                }
//            });
        //集成身份证读卡器qlrmc
        $("input[name='qlrmc']").dblclick(function () {
            var index = $(this).attr('id');
            var y = index.lastIndexOf("_");
            var id = index.substr(y + 1);
            var qlrzjh = "qlr_qlrzjh_" + id;
            ReadIDCardNew(index, qlrzjh);
        });
        ///计算权利人数量并对分别持证进行更改
        changFbcz();
        limGyfs();
    }
    $(document).ready(function () {
        //重定义行
        $("#sqrqk").attr("rowspan", sqrQk(i));
        initDyqk();
        changelisten();
    });
    //根据权利类型判断显示抵押情况信息
    function  initDyqk() {
        var qllx=$("#qllx").val();
        if(qllx=="18"){
            $("#ygdyqk").hide();
        }
        else if(qllx=="19"){
            $("#dyqk").hide();
        }
        else {
            $("#dyqk").hide();
            $("#ygdyqk").hide();
        }
    }
</script>
    <@script name="static/js/batch/commonSqsAndSpb.js"></@script>
    <@script name="static/js/batch/spb.js"></@script>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-info save" id="提交" name="提交" onclick="saveBdcSpbxx()">保存</button>
    </div>
    <div class="rightToolTop">
        <div class='btn-group'>
            <a class='btn btn-success' class="dropdown-toggle" data-toggle="dropdown" id="打印预览" name="打印预览"
               onclick="printSpb()">打印</a>
        </div>
    </div>
</div>
<div class="main-container">
    <@f.contentDiv  title="不动产登记审批表">
        <@f.form id="spbForm" name="spbForm">
            <@f.hidden id="proid" name="proid" value="${bdcXm.proid!}"/>
            <@f.hidden id="wiid" name="wiid" value="${bdcXm.wiid!}"/>
            <@f.hidden id="qllx" name="qllx" value="${bdcXm.qllx!}"/>
            <@f.hidden id="spxxid" name="spxxid" value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="qlid" name="qlid" value="${bdcFdcq.qlid!}"/>
            <@f.hidden id="ywh" name="ywh" value="${bdcXm.bh!}"/>
            <@f.hidden id="rowsize" name="rowsize" value="${rowsize!}"/>
            <@f.hidden id="csSignId"  name="csSignId" value="${csrSign.signId!}"/>
            <@f.hidden id="fsSignId"  name="fsSignId" value="${fsrSign.signId!}"/>
            <@f.hidden id="hdSignId"  name="hdSignId" value="${hdrSign.signId!}"/>
            <@f.hidden id="csSignKey"  name="csSignKey" value="csr"/>
            <@f.hidden id="fsSignKey"  name="fsSignKey" value="fsr"/>
            <@f.hidden id="hdSignKey"  name="hdSignKey" value="hdr"/>
            <div class="secondTitle" style="width:780px">
                <section id="jbxx">
                    <div class="row">
                        <div class=" col-xs-2 demonstrative">
                        </div>
                        <div class=" col-xs-4  ">
                        </div>
                        <div class=" col-xs-2 demonstrative dw ">
                            单位：
                        </div>
                        <div class="rowLabel col-xs-4 mjdwCol">
                            <@f.radio id="dw"name="mjdw" saveValue="${mjdw!}" defaultValue="1" valueFieldName="1">
                                平方米</@f.radio>
                            <@f.radio  id="dw"name="mjdw" saveValue="${mjdw!}" valueFieldName="2">亩</@f.radio>
                            <@f.radio  id="dw"name="mjdw" saveValue="${mjdw!}" valueFieldName="3">公顷、万元</@f.radio>
                        </div>
                    </div>
                </section>
            </div>
            <@f.table style="width:650px">
                <@f.tr style="border:none;height:0px;">
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:70px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:70px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:70px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:60px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:55px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:70px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:30px;"></@f.td>
                </@f.tr>
                <#include "top.ftl">
                <@f.tr>
                    <@f.th   colspan="1" id="sqrqk">
                        <@f.label name="                    申<br/>请<br/>人<br/>情<br/>况                   "></@f.label>
                    </@f.th>
                    <@f.th   colspan="11" >
                        <@f.label name="登&nbsp;&nbsp;&nbsp;&nbsp;记&nbsp;&nbsp;&nbsp;&nbsp;申&nbsp;&nbsp;&nbsp;&nbsp;请&nbsp;&nbsp;&nbsp;&nbsp;人"></@f.label>
                    </@f.th>
                </@f.tr>
                <#include "qlr.ftl">
                <@f.tr id="tr_qlr">
                    <@f.th    colspan="11" >
                        <@f.label name="登&nbsp;&nbsp;&nbsp;&nbsp;记&nbsp;&nbsp;&nbsp;&nbsp;申&nbsp;&nbsp;&nbsp;&nbsp;请&nbsp;&nbsp;&nbsp;&nbsp;人"></@f.label>
                    </@f.th>
                </@f.tr>
                <#include "ywr.ftl">
                <#include "bdcdyqk.ftl">
                <#--<#include "bottom.ftl">-->
                <#include "sign.ftl">
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="备注"></@f.label>
                    </@f.th>
                    <@f.td colspan="12">
                        <@f.text  id="bz" name="bz" value="${bdcXm.bz!}"></@f.text>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
</div>
</@com.html>
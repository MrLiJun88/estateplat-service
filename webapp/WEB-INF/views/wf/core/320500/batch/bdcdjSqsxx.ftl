<@com.html title="不动产登记业务管理系统" import="ace,public,init">
    <@script name="static/js/wf.js"></@script>
    <@script name="static/js/readCard.js"></@script>
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
</script>
    <@script name="static/js/batch/commonSqsAndSpb.js"></@script>
    <@script name="static/js/batch/sqs.js"></@script>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-info save" onclick="saveBdcSqsxx()">保存</button>
    </div>
    <div class="rightToolTop">
        <div class='btn-group'>
            <a class='btn btn-success' class="dropdown-toggle" onclick="printSqs()">打印</a>
        </div>
    </div>
</div>
<div class="main-container">
    <@f.contentDiv  title="不动产登记申请书">
        <@f.form id="sqsForm" name="sqsForm">
            <@f.hidden id="proid" name="proid" value="${bdcXm.proid!}"/>
            <@f.hidden id="qlid" name="qlid" value="${bdcFdcq.qlid!}"/>
            <@f.hidden id="wiid" name="wiid" value="${bdcXm.wiid!}"/>
            <@f.hidden id="qllx" name="qllx" value="${bdcXm.qllx!}"/>
            <@f.hidden id="spxxid" name="spxxid" value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="ywh" name="ywh" value="${bdcXm.bh!}"/>
            <@f.hidden id="rowsize" name="rowsize" value="${rowsize!}"/>
            <div class="secondTitle" style="width:650px;margin-left: 106px;">
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
                    <@f.td  style="border:none;height:0px;width:60px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:70px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:60px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:55px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:70px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:55px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:30px;"></@f.td>
                </@f.tr>
                <#include "top.ftl">
                <@f.tr>
                    <@f.th   colspan="1" id="sqrqk">
                        <@f.label name="                    申<br/>请<br/>人<br/>情<br/>况                   "></@f.label>
                    </@f.th>
                    <@f.th     colspan="11" >
                        <@f.label name="登&nbsp;&nbsp;&nbsp;&nbsp;记&nbsp;&nbsp;&nbsp;&nbsp;申&nbsp;&nbsp;&nbsp;&nbsp;请&nbsp;&nbsp;&nbsp;&nbsp;人"></@f.label>
                    </@f.th>

                </@f.tr>
                <#include "qlr.ftl">
                <@f.tr id="tr_qlr">
                    <@f.th     colspan="11" >
                        <@f.label name="登&nbsp;&nbsp;&nbsp;&nbsp;记&nbsp;&nbsp;&nbsp;&nbsp;申&nbsp;&nbsp;&nbsp;&nbsp;请&nbsp;&nbsp;&nbsp;&nbsp;人"></@f.label>
                    </@f.th>
                </@f.tr>
                <#include "ywr.ftl">
                <#include "bdcdyqk.ftl">
                <#include "bottom.ftl">
            </@f.table>
        </@f.form>
    </@f.contentDiv>
</div>
</@com.html>
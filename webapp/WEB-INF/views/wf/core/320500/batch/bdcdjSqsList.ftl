<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<#--authorize-->
<#--<#include "../../../common/rightsManagement.ftl">-->
<#--<#include "../../../common/fieldColorManagement.ftl">-->

<style type="text/css">
    .tableA > tbody > tr > td:nth-child(odd) {
        text-align: center;
        width: 20%;
        background: #fff;
    }

    .tableA > tbody > tr > td {
        text-align: center;
        width: 20%;
        background: #fff;
    }
    .formBody{
        width:1200px;
    }
</style>
<div class="main-container">
    <@f.contentDiv  title="不动产申请书" >
        <@f.form id="bdcdjSqsxxForm" name="bdcdjSqsxxForm">
            <@f.hidden id="proid" name="proid"  value="${proid!}"/>
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
            <@f.table style="width:650px">
                <@f.tr style="border:none;height:0px;">
                    <@f.td  style="border:none;height:0px;width:30px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:200px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:200px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:150px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:180px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:220px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:60px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:60px;"></@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="1" >
                        <@f.label name="序号"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="登记事由"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="权利人"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="义务人"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.th colspan="1" >
                        <@f.label name="坐落"></@f.label>
                    </@f.th>
                    <@f.th colspan="1">
                        <@f.label name="操作"></@f.label>
                    </@f.th>
                    <@f.th colspan="1">
                        <@f.label name="打印"></@f.label>
                    </@f.th>
                </@f.tr>
                <tbody id="bdcdyxx">
                    <#list returnValueList as value>
                        <@f.tr>
                            <@f.hidden id="proid_${value.bdcXm.proid!}" name="proid"  value="${value.bdcXm.proid!}"/>
                            <@f.td colspan="1" >
                                <@f.text id="xh" name="xh"  value="${value.index!}"></@f.text>
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.select id="djsy" name="djsy"  showFieldName="MC" valueFieldName="DM" source="djsyMcList" defaultValue="${value.bdcXm.djsy!}" noEmptyValue="true" ></@f.select>
                            </@f.td>
                            <@f.td colspan="1" >
                                <#list value.bdcQlrList as qlr>
                                    <@f.hidden id="qlrid_${value.bdcdyh}" name="qlrid"  value="${qlr.qlrid!}"/>
                                    <@f.text  id="qlrmc_${qlr.qlrid!}_${value.bdcdyh!}" name="qlrmc" value="${qlr.qlrmc!}" style="text-align:center"></@f.text>
                                </#list>
                            </@f.td>
                            <@f.td colspan="1" >
                                <#list value.bdcYwrList as qlr>
                                    <@f.hidden id="qlrid_${value.bdcdyh}" name="qlrid"  value="${qlr.qlrid!}"/>
                                    <@f.text  id="qlrmc_${qlr.qlrid!}_${value.bdcdyh!}" name="qlrmc" value="${qlr.qlrmc!}" style="text-align:center"></@f.text>
                                </#list>
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.text  id="bdcdyh_${value.bdcXm.proid!}" name="bdcdyh" value="${value.bdcdyh!}"></@f.text>
                            </@f.td>
                            <@f.td colspan="1" >
                                <@f.text  id="zl_${value.bdcXm.proid!}" name="zl" value="${value.bdcSpxx.zl!}" style="text-align:center"></@f.text>
                            </@f.td>
                            <@f.td colspan="1">
                            <a href="javascript:void(0)" id="update_${value.bdcXm.proid!}"
                               onclick="update('${value.bdcXm.proid!}')">修改</a>
                            </@f.td>
                            <@f.td colspan="1">
                            <a href="javascript:void(0)" id="printsqs_${value.bdcXm.proid!}"
                               onclick="printSqs('${value.bdcXm.proid!}')">打印</a>
                            </@f.td>
                        </@f.tr>
                    </#list>
                </tbody>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
</div>
</@com.html>
<script type="text/javascript">
    var bdcdjUrl = "${bdcdjUrl!}";
    var reportUrl = "${reportUrl!}";
    var etlUrl = "${etlUrl!}";
    var wiid = "${wiid!}";
    var proid = "${proid!}";
    var portalUrl = "${portalUrl!}";
    var platformUrl = "${path_platform!}"
    var analysisUrl = "${analysisUrl!}";
    var taskid = "${taskid!}";
    var from = "${from!}";
    var rid = "${rid!}";
    e = 1;
    function update(proid) {
        proid = "in('" + proid + "')";
        var url = '${bdcdjUrl!}/bdcdjSqsxx?wiid=${wiid!}&proid=${proid!}';
        openWin(url);
    }
    function printSqs(proid) {
        var url = "${reportUrl!}/ReportServer?reportlet=print%2F${sqsreportName!}&__cutpage__=v&op=write&proid=" + proid;
        openWin(url);
    }
</script>
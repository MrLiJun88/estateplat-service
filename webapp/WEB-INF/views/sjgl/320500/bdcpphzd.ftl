<@com.html title="匹配清单信息" import="ace,public,init">
<style type="text/css">
    /*去掉表格横向滚动条*/
    /*.ui-jqgrid-bdiv{
        overflow-x: hidden!important;
    }*/

    /*表单样式重写*/
    .leftToolTop {
        margin: 0 auto;
        width: 20px;
        line-height: 24px;
        border: 0px solid #333;
        margin-right:44px;
    }
    .main-container{
        text-align: center;
        font-size: 200%;
    }
    .tableA>tbody>tr>th {
        text-align: center ;
        padding-right: 15px;
    }
    th {
        text-align: center;
    }
</style>
<script>
    var bdcdyh = "${bdcdyh!}"
    var qlid = "${qlid!}"
    function print(){
        var url = "${reportUrl!}/ReportServer?reportlet=print%2Fbdc_pphzd.cpt&op=write&bdcdyh=" + bdcdyh + "&qlid=" + qlid + "&qlids=" + qlid;
        openWin(url);
    }
</script>

<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-info save" onclick="print()">打印</button>
    </div>
</div>
<div class="main-container" >
    <@f.contentDiv title="苏州地籍测绘队不动产单元号留存备查单">
        <@f.form id="ppdForm" name="ppdForm">
            <@f.table style="width:750px">
                <@f.tr style="border:none;height:0px;">
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:60px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:55px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:70px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:30px;"></@f.td>
                </@f.tr>
            <#if ("${ppdXxIsNull!}" == "false")>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="权利人"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="qlrmc" name="qlrmc" value="${qlrmc!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="坐落"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="zl" name="zl" value="${zl!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="bdcdyh" name="bdcdyh" value="${bdcdyh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="房产证号"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="fczh" name="fczh" value="${fczh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="土地证号"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="tdz" name="tdz" value="${tdz!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="业务类型"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="bz" name="bz" value="${bz!}"></@f.text>
                    </@f.td>
                </@f.tr>
            <#else>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="权利人"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="qlrmc" name="qlrmc" value=""></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="坐落"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="zl" name="zl" value=""></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="bdcdyh" name="bdcdyh" value=""></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="房产证号"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="fczh" name="fczh" value=""></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="土地证号"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="tdz" name="tdz" value=""></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="业务类型"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="bz" name="bz" value=""></@f.text>
                    </@f.td>
                </@f.tr>
            </#if>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
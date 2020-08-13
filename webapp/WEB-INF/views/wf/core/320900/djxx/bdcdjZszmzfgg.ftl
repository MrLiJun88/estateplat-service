<@com.html title="不动产登记业务管理系统" import="ace,public,init,authorize">
<#--<#include "../../../common/rightsManagement.ftl">-->
<#--<#include "../../../common/fieldColorManagement.ftl">-->
<#--<script src="../js/sjd.js"></script>-->
<#--<#assign path="${request.getContextPath()}">-->
<script src="js/sjd.js" type="text/javascript"></script>
<script type="text/javascript">
    var bdcdjUrl = "${bdcdjUrl!}";
    var reportUrl = "${reportUrl!}";
    var etlUrl = "${etlUrl!}";
    var wiid = "${wiid!}";
    var proid = "${proid!}";
    var portalUrl = "${portalUrl!}";
    var platformUrl = "${path_platform!}";
    var analysisUrl = "${analysisUrl!}";
    var taskid = "${taskid!}";
    var from = "${from!}";
    var rid = "${rid!}";
    e = 1;
    function saveGg() {
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: bdcdjUrl + "/bdcdjGg/saveGg",
            type: 'POST',
            dataType: 'json',
            data: $("#ggForm").serialize(),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        alert("保存成功!");
                    }
                }
            },
            error: function (data) {
                alert("保存失败!");
            }
        });
    }
    function printBysl() {
        var proid = "${proid!}";
        var url = "${reportUrl!}/ReportServer?reportlet=print%2Fbdc_zszmzfgg.cpt&proid=" + proid+'&wiid='+wiid;
        openWin(url);
    }
</script>

<style type="text/css">
    span {
        border-bottom: 1px solid black;
    }

    .toparea {
        width: 900px;
        position: relative;
        text-align: left;
    }

    .bottomarea {
        width: 900px;
        text-align: right;
        position: relative;
        margin-top: 20px;
    }

    .bottomarea textarea {
        border-bottom: 1px solid black;
    }

    .toparea .info {
        margin-top: 5px;
        margin-bottom: 10px;
    }

    .toparea .bh {
        position: relative;
        margin-top: 40px;
        text-align: right;
    }

    .formBody {
        width: 900px;
    }
    .toparea .cldz {
        position: relative;
        text-align: center;
        margin-top: 20px;
        margin-bottom: 20px;
    }

    #clsddz{
        position: relative;
        border-bottom: 1px solid black;
        width: 300px;
    }


</style>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-primary save" id="sjdxxSaveButton" onclick="saveGg()">保存</button>
        <button type="button" class="btn btn-info save" onclick="printBysl()">打印</button>
    </div>
</div>
<div class="main-container">
    <@f.contentDiv  title="${bdcGg.ggmc!}" >
        <@f.form id="ggForm" name="ggForm">
            <@f.hidden id="proid" name="proid"  value="${proid!}"/>
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
            <@f.hidden id="userid" name="userid"  value="${userid!}"/>
            <@f.hidden id="ggid" name="ggid"  value="${bdcGg.ggid!}"/>
            <@f.hidden id="ggbh" name="ggbh"  value="${bdcGg.ggbh!}"/>
            <@f.hidden id="ggdw" name="ggdw"  value="${bdcGg.ggdw!}"/>
            <div class="toparea">
                <div class="bh"><p>编号：<span>${bdcGg.ggbh!}</span></p></div>
                <div class="info">
                    <p> 因我机构无法收回下列不动产权属证书和不动产登记证明，根据《不动产登记暂行条例实施细则》第二十三条的规定，现公告作废。</p>
                </div>
                <div class="cldz"><p>异议书面材料送达地址:<span><input id="clsddz" name="clsddz" value="${bdcGg.clsddz!}"/></span></p></div>
            </div>
            <@f.table style="width:650px">
                <@f.tr style="border:none;height:0px;">
                    <@f.td  style="border:none;height:0px;width:250px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:100px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:100px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:100px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:250px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:100px;"></@f.td>
                </@f.tr>

                <@f.tr>
                    <@f.th colspan="1">
                        <@f.label name="不动产权属证书或不动产登记证明号"></@f.label>
                    </@f.th>
                    <@f.th colspan="1">
                        <@f.label name="权利人"></@f.label>
                    </@f.th>
                    <@f.th colspan="1">
                        <@f.label name="不动产权利类型"></@f.label>
                    </@f.th>
                    <@f.th colspan="1">
                        <@f.label name="宗地/宗海号"></@f.label>
                    </@f.th>
                    <@f.th colspan="1">
                        <@f.label name="不动产坐落（名称）"></@f.label>
                    </@f.th>
                    <@f.th colspan="1">
                        <@f.label name="备注"></@f.label>
                    </@f.th>
                </@f.tr>
                <tbody id="bdcqk">
                    <#list resultList as value>
                        <@f.tr>
                        <@f.td colspan="1" >
                            <@f.text  id="bdcqzh_${value.bdcZs.bdcqzh!}" name="bdcqzh" value="${value.bdcZs.bdcqzh!}"></@f.text>
                        </@f.td>
                        <@f.td colspan="1" >
                            <#list value.bdcQlrList as qlr>
                                <@f.hidden id="qlrid_${qlr.qlrid}" name="qlrid"  value="${qlr.qlrid!}"/>
                                <@f.text  id="qlrmc_${qlr.qlrid!}" name="qlrmc" value="${qlr.qlrmc!}" style="text-align:center"></@f.text>
                            </#list>
                        </@f.td>
                        <@f.td colspan="1" >
                            <@f.select  id="qllx_${value.bdcXm.proid!}" name="qllx"   showFieldName="mc" valueFieldName="dm" source="qllxList"  defaultValue="${value.bdcXm.qllx!}" noEmptyValue="true" ></@f.select>
                        </@f.td>
                        <@f.td colspan="1" >
                            <@f.text  id="zdzhh_${value.bdcSpxx.spxxid!}" name="zdzhh" value="${value.bdcSpxx.bdcdyh!}" style="text-align:center"></@f.text>
                        </@f.td>
                        <@f.td colspan="1" >
                            <@f.text  id="zl_${value.bdcSpxx.spxxid!}" name="zl" value="${value.bdcSpxx.zl!}" style="text-align:center"></@f.text>
                        </@f.td>
                        <@f.td colspan="1" >
                            <@f.text  id="bz_${bdcGg.ggid!}" name="bz" value="${bdcGg.bz!}" style="text-align:center"></@f.text>
                        </@f.td>
                    </@f.tr>
                    </#list>
                </tbody>
            </@f.table>
            <div class="bottomarea">
                <p>公告单位：<span>${bdcGg.ggdw!}</span></p>
            </div>
        </@f.form>
    </@f.contentDiv>
</div>
</@com.html>